package priv.adt.startup.API;

import priv.adt.exception.ThrowError;
import priv.adt.interval.DutyRoster.DutyIntervalSet;
import priv.adt.interval.DutyRoster.Employee;
import priv.adt.interval.Interval;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 排班系统
 */
public class DutyRoster {

    private static int MAX_DUTY_DAY_RANDOM; //防止随机时某人被随机安排的时间过长
    private final LocalDate date_Start;
    private final LocalDate date_End;
    private final long duration;
    private final HashSet<Employee> employee_List;
    private final DutyIntervalSet<Employee> duty_interval;

    /*
    AF：(date_Start) = 起始日期起点
    (date_End) = 结束日期
    (duration）= 持续的日期
    (employees_List) = 添加过的全部员工
    (duty_interval) = 排班表

    RI：
    所有信息均需要合法，例如（起始日期，结束日期）

    safe from RE：
    用private修饰
     */


    public DutyRoster(int year_Start, int month_Start, int day_Start, int year_End, int month_End, int day_End) {
        try {
            employee_List = new HashSet<>();
            date_Start = LocalDate.of(year_Start, month_Start, day_Start);
            date_End = LocalDate.of(year_End, month_End, day_End);
            duration = date_End.toEpochDay() - date_Start.toEpochDay();
            long MAX_DURATION = 365;
            if (duration > MAX_DURATION || duration <= 0)
                throw new ThrowError("输入的日期存在错误！");
            duty_interval = new DutyIntervalSet<>(0, duration);
        } catch (DateTimeException e) {
            throw new ThrowError("输入的日期存在错误！");
        }
    }

