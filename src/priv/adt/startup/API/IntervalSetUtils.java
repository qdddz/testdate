package priv.adt.startup.API;

import priv.adt.interval.CommonMultiIntervalSet;
import priv.adt.interval.CourseSchedule.Course;
import priv.adt.interval.CourseSchedule.CourseIntervalSet;
import priv.adt.interval.Interval;
import priv.adt.interval.IntervalSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 工具类主要提供以下三种功能：
 * 1.相似度的计算
 * 2.时间冲突比例的计算
 * 3.空闲时间比例的计算
 */
@SuppressWarnings("rawtypes")
public class IntervalSetUtils {


    public static double cal_Free(CourseSchedule courseSchedule) {
        long not_free = 0;
        ArrayList<Map.Entry<Course, Integer>> a = courseSchedule.getIndex();

        int len = 168;
        double result;
        int[] period = new int[len];
        Arrays.fill(period, 0);

        for (Map.Entry<Course, Integer> course : a) {
            Integer b = course.getValue();
            Arrays.fill(period, b, b + 2, 1);
        }

        for (int j : period) {
            if (j == 1) {
                not_free++;
            }
        }
        result = 1 - ((double) not_free / 70);
        return result;
    }

    public static double cal_similarity(CommonMultiIntervalSet<?> a, CommonMultiIntervalSet<?> b) {

        long sim;
        sim = 0;
        double result;
        ArrayList<? extends Interval<?>> interval_a = a.get_intervalSet();
        ArrayList<? extends Interval<?>> interval_b = b.get_intervalSet();

        for (Interval<?> interval1 : interval_a) {
            for (Interval<?> interval2 : interval_b) {
                if (interval1.get_label().equals(interval2.get_label()))
                    sim += intersection(interval1, interval2);
            }
        }
        result = (double) sim / (double) (Math.max(a.new_End(), b.new_End()) - Math.min(a.new_Start(), b.new_Start()));
        return result;
    }

    /**
     * 计算IntervalSet的子类对象中的冲突比例，仅针对课表系统使用
     *
     * @param set 待处理的IntervalSet实现类对象
     * @return 冲突比例
     */
    public static double cal_ConflictRatio(IntervalSet<?> set) {

        long con = 0;
        double sum;
        Collection<? extends Interval<?>> new_interval = set.get_intervalSet();

        for (Interval<?> a : new_interval) {
            for (Interval<?> b : new_interval) {
                if (!a.equals(b))
                    con += intersection(a, b);
            }
        }
        sum = (double) con / (double) (2 * (set.new_End() - set.new_Start()));
        return sum;
    }

    /**
     * 计算CommonIntervalSet的子类对象中的空闲时间比例
     *
     * @param set 待处理的IntervalSet实现类对象，这里特指CommonIntervalSet的子类对象
     * @return 空闲时间比例
     */
    public static double cal_FreeTimeRatio(IntervalSet<?> set) {

        long cnt;
        double sum;
        long new_Start = set.new_Start();
        long new_End = set.new_End();
        int size = (int) (new_End - new_Start + 1);
        int[] period = new int[(int) size];
        cnt = 0;
        Arrays.fill(period, 0);

        Collection<? extends Interval<?>> new_interval = set.get_intervalSet();
        for (Interval<?> interval : new_interval) {
            Arrays.fill(period, (int) (interval.get_start() - new_Start), (int) (interval.get_end() - new_Start) + 1, 1);
        }

        for (int j : period) {
            if (j == 0) {
                cnt++;
            }
        }
        sum = (double) cnt / (double) size;
        return sum;
    }

    //返回两个Interval对象的交集时间
    private static long intersection(Interval a, Interval b) {
        long Start_a = a.get_start();
        long End_a = a.get_end();
        long Start_b = b.get_start();
        long End_b = b.get_end();

        if (End_a <= Start_b || End_b <= Start_a) {
            return 0;
        } else if ((Start_a <= Start_b && End_a >= End_b) || (Start_b <= Start_a && End_b >= End_a)) {
            return Math.min(End_b - Start_b, End_a - Start_a);
        } else {
            return Math.min(Math.abs(End_a - Start_b), Math.abs(End_b - Start_a));
        }

    }

    /**
     * 计算CourseIntervalSet类中空闲时间比例，专用于课表系统
     * <p>注：由于课表系统的时间段不连续，有些时间段不应纳入计算
     *
     * @param set 待计算的课表
     */
    public static double cal_FreeTimeRatio(CourseIntervalSet<Course> set) {
        ArrayList<Interval<Course>> new_interval = set.get_intervalSet();
        long sum = 0;
        double result;
        for (Interval<Course> a : new_interval)
            sum += (a.get_end() - a.get_start());
        result = 1 - (double) sum / (double) (10 * 7);
        return result;
    }

    /**
     * 计算CourseIntervalSet类中冲突的时间比例，专用于课表系统
     * <p>注：由于课表系统的时间段不连续，有些时间段不应纳入计算
     *
     * @param set 待计算的课表
     */
    public static double cal_ConflictRatio(CourseIntervalSet<Course> set) {
        return cal_ConflictRatio((IntervalSet<?>) set) * (double) ((set.new_End() - set.new_Start())) / 70;
    }

    public static double cal_Conflict(CourseSchedule courseSchedule) {
        return IntervalSetUtils.cal_ConflictRatio(courseSchedule.get_interval());
    }
}
