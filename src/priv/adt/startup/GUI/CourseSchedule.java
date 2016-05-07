package priv.adt.startup.GUI;

import priv.adt.interval.CourseSchedule.Course;
import priv.adt.startup.API.IntervalSetUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

//课程管理的图形化界面

public class CourseSchedule {
    private static String addedFrameInfo = "";

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(CourseSchedule::dateDurationSet);
    }

    /**
     * 首先进入初始面板
     * 设置日期，教学周数
     * <p>初始化成功，便不返回
     */
    public static void dateDurationSet() {
        String[] panel_Name = {"欢迎使用课程排课系统", "请输入起始日期，开始必须是周一 (yyyy-mm-dd)：", "请输入教学周总数："};
        JFrame frame = new JFrame("课程排课系统");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(200, 200, 600, 400);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));

        java.util.List<JPanel> panel_List = new ArrayList<>();
        List<JTextField> textList = new ArrayList<>();
        for (int i = 0; i < panel_Name.length; i++) {
            JPanel new_panel = new JPanel();
            new_panel.setLayout(new FlowLayout());
            panel_List.add(new_panel);
            new_panel.add(new JLabel(panel_Name[i]));
            if (i > 0) {
                JTextField new_text = new JTextField(16);
                textList.add(new_text);
                new_panel.add(new_text);
            }
            frame.add(new_panel);
            frame.add(new JLabel("                                                    "));
        }

        frame.add(new JLabel("                                                                            "));
        JButton enter = new JButton("确认");
        enter.setPreferredSize(new Dimension(100, 30));
        enter.setContentAreaFilled(false);
        frame.add(enter);

        //检测输入是否有错
        enter.addActionListener(e -> {
            String date_start_text = textList.get(0).getText();
            String[] date_start_info = date_start_text.split("-");
            String week_Info = textList.get(1).getText();
            if (Integer.parseInt(week_Info) <= 0) {
                JOptionPane.showMessageDialog(frame, "教学周数应大于0！");
                textList.get(1).setText("");
                return;
            }
            if (date_start_info.length != 3) {
                JOptionPane.showMessageDialog(frame, "日期格式错误！");
                textList.get(0).setText("");
                return;
            }
            if (Integer.parseInt(week_Info) > 25) {
                JOptionPane.showMessageDialog(frame, "教学周数过长！");
                textList.get(1).setText("");
                return;
            }

            priv.adt.startup.API.CourseSchedule course_schedule;
            try {
                course_schedule = new priv.adt.startup.API.CourseSchedule(Integer.parseInt(date_start_info[0]), Integer.parseInt(date_start_info[1]), Integer.parseInt(date_start_info[2]), Integer.parseInt(week_Info));
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(frame, "日期输入有误！");
                textList.get(0).setText("");
                return;
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                textList.get(0).setText("");
                return;
            }
            mainFrame(course_schedule);
            frame.dispose();
        });
    }

    //  课程管理系统主要控制面板

    public static void mainFrame(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = new JFrame("主控面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200, 200, 600, 400);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));

        JButton add_course = new JButton("添加课程/查询可安排课程");
        add_course.setContentAreaFilled(false);
        frame.add(add_course);
        add_course.addActionListener(e -> add_Course(courseSchedule));

        JButton set_course = new JButton("安排新课程");
        set_course.setContentAreaFilled(false);
        frame.add(set_course);
        set_course.addActionListener(e -> setCourse(courseSchedule));

        JButton check_course = new JButton("查询全部课程表");
        check_course.setContentAreaFilled(false);
        frame.add(check_course);
        check_course.addActionListener(e -> check_whole(courseSchedule));

        JButton specificday_courses = new JButton("查询指定时间课程");
        specificday_courses.setContentAreaFilled(false);
        frame.add(specificday_courses);
        specificday_courses.addActionListener(e -> Scan_specific(courseSchedule));
    }

    //课程添加

    public static void add_Course(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = new JFrame("课程添加及可安排课程查询");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        String[] panel_name = {
                "等待添加的课程ID：",
                "待待添加的课程名称",
                "课程教师的姓名：",
                "课程的教学地点：",
                "周学时数"};
        java.util.List<JPanel> panel_list = new ArrayList<>();
        List<JTextField> text_list = new ArrayList<>();

        for (String s : panel_name) {
            JPanel new_panel = new JPanel();
            panel_list.add(new_panel);
            new_panel.setLayout(new FlowLayout());
            new_panel.add(new JLabel(s));
            JTextField new_text = new JTextField(16);
            text_list.add(new_text);
            new_panel.add(new_text);
            frame.add(new_panel);
        }

        JButton enter = new JButton("确认");
        enter.setContentAreaFilled(false);
        panel_list.get(4).add(enter);

        //展示课程列表
        Set<Course> course_list = courseSchedule.get_list().keySet();
        StringBuilder course_list_string = new StringBuilder();
        for (Course course : course_list) {
            course_list_string.append(course.toString()).append("\n");
        }
        JTextArea courses_text = new JTextArea(course_list_string.toString());
        Set<Map.Entry<Course, Integer>> courses_infoSet = courseSchedule.get_list().entrySet();
        StringBuilder courses_info = new StringBuilder();
        for (Map.Entry<Course, Integer> entry : courses_infoSet) {
            courses_info.append(entry.getKey().toString()).append("\t").append("剩下的可安排课时数量：").append(entry.getValue()).append("\n");
        }
        courses_text.setText(courses_info.toString());
        courses_text.setText("".equals(addedFrameInfo) ? "当前可安排的课程：\n无" : addedFrameInfo);
        courses_text.setEditable(false);
        JScrollPane scroll_pane = new JScrollPane(courses_text);
        frame.add(scroll_pane);

        enter.addActionListener(e -> {
            List<String> get_string = new ArrayList<>();
            int course_MaxClass;
            String course_ID;
            String course_Location;
            String course_name;
            String course_TeacherName;

            for (int i = 0; i < panel_name.length; i++) {
                get_string.add(text_list.get(i).getText());
            }
            try {
                course_ID = get_string.get(0);
                course_name = get_string.get(1);
                course_TeacherName = get_string.get(2);
                course_Location = get_string.get(3);
                course_MaxClass = Integer.parseInt(get_string.get(4));
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, "所输入课程信息错误！");
                for (JTextField text_field : text_list) {
                    text_field.setText("");
                }
                return;
            }

            if (course_ID == null || course_name == null || course_TeacherName == null || course_Location == null) {
                JOptionPane.showMessageDialog(frame, "所输入课程信息错误！");
                for (JTextField text_field : text_list) {
                    text_field.setText("");
                }
                return;
            }
            if (course_MaxClass < 0 || (course_MaxClass % 2) != 0) {
                JOptionPane.showMessageDialog(frame, "所输入周学时数存在错误！");
                text_list.get(4).setText("");
                return;
            }

            Course course = new Course(course_ID, course_name, course_TeacherName, course_Location, course_MaxClass);
            HashSet<Course> courses = new HashSet<>();
            courses.add(course);
            try {
                courseSchedule.add_course(courses);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                text_list.get(4).setText("");
                return;
            }
            for (int i = 0; i < panel_name.length; i++) {
                text_list.get(i).setText("");
            }

            courses_text.setEditable(true);

            Set<Map.Entry<Course, Integer>> courses_infoSetAdded = courseSchedule.get_list().entrySet();
            StringBuilder courses_infoAdded = new StringBuilder();
            courses_infoAdded.append("当前可安排的课程：\n");
            for (Map.Entry<Course, Integer> entry : courses_infoSetAdded) {
                courses_infoAdded.append(entry.getKey().toString()).append("\t").append("剩余可安排课时数：").append(entry.getValue()).append("\n");
            }
            courses_text.setText(courses_infoAdded.toString());
            addedFrameInfo = courses_infoAdded.toString();
            courses_text.setEditable(false);
        });
    }

    //课程安排
    public static void setCourse(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = new JFrame("添加课程");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        String[] panel_name = {
                "等待设置课程ID：",
                "添加到星期几?（从星期一到星期日中选择）",
                "添加到哪个时间段？(从8、10、13、15、19中选择其一)"};
        java.util.List<JPanel> panel_list = new ArrayList<>();
        List<JTextField> text_list = new ArrayList<>();

        for (String s : panel_name) {
            JPanel new_panel = new JPanel();
            panel_list.add(new_panel);
            new_panel.setLayout(new FlowLayout());
            new_panel.add(new JLabel(s));
            JTextField newText = new JTextField(16);
            text_list.add(newText);
            new_panel.add(newText);
            frame.add(new_panel);
        }

        JButton enter = new JButton("确认");
        enter.setContentAreaFilled(false);
        frame.add(enter);

        enter.addActionListener(e -> {
            List<String> got_string = new ArrayList<>();
            for (int i = 0; i < panel_name.length; i++) {
                got_string.add(text_list.get(i).getText());
            }

            String course_ID = got_string.get(0);
            int day = Integer.parseInt(got_string.get(1));
            int start_time = Integer.parseInt(got_string.get(2));
            Course chosen_course = null;
            for (Course course : courseSchedule.get_list().keySet()) {
                if (course.get_id().equals(course_ID)) {
                    chosen_course = course;
                }
            }

            try {
                courseSchedule.arrange_Course(chosen_course, day, start_time);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(frame, exception.getMessage());
                for (JTextField text_field : text_list) {
                    text_field.setText("");
                }
                return;
            }

            //更新添加页面
            Set<Map.Entry<Course, Integer>> courses_infoSetAdded = courseSchedule.get_list().entrySet();
            StringBuilder courses_infoAdded = new StringBuilder();
            courses_infoAdded.append("当前能够安排的课程如下：\n");
            for (Map.Entry<Course, Integer> entry : courses_infoSetAdded) {
                courses_infoAdded.append(entry.getKey().toString()).append("\t").append("剩下可以的安排课时数：").append(entry.getValue()).append("\n");
            }
            addedFrameInfo = courses_infoAdded.toString();
            if (courses_infoSetAdded.size() == 0) {
                addedFrameInfo = "目前能够安排的课程：\n无";
            }

            JOptionPane.showMessageDialog(frame, "成功安排课程");
            for (int i = 0; i < panel_name.length; i++) {
                text_list.get(i).setText("");
            }
            frame.dispose();
            refresh_Course(courseSchedule);
        });
    }


    //查询整体课表
    public static void check_whole(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = refresh_Course(courseSchedule);
        frame.setVisible(true);
    }


    //查询指定日期
    public static void Scan_specific(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = new JFrame("查询用户指定日期的课程");
        frame.add(new JLabel("请输入指定日期：(yyyy-mm-dd)", SwingConstants.CENTER));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JTextField specific_date = new JTextField(16);
        panel.add(specific_date);
        JButton enter = new JButton("确认");
        enter.setContentAreaFilled(false);
        panel.add(enter);
        frame.add(panel);

        JTextArea result_text = new JTextArea("");
        result_text.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(result_text);
        frame.add(scrollPane);

        enter.addActionListener(e -> {
            String Scan_specific = specific_date.getText();
            String[] specific_dateInfo = Scan_specific.split("-");
            if (specific_dateInfo.length != 3) {
                JOptionPane.showMessageDialog(frame, "所输入日期格式错误！");
                specific_date.setText("");
                return;
            }
            LocalDate local_date;
            try {
                local_date = LocalDate.of(Integer.parseInt(specific_dateInfo[0]), Integer.parseInt(specific_dateInfo[1]), Integer.parseInt(specific_dateInfo[2]));
            } catch (NumberFormatException numberFormatException) {
                JOptionPane.showMessageDialog(frame, "所输入日期格式错误！");
                specific_date.setText("");
                return;
            }
            if ((local_date.toEpochDay() - courseSchedule.get_Datestart().toEpochDay()) >= (courseSchedule.get_Week() * 7L)) {
                JOptionPane.showMessageDialog(frame, "输入日期不在教学周内！");
                specific_date.setText("");
                return;
            }

            StringBuilder strb = courseSchedule.printf_thatday(Integer.parseInt(specific_dateInfo[0]), Integer.parseInt(specific_dateInfo[1]), Integer.parseInt(specific_dateInfo[2]));
            result_text.setEditable(true);
            result_text.setText(strb.toString());
            result_text.setEditable(false);
        });

    }

    private static JFrame refresh_Course(priv.adt.startup.API.CourseSchedule courseSchedule) {
        JFrame frame = new JFrame("查询课表情况");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        String[] columns_Name = {"时间段", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        String[][] row_data = new String[5][8];
        row_data[0][0] = "8:00 - 10:00";
        row_data[1][0] = "10:00 - 12:00";
        row_data[2][0] = "13:00 - 15:00";
        row_data[3][0] = "15:00 - 17:00";
        row_data[4][0] = "19:00 - 21:00";

        ArrayList<Map.Entry<Course, Integer>> index_Map = courseSchedule.getIndex();
        for (Map.Entry<Course, Integer> course_index : index_Map) {
            dataIndex data_index = toDataIndex(course_index.getValue());
            StringBuilder strb;
            if (row_data[data_index.row][data_index.col] == null) {
                strb = new StringBuilder();
            } else {
                strb = new StringBuilder(row_data[data_index.row][data_index.col]);
            }
            strb.append(course_index.getKey().get_name()).append("\n").append(course_index.getKey().get_teacher()).append("\n").append(course_index.getKey().get_location()).append("\n\n");
            row_data[data_index.row][data_index.col] = strb.toString();
        }

        JTable table = new JTable(row_data, columns_Name);
        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(150);
        table.setDefaultRenderer(Object.class, new Table_viewRenderer());
        JScrollPane scroll_Pane = new JScrollPane(table);
        panel.add(scroll_Pane);
        frame.add(panel, BorderLayout.CENTER);

        JLabel label = new JLabel();
        label.setText("空闲的时间比例为：" + (IntervalSetUtils.cal_Free(courseSchedule) < 0.00001 ? IntervalSetUtils.cal_Free(courseSchedule) : 0) + "    重复的时间比例为：" + (IntervalSetUtils.cal_Conflict(courseSchedule) < 0.00001 ? IntervalSetUtils.cal_Conflict(courseSchedule) : 0));
        label.setPreferredSize(new Dimension(0, 50));
        frame.add(label, BorderLayout.AFTER_LAST_LINE);

        frame.setSize(1200, 800);
        return frame;
    }


    //返回对应的课程在rowData中的位置
    private static dataIndex toDataIndex(int index) {
        int col = index / 24 + 1;
        int row = index - (col - 1) * 24;
        switch (row) {
            case 8:
                row = 0;
                break;
            case 10:
                row = 1;
                break;
            case 13:
                row = 2;
                break;
            case 15:
                row = 3;
                break;
            case 19:
                row = 4;
                break;
        }
        return new dataIndex(row, col);
    }

    //标识所查在二维数组rowData的位置
    public static class dataIndex {
        public final int row;
        public final int col;

        public dataIndex(int row, int column) {
            this.row = row;
            this.col = column;
        }
    }

    //自动换行
    public static class Table_viewRenderer extends JTextArea implements TableCellRenderer {

        public Table_viewRenderer() {
            setLineWrap(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }
}