    /**
     * 通过正则表达式的形式初始化一个排班系统信息
     *
     * @param filename 文件名
     */
    public DutyRoster(String filename) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename));
            StringBuilder stringBuilder = new StringBuilder();
            String s1;
            while ((s1 = bufferedReader.readLine()) != null)
                stringBuilder.append(s1);
            String string_info = stringBuilder.toString();//文件中所有信息获取

            //员工列表
            employee_List = new HashSet<>();
            Pattern pattern = Pattern.compile("([A-Z][a-z]+[A-Z][a-z]+)\\{([A-Z][A-Za-z\\s]+),(1(?:3\\d|4[4-9]|5[0-35-9]|6[67]|7[013-8]|8\\d|9\\d)-\\d{4}-\\d{4})}");
            Matcher matcher = pattern.matcher(string_info);
            while (matcher.find()) {
                StringBuilder phone = new StringBuilder();
                String[] phone_info = matcher.group(3).split("-");
                String name = matcher.group(1);
                String position = matcher.group(2);

                for (String s : phone_info)
                    phone.append(s);
                employee_List.add(new Employee(name, position, phone.toString()));
            }

            //时间段
            Pattern pattern_duration = Pattern.compile("Period\\{(\\d{4})-(\\d{2})-(\\d{2}),(\\d{4})-(\\d{2})-(\\d{2})}");
            Matcher matcher_duration = pattern_duration.matcher(string_info);
            if (!matcher_duration.find())
                throw new RuntimeException("正则表达式输入错误！");
            String day_End = matcher_duration.group(6);
            String month_End = matcher_duration.group(5);
            String year_End = matcher_duration.group(4);
            String year_Start = matcher_duration.group(1);
            String month_Start = matcher_duration.group(2);
            String day_Start = matcher_duration.group(3);

            date_Start = LocalDate.of(Integer.parseInt(year_Start), Integer.parseInt(month_Start), Integer.parseInt(day_Start));
            date_End = LocalDate.of(Integer.parseInt(year_End), Integer.parseInt(month_End), Integer.parseInt(day_End));
            duration = date_End.toEpochDay() - date_Start.toEpochDay();
            duty_interval = new DutyIntervalSet<>(0, duration);

            if (duration <= 0) {
                throw new ThrowError("输入日期错误！");
            }
            //时间段安排
            Pattern pattern_Arrange = Pattern.compile("([A-Z][a-z]+[A-Z][a-z]+)\\{(\\d{4})-(\\d{2})-(\\d{2}),(\\d{4})-(\\d{2})-(\\d{2})}");
            Matcher matcher_Arrange = pattern_Arrange.matcher(string_info);
            while (matcher_Arrange.find()) {
                String name = matcher_Arrange.group(1);
                Employee employee = null;
                for (Employee a : employee_List)
                    if (a.get_name().equals(name)) {
                        employee = a;
                        break;
                    }
                if (employee == null)
                    throw new ThrowError("被员工并不在列表内！");
                day_End = matcher_Arrange.group(7);
                month_End = matcher_Arrange.group(6);
                year_Start = matcher_Arrange.group(2);
                month_Start = matcher_Arrange.group(3);
                day_Start = matcher_Arrange.group(4);
                year_End = matcher_Arrange.group(5);
                arrangeEmployee(employee, Integer.parseInt(year_Start), Integer.parseInt(month_Start), Integer.parseInt(day_Start), Integer.parseInt(year_End), Integer.parseInt(month_End), Integer.parseInt(day_End));
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    /**
     * 向成员列表中添加一组指定员工
     *
     * @param employeeSet 待添加员工组
     * @return boolean 是否成功添加
     */
    public boolean add_Employee(Set<Employee> employeeSet) {
        boolean what = employee_List.addAll(employeeSet);
        checkRep();
        return what;
    }

    /**
     * 在成员列表中删除一组指定员工
     *
     * @param employeeSet 待删除员工组
     * @return boolean 是否成功删除
     */
    public boolean delete_Employee(Set<Employee> employeeSet) {
        for (Employee a : employeeSet)
            duty_interval.remove(a);
        boolean what = employee_List.removeAll(employeeSet);
        checkRep();
        return what;
    }

    /**
     * 手工选择某个员工，安排其进入排班表的某个时间段
     *
     * @param employee   被安排的员工
     * @param yearStart  起始年
     * @param monthStart 起始月
     * @param dayStart   起始日
     * @param yearEnd    结束年
     * @param monthEnd   结束月
     * @param dayEnd     结束日
     */
    public void arrangeEmployee(Employee employee, int yearStart, int monthStart, int dayStart, int yearEnd, int monthEnd, int dayEnd) {
        if (!employee_List.contains(employee))
            throw new ThrowError("该名职员并不在职员列表中！");
        int new_Start;
        int new_End;
        LocalDate start = LocalDate.of(yearStart, monthStart, dayStart);
        LocalDate end = LocalDate.of(yearEnd, monthEnd, dayEnd);
        new_Start = (int) (start.toEpochDay() - date_Start.toEpochDay());
        new_End = (int) (end.toEpochDay() - date_Start.toEpochDay());
        duty_interval.insert(new_Start, new_End, employee);
    }

    /**
     * 形成一张随机的排班表
     */
    public void random_Arrange() {
        refresh();
        HashSet<Employee> random_arranged = new HashSet<>();
        long rest = duration;
        long now = 0;
        ArrayList<Employee> emp = new ArrayList<>(employee_List);

        Random random = new Random();
        while (rest >= 1) {
            if (random_arranged.size() == employee_List.size() - 1) {
                Employee the_Last = null;
                for (Employee ite : employee_List)
                    if (!random_arranged.contains(ite)) {
                        the_Last = ite;
                        break;
                    }
                duty_interval.insert(now, now + rest, the_Last);
                break;
            }

            int rand_Days = random.nextInt((int) rest) + 1;
            adjust_Random();
            rand_Days = Math.min(rand_Days, MAX_DUTY_DAY_RANDOM);
            int randEmp = random.nextInt(employee_List.size());
            Employee employee = emp.get(randEmp);

            if (!random_arranged.contains(employee)) {
                random_arranged.add(employee);
                duty_interval.insert(now, now + rand_Days, employee);
                rest -= rand_Days;
                now += rand_Days;
            }
        }
        printf_Roster();
    }

    /**
     * 返回内部的员工列表
     *
     * @return 内部的员工列表，用于打印
     */
    public HashSet<Employee> get_Employee() {
        return employee_List;
    }

    public long get_Duration() {
        return duration;
    }

    private void adjust_Random() {
        MAX_DUTY_DAY_RANDOM = (int) (duration / employee_List.size() * 2);
    }

    public LocalDate get_DateStart() {
        return date_Start;
    }

    public LocalDate get_DateEnd() {
        return date_End;
    }

    /**
     * 排班表打印:
     * <p>1.是否所有时间段都安排满了
     * <p>2.未满时间段高亮
     * <p>3.未安排时间段的空闲比例
     */
    public void printf_Roster() {
        System.out.println("                 排班表                    ");
        System.out.println("起始日期：" + date_Start);
        System.out.println("终止日期：" + date_End);
        System.out.println("值班日期 值班人员姓名 职位 手机号码");
        duty_interval.sort();
        ArrayList<Interval<Employee>> new_interval = duty_interval.get_intervalSet();
        for (Interval<Employee> a : new_interval) {
            long start = a.get_start();
            long end = a.get_end();
            for (int i = (int) start; i <= (int) end; i++)
                System.out.println(date_Start.plusDays(i) + " " + a.get_label());
        }
        System.out.println("空闲时间的比例：" + IntervalSetUtils.cal_FreeTimeRatio(duty_interval));
    }

    /**
     * 员工表打印
     */
    public void displayEmployees() {
        System.out.println("                员工表                 ");
        System.out.println("职员总人数：" + employee_List.size());
        System.out.println("职员姓名 职位 手机号码");

        for (Employee a : employee_List)
            System.out.println(a);
    }

    /*
    刷新操作：
    清空排班表，但是不修改开始、结束日期
     */
    private void refresh() {
        Set<Employee> sum_label = duty_interval.labels();
        for (Employee a : sum_label) {
            duty_interval.remove(a);
        }
    }

    /**
     * 返回内部DutyIntervalSet
     *
     * @return 内部的dutyIntervalSet，用于检查排班表是否排满
     */
    public DutyIntervalSet<Employee> get_Dutyinterval() {
        return duty_interval;
    }


    private void checkRep() {
        //只有对employeesList的操作需要check，对dutyIntervalSet在其父类中进行过check
        for (Employee a : employee_List) {
            if (a.get_name() == null || a.get_phone() == null || a.get_position() == null)
                throw new RuntimeException("插入/删除的员工信息有误！");
        }
    }

}
