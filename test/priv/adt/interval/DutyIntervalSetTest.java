package priv.adt.interval;

import priv.adt.interval.DutyRoster.DutyIntervalSet;
import org.junit.Test;
import priv.adt.interval.DutyRoster.Employee;
import static org.junit.Assert.*;

/**
 * 对DutyIntervalSet类测试
 */
public class DutyIntervalSetTest {

    /*
    测试策略：
    1.首先测试合法的排班表
    2.再测试非法排班表
     */
    @Test
    public void testCheck(){
        try {
            DutyIntervalSet<Employee> dutySet = new DutyIntervalSet<>(0, 25);
            dutySet.insert(0,5,new Employee("DVA","1","123456"));
            dutySet.insert(6,15,new Employee("VIVY","2","123125"));
            dutySet.check();

            dutySet.insert(16,22,new Employee("GRACE","3","121546"));
            dutySet.check();

            dutySet.insert(22,25,new Employee("JOE","4","45467"));
            dutySet.check();

            dutySet.insert(20,22,new Employee("NOMAD","4","457897"));
            dutySet.check();
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
    }
}
