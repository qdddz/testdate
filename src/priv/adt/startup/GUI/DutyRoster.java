package priv.adt.startup.GUI;

import priv.adt.exception.ThrowError;
import priv.adt.interval.DutyRoster.DutyIntervalSet;
import priv.adt.interval.DutyRoster.Employee;
import priv.adt.interval.Interval;
import priv.adt.startup.API.IntervalSetUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 排班表的图形化界面
 */
public class DutyRoster {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(DutyRoster::createGUI);
    }

    public static void createGUI() {

        JFrame frame = new JFrame("值班管理系统");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(200, 200, 600, 400);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));

        JPanel first_panel = new JPanel();
        first_panel.setLayout(new FlowLayout());
        first_panel.add(new JLabel("是否初始化？"));
        JButton button1 = new JButton("确认");
        button1.setContentAreaFilled(false);
        first_panel.add(button1);
        frame.add(first_panel);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("请输入已保存的文件路径："));
        JTextField secondField = new JTextField(2 * 16);
        panel.add(secondField);
        JButton button2 = new JButton("确认");
        button2.setContentAreaFilled(false);
        panel.add(button2);
        frame.add(panel);

        button1.addActionListener(e -> {
            date_DurationSet();
            frame.dispose();
        });

        button2.addActionListener(e -> {
            String path = secondField.getText();
            try {
                priv.adt.startup.API.DutyRoster duty_roster = new priv.adt.startup.API.DutyRoster(path);
                mainFrame(duty_roster);
                frame.dispose();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(frame, "文件路径错误！");
                secondField.setText("");
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(frame, "未成功初始化！");
            }
        });
    }


    //初始化日期面板
    public static void date_DurationSet() {
        JFrame frame = new JFrame("设置排班表系统的日期");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        String[] panel_Name = {"请输入起始日期/结束日期：", "起始日期 (yyyy-mm-dd)", "结束日期 (yyyy-mm-dd)"};
        List<JPanel> plist = new ArrayList<>();
        List<JTextField> text_List = new ArrayList<>();
        for (int i = 0; i < panel_Name.length; i++) {
            JPanel panel = new JPanel();
            plist.add(panel);
            panel.setLayout(new FlowLayout());
            panel.add(new JLabel(panel_Name[i]));
            if (i > 0) {
                JTextField new_Text = new JTextField(16);
                text_List.add(new_Text);
                panel.add(new_Text);
            }
            frame.add(panel);
        }

        //确认
        JButton enter = new JButton("确认");
        enter.setContentAreaFilled(false);
        frame.add(enter);

        enter.addActionListener((e -> {
            List<String> get_string = new ArrayList<>();
            for (int i = 0; i < panel_Name.length - 1; i++) {
                get_string.add(text_List.get(i).getText());
            }
            String[] date_start = get_string.get(0).split("-");
            String[] date_end = get_string.get(1).split("-");
            if (date_start.length > 3 || date_end.length > 3) {
                JOptionPane.showMessageDialog(frame, "输入的日期格式存在错误");
                text_List.get(0).setText("");
                text_List.get(1).setText("");
                return;
            }


            priv.adt.startup.API.DutyRoster duty_roster;
            try {
                duty_roster = new priv.adt.startup.API.DutyRoster(Integer.parseInt(date_start[0]), Integer.parseInt(date_start[1]), Integer.parseInt(date_start[2]), Integer.parseInt(date_end[0]), Integer.parseInt(date_end[1]), Integer.parseInt(date_end[2]));
                mainFrame(duty_roster);
                frame.dispose();
            } catch (ThrowError exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                for (JTextField text_field : text_List) {
                    text_field.setText("");
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                JOptionPane.showMessageDialog(frame, "输出的日期存在错误！");
                for (JTextField text_field : text_List) {
                    text_field.setText("");
                }
            }
        }));
    }

    //主要控制面板
    public static void mainFrame(priv.adt.startup.API.DutyRoster dutyRoster) {
        JFrame frame = new JFrame("主要控制面板");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        JButton button1 = new JButton("管理员工表");
        button1.setContentAreaFilled(false);
        frame.add(button1);
        button1.addActionListener((e) -> set_EmployeesSet(dutyRoster));

        JButton button2 = new JButton("安排时间段");
        button2.setContentAreaFilled(false);
        frame.add(button2);
        button2.addActionListener((e) -> arrange_employee(dutyRoster));

        JButton button3 = new JButton("查询排班表");
        button3.setContentAreaFilled(false);
        frame.add(button3);
        button3.addActionListener((e) -> check_DutyRoster(dutyRoster));
    }


    //设置员工列表
    public static void set_EmployeesSet(priv.adt.startup.API.DutyRoster dutyRoster) {
        JFrame frame = new JFrame("管理员工表");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        //添加员工
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        panel1.add(new JLabel("添加一位职员：(姓名-职位-电话)"));
        JTextField textField1 = new JTextField(16);
        panel1.add(textField1);
        JButton button1 = new JButton("添加");
        button1.setContentAreaFilled(false);
        panel1.add(button1);
        frame.add(panel1);

        //删除员工
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(new JLabel("删除一位职员：(姓名-职位-电话)"));
        JTextField textField2 = new JTextField(16);
        panel2.add(textField2);
        JButton button2 = new JButton("删除");
        button2.setContentAreaFilled(false);
        panel2.add(button2);
        panel2.add(new JLabel("\n删除的职员若已在排班表中，将移除排班表中的全部记录"));
        frame.add(panel2);

        //展示员工列表
        HashSet<Employee> employees_List = dutyRoster.get_Employee();
        StringBuilder employees_ListString = new StringBuilder();
        for (Employee employee : employees_List) {
            employees_ListString.append(employee.toString()).append("\n");
        }

        JTextArea textArea = new JTextArea(employees_ListString.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        button1.addActionListener(e -> {
            try {
                String text = textField1.getText();
                String[] employee_info = text.split("[-|\\s]");
                if (employee_info.length > 3) {
                    JOptionPane.showMessageDialog(frame, "输入的格式存在错误！");
                    textField1.setText("");
                    return;
                }
                Employee employee = new Employee(employee_info[0], employee_info[1], employee_info[2]);
                HashSet<Employee> employees = new HashSet<>();
                employees.add(employee);
                dutyRoster.add_Employee(employees);
                textField1.setText("");
                textArea.setEditable(true);
                textArea.append(employee + "\n");
                textArea.setEditable(false);
            } catch (ArrayIndexOutOfBoundsException exception) {
                JOptionPane.showMessageDialog(frame, "输入的格式存在错误！");
                textField1.setText("");
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                textField1.setText("");
            }
        });

        button2.addActionListener(e -> {
            try {
                String text = textField2.getText();
                String[] employee_info = text.split("[-|\\s]");
                if (employee_info.length > 3) {
                    JOptionPane.showMessageDialog(frame, "输入的格式存在错误！");
                    textField2.setText("");
                    return;
                }
                Employee employee = new Employee(employee_info[0], employee_info[1], employee_info[2]);
                HashSet<Employee> employees = new HashSet<>();
                employees.add(employee);
                dutyRoster.get_Dutyinterval().remove(employee);
                boolean res = dutyRoster.delete_Employee(employees);
                if (!res) {
                    JOptionPane.showMessageDialog(frame, "该职员并不在职员列表中");
                    textField2.setText("");
                } else {
                    textField2.setText("");
                    fresh_Duty(dutyRoster);
                    HashSet<Employee> list = dutyRoster.get_Employee();
                    StringBuilder strb = new StringBuilder();
                    for (Employee emp : list) {
                        strb.append(emp.toString()).append("\n");
                    }
                    textArea.setEditable(true);
                    textArea.setText(strb.toString());
                    textArea.setEditable(false);
                }
            } catch (ArrayIndexOutOfBoundsException exception) {
                JOptionPane.showMessageDialog(frame, "输入格式有误！");
                textField2.setText("");
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                textField2.setText("");
            }
        });
    }


    //安排员工值班时间
    public static void arrange_employee(priv.adt.startup.API.DutyRoster dutyRoster) {
        JFrame frame = new JFrame("安排职员值班时间");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        String[] panel_Name = {"请输入职员信息、值班起始日期/结束日期：",
                "职员信息(姓名-职位-电话)",
                "起始日期 (yyyy-mm-dd)",
                "结束日期 (yyyy-mm-dd)"};
        List<JPanel> panel_List = new ArrayList<>();
        List<JTextField> text_List = new ArrayList<>();
        for (int i = 0; i < panel_Name.length; i++) {
            JPanel new_panel = new JPanel();
            panel_List.add(new_panel);
            new_panel.setLayout(new FlowLayout());
            new_panel.add(new JLabel(panel_Name[i]));
            if (i > 0) {
                JTextField new_text = new JTextField(16);
                text_List.add(new_text);
                new_panel.add(new_text);
            }
            frame.add(new_panel);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        JButton random = new JButton("随机生成排班表");
        random.setContentAreaFilled(false);
        panel.add(random);
        JButton enter = new JButton("确认");
        enter.setContentAreaFilled(false);
        panel.add(enter);
        frame.add(panel);


        //随机生成排班表
        random.addActionListener(e -> {
            try {
                dutyRoster.random_Arrange();
                JOptionPane.showMessageDialog(frame, "成功以随机方式生成排班表！");
                frame.dispose();
                fresh_Duty(dutyRoster);
                check_DutyRoster(dutyRoster);
            } catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(frame, "请先至少添加一名职员后再操作！");
            }
        });

        //确认
        enter.addActionListener((e -> {
            List<String> got_String = new ArrayList<>();
            for (int i = 0; i < panel_Name.length - 1; i++) {
                got_String.add(text_List.get(i).getText());
            }
            String[] employee = got_String.get(0).split("[-|\\s]");
            String[] date_start = got_String.get(1).split("-");
            String[] date_end = got_String.get(2).split("-");
            if (employee.length > 3 || date_start.length > 3 || date_end.length > 3) {
                JOptionPane.showMessageDialog(frame, "输入的信息有误");
                if (employee.length != 3) {
                    text_List.get(0).setText("");
                }
                if (date_start.length != 3) {
                    text_List.get(0).setText("");
                }
                if (date_end.length != 3) {
                    text_List.get(0).setText("");
                }
            }

            try {
                dutyRoster.arrangeEmployee(new Employee(employee[0], employee[1], employee[2]), Integer.parseInt(date_start[0]), Integer.parseInt(date_start[1]), Integer.parseInt(date_start[2]), Integer.parseInt(date_end[0]), Integer.parseInt(date_end[1]), Integer.parseInt(date_end[2]));
                JOptionPane.showMessageDialog(frame, "职员表安排成功！");
                frame.dispose();
                fresh_Duty(dutyRoster);
            } catch (ThrowError exception) {
                JOptionPane.showMessageDialog(frame, "在列表中查找不到该名职员！");
                text_List.get(0).setText("");
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, "输入有误，或该员工已有别的工作！");
                for (int i = 0; i < panel_Name.length - 1; i++) {
                    text_List.get(i).setText("");
                }
            }

        }));
    }


    //打印当前排班表信息
    public static void check_DutyRoster(priv.adt.startup.API.DutyRoster dutyRoster) {
        JFrame frame = fresh_Duty(dutyRoster);
        frame.setVisible(true);
    }

    private static JFrame fresh_Duty(priv.adt.startup.API.DutyRoster dutyRoster) {
        JFrame frame = new JFrame("排班表");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        String[] column_Name = {"日期", "姓名", "职位", "电话号码"};
        String[][] row_data = new String[(int) dutyRoster.get_Duration() + 1][4];
        for (int i = 0; i < row_data.length; i++) {
            row_data[i][0] = String.valueOf(dutyRoster.get_DateStart().plusDays(i));
        }
        DutyIntervalSet<Employee> duty_intervalSet = dutyRoster.get_Dutyinterval();
        duty_intervalSet.sort();
        ArrayList<Interval<Employee>> intervalSet = duty_intervalSet.get_intervalSet();
        for (Interval<Employee> interval : intervalSet) {
            long start = interval.get_start();
            long end = interval.get_end();
            for (int i = (int) start; i <= (int) end; i++) {
                row_data[i][1] = interval.get_label().get_name();
                row_data[i][2] = interval.get_label().get_position();
                row_data[i][3] = interval.get_label().get_phone();
            }
        }
        JTable table = new JTable(row_data, column_Name);
        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        frame.add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel();
        if (IntervalSetUtils.cal_FreeTimeRatio(dutyRoster.get_Dutyinterval()) < 0.00001) {
            label.setText("空闲时间的比例：" + IntervalSetUtils.cal_FreeTimeRatio(dutyRoster.get_Dutyinterval()));
        } else {
            label.setText("存在空闲段未安排，空闲时间的比例为：" + IntervalSetUtils.cal_FreeTimeRatio(dutyRoster.get_Dutyinterval()));
        }
        label.setPreferredSize(new Dimension(0, 50));
        frame.add(label, BorderLayout.AFTER_LAST_LINE);
        frame.setSize(1000, 800);
        return frame;
    }

    /**
     * 排班系统启动主函数
     */
}
