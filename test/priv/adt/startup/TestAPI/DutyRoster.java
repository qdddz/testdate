package priv.adt.startup.TestAPI;

import org.junit.Test;
import priv.adt.interval.DutyRoster.Employee;

import java.io.File;
import java.util.HashSet;

/**
 * DutyRosterApp的测试类
 */
public class DutyRoster {

    /*
    测试策略：
    1.添加和删除
    */
    @Test
    public void add_delete_test() {
        priv.adt.startup.API.DutyRoster duty_Roster = new priv.adt.startup.API.DutyRoster(2021, 7, 1, 2021, 7, 30);
        HashSet<Employee> sum_employee = new HashSet<>();
        Employee employee_a = new Employee("DVA", "A", "888456");
        Employee employee_b = new Employee("VIVY", "B", "1919810");
        Employee employee_c = new Employee("GRACE", "C", "114514");
        sum_employee.add(employee_a);
        sum_employee.add(employee_b);
        sum_employee.add(employee_c);
        duty_Roster.add_Employee(sum_employee);
        HashSet<Employee> ano_employees = new HashSet<>();
        ano_employees.add(employee_a);
        duty_Roster.delete_Employee(ano_employees);
    }

    /*
    测试策略：
    1.随机安排
    */
    @Test
    public void randomArrange() {
        priv.adt.startup.API.DutyRoster duty_Roster = new priv.adt.startup.API.DutyRoster(2021, 7, 1, 2021, 7, 30);
        HashSet<Employee> sum_employee = new HashSet<>();
        Employee employee_a = new Employee("DVA", "A", "888456");
        Employee employee_b = new Employee("VIVY", "B", "1919810");
        Employee employee_c = new Employee("GRACE", "C", "114514");
        sum_employee.add(employee_a);
        sum_employee.add(employee_b);
        sum_employee.add(employee_c);
        duty_Roster.add_Employee(sum_employee);

        duty_Roster.displayEmployees();
        duty_Roster.random_Arrange();
    }

    /*
    测试策略：
    1.值班表满
    2.删除两名职员
    3.删除全部职员
     */
    @Test
    public void arrange_Employee() {
        priv.adt.startup.API.DutyRoster duty_Roster = new priv.adt.startup.API.DutyRoster(2021, 7, 1, 2021, 7, 30);
        HashSet<Employee> sum_employee = new HashSet<>();
        Employee employee_a = new Employee("DVA", "A", "888456");
        Employee employee_b = new Employee("VIVY", "B", "1919810");
        Employee employee_c = new Employee("GRACE", "C", "114514");
        sum_employee.add(employee_a);
        sum_employee.add(employee_b);
        sum_employee.add(employee_c);
        duty_Roster.add_Employee(sum_employee);

        duty_Roster.arrangeEmployee(employee_a, 2021, 7, 1, 2021, 7, 12);
        duty_Roster.arrangeEmployee(employee_b, 2021, 7, 13, 2021, 7, 22);
        duty_Roster.arrangeEmployee(employee_c, 2021, 7, 23, 2021, 7, 30);
        duty_Roster.printf_Roster();

        HashSet<Employee> ano_employees = new HashSet<>();
        ano_employees.add(employee_a);
        ano_employees.add(employee_b);
        duty_Roster.delete_Employee(ano_employees);
        duty_Roster.printf_Roster();

        ano_employees.add(employee_c);
        duty_Roster.delete_Employee(ano_employees);
        duty_Roster.printf_Roster();
    }



    @Test
    public void Regular_Expression() {
        int i = 1;
        while (i <= 8) {
            priv.adt.startup.API.DutyRoster duty_Roster = null;
            try {
                duty_Roster = new priv.adt.startup.API.DutyRoster("test" + File.separator + "txt" + File.separator + "test" + i + ".txt");
                //test\txt\test1.txt
                duty_Roster.get_Dutyinterval().check();
            } catch (Exception e) {
                i++;
                continue;
            }
            duty_Roster.displayEmployees();
            duty_Roster.printf_Roster();
            i++;
        }
    }
}
