package priv.adt.interval;

import priv.adt.interval.ProcessSchedule.ProcessIntervalSet;
import org.junit.Test;
import priv.adt.interval.ProcessSchedule.Process;
import static org.junit.Assert.*;

/**
 * 对ProcessIntervalSet类测试
 */
public class ProcessIntervalSetTest {

    /*
    测试策略：
    1.先测试合理的进程调度
    2.再测试存在冲突的情况
     */
    @Test
    public void testCheck(){
        try {
            ProcessIntervalSet<Process> test = new ProcessIntervalSet<>(0, 50);
            Process test_1 = new Process("x", "x", 25, 25);
            Process test_2 = new Process("y", "z", 25, 25);
            Process test_3 = new Process("z", "z", 25, 25);
            test.insert(0,12,test_1);
            test.insert(13,25,test_2);
            test.check();

            test.insert(10,20,test_3);
            test.check();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}
