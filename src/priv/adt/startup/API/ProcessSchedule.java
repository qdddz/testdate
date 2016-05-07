package priv.adt.startup.API;

import priv.adt.interval.Interval;
import priv.adt.interval.ProcessSchedule.Process;
import priv.adt.interval.ProcessSchedule.ProcessIntervalSet;

import java.util.*;

/**
 * CPU进程调度系统
 */

    /*
    AF:（process_interval） = 进程调度表
    （processList） = 进程及其剩余最大执行时间

    RI:
    定义的元素均需要合法

    safe from RE:
    用private修饰
    */

public class ProcessSchedule {
    private final ProcessIntervalSet<Process> process_interval;
    private final HashMap<Process, Long> process_List;//value表示距离已调度时间最大执行时间的时间差，如果执行结束就移除

    public ProcessSchedule() {
        process_interval = new ProcessIntervalSet<>(0, Long.MAX_VALUE);
        process_List = new HashMap<>();
    }

    /**
     * 向进程表中添加一组进程
     *
     * @param processSet 待添加进程
     */
    public void add_Process(Set<Process> processSet) {
        for (Process a : processSet)
            process_List.put(a, a.get_Max());
    }

    public ProcessIntervalSet<Process> get_interval() {
        return process_interval;
    }

    public HashMap<Process, Long> get_List() {
        return process_List;
    }

    /**
     * 随机选择进程策略
     */
    public void randomSchedule() {
        long now = 0;
        Random random = new Random();
        while (process_List.size() > 0) {
            if (Math.random() < 0.5) {//随机闲置
                //随机选一个进程
                int rand_Process = random.nextInt(process_List.size());
                ArrayList<Map.Entry<Process, Long>> entries = new ArrayList<>(process_List.entrySet());
                Map.Entry<Process, Long> chosen = entries.get(rand_Process);

                //根据所选进程，随机选择一个执行时间
                int Execute_Time = random.nextInt((int) chosen.getValue().longValue()) + 1;

                //插入进程调度表
                process_interval.insert(now, now + Execute_Time, chosen.getKey());

                //修改剩余执行时间
                process_List.put(chosen.getKey(), chosen.getValue() - Execute_Time);

                //如果已经落在执行结束的区间，从待被调度进程表中删除对应进程
                if (is_Finish(chosen.getKey()))
                    process_List.remove(chosen.getKey());
                //时间推进
                now += Execute_Time;

            }
            now++;
        }

        Print_Process(now);
    }

    /**
     * 最短进程优先策略
     */
    public void SJFSchedule() {
        long now = 0;
        Random random = new Random();
        while (process_List.size() > 0) {
            if (Math.random() >= 0.5) {//随机闲置
                now++;
                continue;
            }

            //选择离最大执行时间最短的进程
            Map.Entry<Process, Long> chosenProcess = null;
            for (Map.Entry<Process, Long> entry : process_List.entrySet()) {
                if (chosenProcess == null) {
                    chosenProcess = entry;
                } else {
                    if (chosenProcess.getValue() > entry.getValue()) {
                        chosenProcess = entry;
                    }
                }
            }

            //根据所选进程，随机选择一个执行时间
            int randExecuteTime = random.nextInt((int) chosenProcess.getValue().longValue()) + 1;

            //插入进程调度表
            process_interval.insert(now, now + randExecuteTime, chosenProcess.getKey());

            //修改剩余执行时间
            process_List.put(chosenProcess.getKey(), chosenProcess.getValue() - randExecuteTime);

            //如果已经落在执行结束的区间，从待被调度进程表中删除对应进程
            if (is_Finish(chosenProcess.getKey())) {
                process_List.remove(chosenProcess.getKey());
            }

            //时间推进
            now += randExecuteTime;
        }

        Print_Process(now);
    }

    //判断进程是否执行结束
    private boolean is_Finish(Process process) {
        return (process.get_Max() - process.get_Min() >= process_List.get(process));
    }

    /**
     * 打印进程调度情况
     *
     * @param total_time 表示所有进程一共执行消耗的总时间
     */
    public void Print_Process(long total_time) {
        System.out.println("                  进程调度情况                 ");
        System.out.println("总运行时长：" + total_time);
        System.out.println("进程ID 进程名 运行起始时间 运行结束时间");
        process_interval.sort();
        ArrayList<Interval<Process>> new_interval = process_interval.get_intervalSet();
        for (Interval<Process> a : new_interval)
            System.out.println(a.get_label().get_id() + " " + a.get_label().get_name() + " " + a.get_start() + " " + a.get_end());
    }

}
