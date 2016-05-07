package priv.adt.interval.CourseSchedule;

import priv.adt.interval.CommonMultiIntervalSet;
import priv.adt.interval.Interval;

/**
 * 表示排课系统，满足如下的要求：
 * 1.时间段可不连续
 * 2.时间段互相可以重叠
 * 3.标签可以重复
 * 4.以一周为周期循环
 */
public class CourseIntervalSet<L> extends CommonMultiIntervalSet<L> {

    public CourseIntervalSet(long new_Start, long new_End) {
        super(new_Start, new_End);
    }

    /**
     * 根据上课的时间进行排序
     */
    public void sort() {
        sum_interval.sort((Interval<L> o1, Interval<L> o2) -> (int) (o1.get_start() - o2.get_start()));
    }
}
