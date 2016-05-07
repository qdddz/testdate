package priv.adt.util;

import priv.adt.interval.CourseSchedule.CourseIntervalSet;
import org.junit.Test;
import priv.adt.startup.API.IntervalSetUtils;

import static org.junit.Assert.*;

/**
 * 对IntervalSetUtils类测试
 */
public class IntervalSetUtilsTest {

    /*
    测试策略
    similarity测试:
    1.完全相似的集合
    2.部分相似的结合
    3.完全不相似的结合

    calcFreeTimeRatio
    1.完全空闲
    2.完全没有空闲
    3.部分空闲

     */
    private static final double DELTA = 0.001;

    @Test
    public void sim_test1() {
        CourseIntervalSet<String> interval_a = new CourseIntervalSet<>(0, 30);
        CourseIntervalSet<String> interval_b = new CourseIntervalSet<>(0, 30);
        interval_a.insert(0,20,"DVA");
        interval_a.insert(20,30,"VIVY");
        interval_b.insert(0,20,"DVA");
        interval_b.insert(20,30,"VIVY");
        assert (Math.abs(IntervalSetUtils.cal_similarity(interval_a, interval_b) - 1) < DELTA);

    }

    @Test
    public void sim_test2() {
        CourseIntervalSet<String> interval_a = new CourseIntervalSet<>(0, 25);
        CourseIntervalSet<String> interval_b = new CourseIntervalSet<>(0, 35);

        interval_a.insert(0, 5, "DVA");
        interval_a.insert(10, 20, "VIVY");
        interval_a.insert(20, 25, "DVA");
        interval_b.insert(10, 20, "VIVY");
        interval_b.insert(20, 35, "DVA");
        interval_b.insert(0, 5, "GRACE");

        assert (Math.abs(IntervalSetUtils.cal_similarity(interval_a, interval_b) - 0.42875) < DELTA);
    }

    @Test
    public void sim_test3() {
        CourseIntervalSet<String> interval_a = new CourseIntervalSet<>(0, 25);
        CourseIntervalSet<String> interval_b = new CourseIntervalSet<>(0, 35);
        interval_a.insert(0,15,"DVA");
        interval_a.insert(20,25,"VIVY");
        interval_b.insert(0,20,"GRACE");
        interval_b.insert(20,35,"OPHELIA");
        assert (Math.abs(IntervalSetUtils.cal_similarity(interval_a, interval_b) - 0) < DELTA);
    }

     /*
    测试策略
    calcConflictRatio测试：
    1.完全不冲突
    2.部分有冲突
    3.完全有冲突
    */
    @Test
    public void Conflict_test1() {
        CourseIntervalSet<String> new_interval = new CourseIntervalSet<>(0, 50);
        new_interval.insert(0, 5, "DVA");
        new_interval.insert(15, 20, "DVA");
        new_interval.insert(25, 30, "VIVY");
        assert (Math.abs(IntervalSetUtils.cal_ConflictRatio(new_interval) - 0) < DELTA);
    }

    @Test
    public void Conflict_test2() {
        CourseIntervalSet<String> new_interval = new CourseIntervalSet<>(0, 100);
        new_interval.insert(0, 5, "DVA");
        new_interval.insert(10, 20, "DVA");
        new_interval.insert(25, 30, "VIVY");
        new_interval.insert(10, 20, "VIVY");
        assert (Math.abs(IntervalSetUtils.cal_ConflictRatio(new_interval) - 0.1) < DELTA);
    }

    @Test
    public void Conflict_test3() {
        CourseIntervalSet<String> new_intervalSet = new CourseIntervalSet<>(0, 10);
        new_intervalSet.insert(0, 5, "DVA");
        new_intervalSet.insert(5, 10, "DVA");
        new_intervalSet.insert(0, 5, "VIVY");
        new_intervalSet.insert(5, 10, "VIVY");
        assert (Math.abs(IntervalSetUtils.cal_ConflictRatio(new_intervalSet) - 1) < DELTA);
    }

    /*
    测试策略
    calcFreeTimeRatio测试
    1.完全空闲
    2.有部分时间空闲
    3.没有空闲时间
    */

    @Test
    public void Free_test1(){

        CourseIntervalSet<String> new_intervalSet = new CourseIntervalSet<>(1, 8);
        assertEquals(1,IntervalSetUtils.cal_FreeTimeRatio(new_intervalSet),DELTA);
    }

    @Test
    public void Free_test2(){
        CourseIntervalSet<String> new_intervalSet = new CourseIntervalSet<>(1, 8);
        new_intervalSet.insert(1, 2, "DVA");
        new_intervalSet.insert(3, 4, "VIVY");
        assertEquals(0.5,IntervalSetUtils.cal_FreeTimeRatio(new_intervalSet),DELTA);
    }

    @Test
    public void Free_test3(){
        CourseIntervalSet<String> new_intervalSet = new CourseIntervalSet<>(1, 8);

        new_intervalSet.insert(1, 2, "DVA");
        new_intervalSet.insert(3,4,"GRACE");
        new_intervalSet.insert(5, 6, "VIVY");
        new_intervalSet.insert(7,8,"OPHELIA");
        assertEquals(0,IntervalSetUtils.cal_FreeTimeRatio(new_intervalSet),DELTA);
    }
}
