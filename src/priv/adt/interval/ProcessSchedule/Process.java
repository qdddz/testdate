package priv.adt.interval.ProcessSchedule;

/**
 * 表示一个进程以及他的相关信息，例如何时被调用
 */
public class Process {
    private final String id;
    private final String name;
    private final long min_time;
    private final long max_time;

    public Process(String id, String name, long min_time, long max_time) {
        this.id = id;
        this.name = name;
        this.min_time = min_time;
        this.max_time = max_time;
    }


    public long get_Min() {
        return min_time;
    }

    public long get_Max() {
        return max_time;
    }

    public String get_id() {
        return id;
    }

    public String get_name() {
        return name;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minExecuteTime=" + min_time +
                ", maxExecuteTime=" + max_time +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        else {
            Process process = (Process) obj;
            return id.equals(process.id);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < id.length(); i++) {
            hash = hash * 31 + id.charAt(i);
        }
        return hash;
    }

}
