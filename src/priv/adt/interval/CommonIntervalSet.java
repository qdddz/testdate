package priv.adt.interval;

import java.util.*;

/**
 * 表示一个基本待扩展时间段的集合
 */
public class CommonIntervalSet<L> implements IntervalSet<L> {

    protected final ArrayList<Interval<L>> sum_interval;
    protected final long new_Start;
    protected final long new_End;

    /*
    AF:(sum_interval) = 时间段的集合
    (L) = 各个时间段的标签
   （Interval对象）=时间段起点与终点
   （start） = 时间段的开始时间
   （end） = 时间段的结束时间

    RI：对于每一个intervals对象：
    1.L不应为空，且唯一
    2.0<=new_Start<=start<=end<=new_End

    Safe from RE：
    用protected final修饰
     */

    public CommonIntervalSet(long new_Start, long new_End) {
        this.new_Start = new_Start;
        this.new_End = new_End;
        this.sum_interval = new ArrayList<>();
        checkRep();
    }

    @Override
    public void insert(long start, long end, L label) {
        for (Interval<L> a : sum_interval) {
            if (a.get_label().equals(label))
                throw new RuntimeException("不能出现重复的标签！");
        }
        sum_interval.add(new Interval<>(label, start, end));
        checkRep();
    }

    @Override
    public Set<L> labels() {
        HashSet<L> set = new HashSet<>();
        for (Interval<L> a : sum_interval)
            set.add(a.get_label());
        return set;
    }

    @Override
    public ArrayList<Interval<L>> get_intervalSet() {
        return new ArrayList<>(sum_interval);
    }

    @Override
    public long new_Start() {
        return this.new_Start;
    }

    @Override
    public long new_End() {
        return this.new_End;
    }

    @Override
    public boolean remove(L label) {
        boolean result;
        Iterator<Interval<L>> a = sum_interval.iterator();
        result = false;
        while (a.hasNext()) {
            Interval<L> b = a.next();
            if (b.get_label().equals(label)) {
                a.remove();
                result = true;
                break;
            }
        }
        checkRep();
        return result;
    }

    private List<Interval<L>> to_ordered() {
        List<Interval<L>> list = new ArrayList<>(sum_interval);
        list.sort(Comparator.comparingLong(Interval::get_start));
        return list;
    }

    @Override
    public long start(L label) {
        for (Interval<L> a : sum_interval) {
            if (a.get_label().equals(label))
                return a.get_start();
        }
        return -1;
    }

    @Override
    public long end(L label) {
        for (Interval<L> a : sum_interval) {
            if (a.get_label().equals(label))
                return a.get_end();
        }
        return -1;
    }

    @Override
    public String toString() {
        List<Interval<L>> list = to_ordered();
        StringBuilder result = new StringBuilder();
        result.append("目前的时间段集合情况：\n" + "总的开始时间 ：").append(this.new_Start).append("\n").append("全部结束时间 ：").append(this.new_End).append("\n");
        for (Interval<L> a : list)
            result.append(a.get_start()).append(" : ").append(a.get_end()).append("\n");
        return result.toString();
    }

    //检查是否符合要求
    private void checkRep() {
        assert (this.new_Start >= 0);
        assert (this.new_End >= this.new_Start);
        for (Interval<L> a : sum_interval) {
            assert (a.get_label() != null);
            assert (a.get_start() <= a.get_end());
            assert (a.get_start() >= this.new_Start);
            assert (a.get_end() <= this.new_End);
        }
    }

}
