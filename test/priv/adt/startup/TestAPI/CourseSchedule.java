package priv.adt.startup.TestAPI;

import org.junit.Test;
import priv.adt.interval.CourseSchedule.Course;

import java.util.HashSet;

/**
 * 测试课表系统是否正常
 */
public class CourseSchedule {

    @Test
    public void add_Course() {
        priv.adt.startup.API.CourseSchedule course_schedule = new priv.adt.startup.API.CourseSchedule(2021, 6, 7, 2);
        HashSet<Course> courses = new HashSet<>();
        Course class_A = new Course("501", "A", "DVA", "11", 3);
        Course class_B = new Course("502", "B", "VIVY", "12", 5);
        Course class_C = new Course("503", "C", "GRACE", "13", 4);
        courses.add(class_A);
        courses.add(class_B);
        courses.add(class_C);
        course_schedule.add_course(courses);
    }

    @Test
    public void arrange_Course() {
        priv.adt.startup.API.CourseSchedule course_Schedule = new priv.adt.startup.API.CourseSchedule(2021, 6, 7, 2);
        HashSet<Course> coursesSet = new HashSet<>();
        Course class_A = new Course("501", "A", "DVA", "11", 8);
        Course class_B = new Course("502", "B", "VIVY", "12", 8);
        Course class_C = new Course("503", "C", "GRACE", "13", 6);
        coursesSet.add(class_A);
        coursesSet.add(class_B);
        coursesSet.add(class_C);

        course_Schedule.add_course(coursesSet);

        course_Schedule.arrange_Course(class_A, 1, 8);
        course_Schedule.arrange_Course(class_B, 2, 8);
        course_Schedule.arrange_Course(class_C, 3, 8);
        course_Schedule.arrange_Course(class_C, 3, 19);
        course_Schedule.arrange_Course(class_C, 4, 10);


        course_Schedule.print_Course();
        course_Schedule.printf_thatday(2021, 6, 16);
    }
}
