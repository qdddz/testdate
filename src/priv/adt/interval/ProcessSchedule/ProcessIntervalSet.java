package priv.adt.interval.ProcessSchedule;

import priv.adt.decorator.OverlapNotAllowedSet;
import priv.adt.interval.CommonMultiIntervalSet;
import priv.adt.interval.Interval;
import priv.adt.interval.IntervalSet;

/**
 * 表示进程管理系统，满足如下的要求：
 * 1.标签可以重复
 * 2.时间段必须连续
 * 3.时间段不允许重叠
 * 4.无需周期性
 */
public class ProcessIntervalSet<L> extends CommonMultiIntervalSet<L> {

    /*
    AF:(sum_interval) = 总进程的调度
    (L) = 各个进程
   （Interval对象）=进程工作时间段起点与终点
   （start） = CPU的开始时间
   （end） = CPU的结束时间

    RI：对于每一个intervals：
    1.L不应为空，且唯一
    2.0<=new_Start<=start<=end<=new_End

    Safe from RE：
    用protected final修饰
    */

    public ProcessIntervalSet(IntervalSet<L> initial) {
        super(initial);
    }

    public ProcessIntervalSet(long new_Start, long new_End) {
        super(new_Start, new_End);
    }

    //根据开始的时间排序
    public void sort() {
        sum_interval.sort((Interval<L> o1, Interval<L> o2)
                -> (int) (o1.get_start() - o2.get_start()));
    }

    public void check() {
        OverlapNotAllowedSet<L> check = new OverlapNotAllowedSet<>(this);
        if (check.check_Overlap()) {
            throw new RuntimeException("调度时间存在冲突！");
        }
    }
}
