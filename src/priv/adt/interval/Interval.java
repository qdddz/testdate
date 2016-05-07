package priv.adt.interval;

/**
 * 时间段表示
 */
public class Interval<L> {
    private final long start;
    private final long end;
    private final L label;

    public Interval(L label, long start, long end) {
        this.label = label;
        this.start = start;
        this.end = end;
    }

    public L get_label() {
        return label;
    }

    public long get_start() {
        return start;
    }

    public long get_end() {
        return end;
    }

    @Override
    public boolean equals(Object x) {
        if (this == x)
            return true;
        if (x == null || getClass() != x.getClass())
            return false;
        Interval<?> interval = (Interval<?>) x;
        return start == interval.start && end == interval.end && label.equals(interval.label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public String toString() {
        return label.toString() + start + " -- " + end;
    }
}
