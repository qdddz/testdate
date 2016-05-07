package priv.adt.interval.CourseSchedule;

/**
 * 表示一个课程，及其对应的时间段
 */
public class Course {

    private final int max_course;
    private final String id;
    private final String name;
    private final String teacher;
    private final String location;

    public Course(String id, String name, String teacher, String location, int max_course) {
        this.max_course = max_course;
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.location = location;
    }

    public String get_teacher() {
        return teacher;
    }

    public String get_location() {
        return location;
    }

    public String get_id() {
        return id;
    }

    public String get_name() {
        return name;
    }

    public int get_Max() {
        return max_course;
    }

    @Override
    public String toString() {
        return "课程ID: " + id + '\t' +
                "课程名: " + name + '\t' +
                "教师姓名: " + teacher + '\t' +
                "教学地点: " + location + '\t' +
                "周学时数: " + max_course;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        else {
            Course course = (Course) obj;
            return id.equals(course.id);
        }
    }

    @Override
    public int hashCode() {
        int hash = Integer.valueOf(0);
        char[] idArray = id.toCharArray();
        for (char c : idArray) {
            hash = hash * 31 + c;
        }
        return hash;
    }
}
