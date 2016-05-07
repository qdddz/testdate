package priv.adt.startup.API;

import priv.adt.interval.CourseSchedule.Course;
import priv.adt.interval.CourseSchedule.CourseIntervalSet;
import priv.adt.interval.Interval;

import java.time.LocalDate;
import java.util.*;

/**
 * 排课程序
 */
public class CourseSchedule {

    private final LocalDate date_start;
    private final int week;
    private final CourseIntervalSet<Course> course_interval;
    private final HashMap<Course, Integer> course_list;
    private final ArrayList<Map.Entry<Course, Integer>> index;

    /*
    AF:(dateStart) = 起始日期
    (week) = 持续周数
    (courseIntervalSet) = 课程表
    (courseList) = 待添加课程

    RI:(weeks)=偶数
    (dateStart)=周一
    待添加课程合法

    safe from RE:
    用private修饰
    */

    public CourseSchedule(int year_Start, int month_Start, int day_Start, int week) {
        date_start = LocalDate.of(year_Start, month_Start, day_Start);
        if (date_start.getDayOfWeek().getValue() != 1) {
            throw new RuntimeException("输入的日期只能是周一！");
        }
        this.week = week;
        course_interval = new CourseIntervalSet<>(0, 168);
        course_list = new HashMap<>();
        index = new ArrayList<>();
    }

    /**
     * 添加一组课程到现有课表中
     *
     * @param courseSet 待添加课程
     */
    public void add_course(Set<Course> courseSet) {
        for (Course mew_course : courseSet) {
            if (course_list.containsKey(mew_course)) {
                throw new RuntimeException("该课程已添加，操作失败！");
            }
            course_list.put(mew_course, mew_course.get_Max());
        }
    }

    public CourseIntervalSet<Course> get_interval() {
        return course_interval;
    }

    public HashMap<Course, Integer> get_list() {
        return course_list;
    }

    public ArrayList<Map.Entry<Course, Integer>> getIndex() {
        return index;
    }

    //星期
    private int indexToDay(long index) {
        return (int) ((index / 24) + 1);
    }

    //小时
    private int indexToHour(long index) {
        return (int) (index - (index / 24) * 24);
    }

    public LocalDate get_Datestart() {
        return date_start;
    }

    public int get_Week() {
        return week;
    }


    /**
     * 将待添加课程中的一门指定课程安排到指定时间
     * <p>注意：一天一共有五个时间段供选择，分别是：
     * <p>8-10,10-12,13-15,15-17,19-21
     *
     * @param course 指定课程
     * @param day    指定是星期几，只能在1-7中选择
     * @param start  指定时间段 ，只能在8、10、13、15、19中选择
     */
    public void arrange_Course(Course course, int day, int start) {

        if (!course_list.containsKey(course))
            throw new RuntimeException("请检查输入，是否错误或者该课程已达到上限！");
        else if (day < 1 || day > 7 || !(Arrays.asList(8, 10, 13, 15, 19).contains(start)))
            throw new RuntimeException("时间输入错误！");


        //确定索引

        //添加
        course_interval.insert(24 * (day - 1) + start, 24 * (day - 1) + start + 2, course);
        index.add(new Map.Entry<>() {
            @Override
            public Course getKey() {
                return course;
            }

            @Override
            public Integer setValue(Integer value) {
                return 0;
            }

            @Override
            public Integer getValue() {
                return 24 * (day - 1) + start;
            }
        });

        //对应可安排课时数减少
        course_list.put(course, course_list.get(course) - 2);

        if (course_list.get(course) <= 0) {
            course_list.remove(course);
        }
    }

    /**
     * 打印指定日期的课程
     *
     * @param year  指定年
     * @param month 指定月
     * @param day   指定日
     */
    public StringBuilder printf_thatday(int year, int month, int day) {
        LocalDate that_day = LocalDate.of(year, month, day);
        StringBuilder stringBuilder = new StringBuilder();
        int i = (int) (that_day.toEpochDay() - date_start.toEpochDay());
        if (i > week * 7)
            throw new RuntimeException("输入日期有误！");
        int Days = (++i) % 7;
        stringBuilder.append("日期：").append(that_day).append("\n");
        stringBuilder.append("课程名 教师姓名 上课地点 星期 上课时间 下课时间" + "\n");
        System.out.println("               该日期课程情况                     ");
        System.out.println("日期：" + that_day);
        System.out.println("课程名 教师姓名 上课地点 星期 上课时间 下课时间");
        course_interval.sort();
        ArrayList<Interval<Course>> new_interval = course_interval.get_intervalSet();
        for (Interval<Course> course : new_interval) {
            if (indexToDay(course.get_start()) == Days) {
                System.out.println(course.get_label().get_name() + " " + course.get_label().get_teacher() + " " +
                        course.get_label().get_location() + " " + indexToDay(course.get_start()) + " " +
                        indexToHour(course.get_start()) + " " + indexToHour(course.get_end()));
                stringBuilder.append(course.get_label().get_name()).append(" ").append(course.get_label().get_teacher()).append(" ").append(course.get_label().get_location()).append(" ").append(indexToDay(course.get_start())).append(" ").append(indexToHour(course.get_start())).append(" ").append(indexToHour(course.get_end())).append("\n");
            }
        }
        return stringBuilder;
    }

    /**
     * 打印课程表
     */
    public void print_Course() {
        ArrayList<Interval<Course>> new_interval = course_interval.get_intervalSet();
        System.out.println("               本学期课程表            ");
        System.out.println("课程名 教师姓名 上课地点 星期 上课时间 下课时间");
        course_interval.sort();
        for (Interval<Course> a : new_interval)
            System.out.println(a.get_label().get_name() + " " + a.get_label().get_teacher() + " " +
                    a.get_label().get_location() + " " + indexToDay(a.get_start()) + " " +
                    indexToHour(a.get_start()) + " " + indexToHour(a.get_end()));
        System.out.println("冲突时间的比例：" + IntervalSetUtils.cal_ConflictRatio(this.course_interval));
        System.out.println("空闲时间的比例：" + IntervalSetUtils.cal_FreeTimeRatio(this.course_interval));
    }
}
