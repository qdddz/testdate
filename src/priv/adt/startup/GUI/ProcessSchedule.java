package priv.adt.startup.GUI;

import priv.adt.interval.Interval;
import priv.adt.interval.ProcessSchedule.Process;
import priv.adt.interval.ProcessSchedule.ProcessIntervalSet;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ProcessScheduleApp的图形化界面
 *
 * @author Edmund_Lai
 * @e-mail 1191000311@stu.hit.edu.cn
 * @create 2021-06-11-8:41
 */
public class ProcessSchedule {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(ProcessSchedule::mainFrame);
    }
    /**
     * 主控面板，包括进程添加、随机选择进程策略可视化、最短进程优先策略可视化
     */
    public static void mainFrame() {
        JFrame frame = new JFrame("进程调度可视化系统主控面板");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setBounds(200, 200, 600, 400);
        frame.setVisible(true);

        priv.adt.startup.API.ProcessSchedule processSchedule = new priv.adt.startup.API.ProcessSchedule();

        JButton processManageButton = new JButton("进程添加");
        processManageButton.setContentAreaFilled(false);
        frame.add(processManageButton);
        processManageButton.addActionListener(e -> processManage(processSchedule));

        JButton checkProcessButton = new JButton("进程信息查询");
        checkProcessButton.setContentAreaFilled(false);
        frame.add(checkProcessButton);
        checkProcessButton.addActionListener(e -> checkProcess(processSchedule));

        JButton randomScheduleVisualizeButton = new JButton("随机选择进程策略可视化");
        randomScheduleVisualizeButton.setContentAreaFilled(false);
        frame.add(randomScheduleVisualizeButton);
        randomScheduleVisualizeButton.addActionListener(e -> {
            if (processSchedule.get_List().size() == 0) {
                JOptionPane.showMessageDialog(frame, "当前进程数目为0，请至少添加一个进程");
            } else {
                randomScheduleVisualize(processSchedule);
            }
        });

        JButton processManageVisualizeButton = new JButton("最短进程优先策略可视化");
        processManageVisualizeButton.setContentAreaFilled(false);
        frame.add(processManageVisualizeButton);
        processManageVisualizeButton.addActionListener(e -> {
            if (processSchedule.get_List().size() == 0) {
                JOptionPane.showMessageDialog(frame, "当前进程数目为0，请至少添加一个进程");
            } else {
                SJBManageVisualize(processSchedule);
            }
        });

    }

    /**
     * 进程添加
     */
    public static void processManage(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = new JFrame("进程添加");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1));
        frame.setVisible(true);
        frame.setSize(800, 400);

        String[] panelsName = {"待添加进程的ID：", "待添加进程的名称", "最小执行时间：", "最大执行时间："};
        java.util.List<JPanel> panelsList = new ArrayList<>();
        List<JTextField> textList = new ArrayList<>();
        for (String s : panelsName) {
            //每一行的panel
            JPanel newPanel = new JPanel();
            panelsList.add(newPanel);
            newPanel.setLayout(new FlowLayout());

            //panel内部结构、panel内部布局
            newPanel.add(new JLabel(s));
            JTextField newText;
            newText = new JTextField(16);
            textList.add(newText);
            newPanel.add(newText);

            //添加Panel
            frame.add(newPanel);
        }

        //确认
        JButton enterButton = new JButton("确认");
        enterButton.setContentAreaFilled(false);
        frame.add(enterButton);

        enterButton.addActionListener(e -> {
            List<String> gotString = new ArrayList<>();
            for (int i = 0; i < panelsName.length; i++) {
                gotString.add(textList.get(i).getText());
            }
            String processID = gotString.get(0);
            String processName = gotString.get(1);
            String processMinExecuteTime = gotString.get(2);
            String processMaxExecuteTime = gotString.get(3);
            Process process = new Process(processID, processName, Long.parseLong(processMinExecuteTime), Long.parseLong(processMaxExecuteTime));
            HashSet<Process> processes = new HashSet<>();
            processes.add(process);
            processSchedule.add_Process(processes);
            JOptionPane.showMessageDialog(frame, "进程添加成功！");
            for (int i = 0; i < panelsName.length; i++) {
                textList.get(i).setText("");
            }
            refreshProcessInfo(processSchedule);
        });
    }

    /**
     * 已添加进程信息查询
     */
    public static void checkProcess(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = refreshProcessInfo(processSchedule);
        frame.setVisible(true);
    }

    private static JFrame refreshProcessInfo(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = new JFrame("全部待执行进程信息");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        String[] columnsName = {"进程ID", "进程名", "最小执行时间", "最大执行时间"};
        String[][] rowData = new String[processSchedule.get_List().size()][4];
        ArrayList<Process> processes = new ArrayList<>(processSchedule.get_List().keySet());

        for (int i = 0; i < processes.size(); i++) {
            rowData[i][0] = processes.get(i).get_id();
            rowData[i][1] = processes.get(i).get_name();
            rowData[i][2] = String.valueOf(processes.get(i).get_Min());
            rowData[i][3] = String.valueOf(processes.get(i).get_Max());
        }

        JTable table = new JTable(rowData, columnsName);

        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(30);

        //居中渲染
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, dtcr);


        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        frame.add(panel, BorderLayout.CENTER);

        frame.setSize(1000, 800);
        return frame;
    }

    /**
     * 随机选择进程策略可视化
     */
    public static void randomScheduleVisualize(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = refreshRandomScheduleVisualize(processSchedule);
        frame.setVisible(true);
    }

    /**
     * 最短进程优先策略可视化
     */
    public static void SJBManageVisualize(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = refreshSJBScheduleVisualize(processSchedule);
        frame.setVisible(true);
    }

    private static JFrame refreshRandomScheduleVisualize(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame randomScheduleVisualizeFrame = new JFrame("随机选择进程策略可视化");
        randomScheduleVisualizeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        randomScheduleVisualizeFrame.setLayout(new BorderLayout());
        randomScheduleVisualizeFrame.setSize(1200, 800);

        //表格部分
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnsName = {"被调度进程ID", "被调度进程名", "起始时间", "结束时间", "持续时间"};
        List<String[]> rowData = new ArrayList<>();
        processSchedule.randomSchedule();
        ProcessIntervalSet<Process> processIntervalSet = processSchedule.get_interval();
        processIntervalSet.sort();
        ArrayList<Interval<Process>> intervalSet = processIntervalSet.get_intervalSet();
        for (int i = 0; i < intervalSet.size(); i++) {
            if (i == 0) {
                Interval<Process> interval = intervalSet.get(0);
                if (interval.get_start() != 0) {
                    String[] blank = {"空闲", "-", "0", Long.toString(intervalSet.get(0).get_start()), Long.toString(intervalSet.get(0).get_start())};
                    rowData.add(blank);
                }
                String[] durationInfo = {interval.get_label().get_id(), interval.get_label().get_name(), Long.toString(interval.get_start()), Long.toString(interval.get_end()), Long.toString(interval.get_end() - interval.get_start())};
                rowData.add(durationInfo);
            } else {
                if (intervalSet.get(i).get_start() - intervalSet.get(i - 1).get_end() > 0) {
                    String[] blank = {"空闲", "-", Long.toString(intervalSet.get(i - 1).get_end()), Long.toString(intervalSet.get(i).get_start()), Long.toString(intervalSet.get(i).get_start() - intervalSet.get(i - 1).get_end())};
                    rowData.add(blank);
                }
                String[] durationInfo = {intervalSet.get(i).get_label().get_id(), intervalSet.get(i).get_label().get_name(), Long.toString(intervalSet.get(i).get_start()), Long.toString(intervalSet.get(i).get_end()), Long.toString(intervalSet.get(i).get_end() - intervalSet.get(i).get_start())};
                rowData.add(durationInfo);
            }
        }

        String[][] rowDateArray = new String[rowData.size()][5];
        for (int i = 0; i < rowData.size(); i++) {
            String[] strings = rowData.get(i);
            rowDateArray[i][0] = strings[0];
            rowDateArray[i][1] = strings[1];
            rowDateArray[i][2] = strings[2];
            rowDateArray[i][3] = strings[3];
            rowDateArray[i][4] = strings[4];
        }

        JTable table = new JTable(rowDateArray, columnsName);
        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.DARK_GRAY);
        table.setSelectionBackground(Color.LIGHT_GRAY);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(30);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, tcr);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        randomScheduleVisualizeFrame.add(panel, BorderLayout.CENTER);

        //清空进程信息，避免再次打印
        Set<Process> labels = processSchedule.get_interval().labels();
        for (Process label : labels) {
            processSchedule.get_interval().remove(label);
        }
        return randomScheduleVisualizeFrame;
    }

    private static JFrame refreshSJBScheduleVisualize(priv.adt.startup.API.ProcessSchedule processSchedule) {
        JFrame frame = new JFrame("最短进程优先策略可视化");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 40));
        frame.setVisible(true);
        frame.setSize(800, 600);

        //表格部分
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnsName = {"被调度进程ID", "被调度进程名", "起始时间", "结束时间", "持续时间"};
        List<String[]> rowData = new ArrayList<>();
        processSchedule.SJFSchedule();
        ProcessIntervalSet<Process> processIntervalSet = processSchedule.get_interval();
        processIntervalSet.sort();
        ArrayList<Interval<Process>> intervalSet = processIntervalSet.get_intervalSet();
        for (int i = 0; i < intervalSet.size(); i++) {
            if (i == 0) {
                Interval<Process> interval = intervalSet.get(0);
                if (interval.get_start() != 0) {
                    String[] blank = {"空闲", "-", "0", Long.toString(intervalSet.get(0).get_start()), Long.toString(intervalSet.get(0).get_start())};
                    rowData.add(blank);
                }
                String[] durationInfo = {interval.get_label().get_id(), interval.get_label().get_name(), Long.toString(interval.get_start()), Long.toString(interval.get_end()), Long.toString(interval.get_end() - interval.get_start())};
                rowData.add(durationInfo);
            } else {
                if (intervalSet.get(i).get_start() - intervalSet.get(i - 1).get_end() > 0) {
                    String[] blank = {"空闲", "-", Long.toString(intervalSet.get(i - 1).get_end()), Long.toString(intervalSet.get(i).get_start()), Long.toString(intervalSet.get(i).get_start() - intervalSet.get(i - 1).get_end())};
                    rowData.add(blank);
                }
                String[] durationInfo = {intervalSet.get(i).get_label().get_id(), intervalSet.get(i).get_label().get_name(), Long.toString(intervalSet.get(i).get_start()), Long.toString(intervalSet.get(i).get_end()), Long.toString(intervalSet.get(i).get_end() - intervalSet.get(i).get_start())};
                rowData.add(durationInfo);
            }
        }

        String[][] rowDateArray = new String[rowData.size()][5];
        for (int i = 0; i < rowData.size(); i++) {
            String[] strings = rowData.get(i);
            rowDateArray[i][0] = strings[0];
            rowDateArray[i][1] = strings[1];
            rowDateArray[i][2] = strings[2];
            rowDateArray[i][3] = strings[3];
            rowDateArray[i][4] = strings[4];
        }

        JTable table = new JTable(rowDateArray, columnsName);
        table.setForeground(Color.BLACK);
        table.setFont(new Font(null, Font.PLAIN, 14));
        table.setSelectionForeground(Color.pink);
        table.setSelectionBackground(Color.blue);
        table.setGridColor(Color.GRAY);
        table.setRowHeight(30);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        tcr.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, tcr);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        frame.add(panel, BorderLayout.CENTER);

        //清空进程信息，避免再次打印
        Set<Process> labels = processSchedule.get_interval().labels();
        for (Process label : labels) {
            processSchedule.get_interval().remove(label);
        }
        return frame;
    }

}
