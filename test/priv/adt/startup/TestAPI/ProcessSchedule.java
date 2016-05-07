package priv.adt.startup.TestAPI;

import org.junit.Test;
import priv.adt.interval.ProcessSchedule.Process;

import java.util.HashSet;

/**
 * 进程调度系统测试
 */

public class ProcessSchedule {
    @Test
    public void add_test(){
        priv.adt.startup.API.ProcessSchedule process_Schedule;
        process_Schedule = new priv.adt.startup.API.ProcessSchedule();
        HashSet<Process> process;
        process = new HashSet<>();
        Process test_A = new Process("501", "DVA", 11, 45);
        Process test_B = new Process("502", "VIVY", 14, 16);
        Process test_C = new Process("503", "GRACE", 3, 7);
        process.add(test_A);
        process.add(test_B);
        process.add(test_C);
        process_Schedule.add_Process(process);
    }

    @Test
    public void random_Test(){
        priv.adt.startup.API.ProcessSchedule process_Schedule;
        process_Schedule = new priv.adt.startup.API.ProcessSchedule();
        HashSet<Process> process;
        process = new HashSet<>();
        Process test_A = new Process("501", "DVA", 11, 45);
        Process test_B = new Process("502", "VIVY", 14, 16);
        Process test_C = new Process("503", "GRACE", 3, 7);
        process.add(test_A);
        process.add(test_B);
        process.add(test_C);
        process_Schedule.add_Process(process);
        process_Schedule.randomSchedule();
    }
}
