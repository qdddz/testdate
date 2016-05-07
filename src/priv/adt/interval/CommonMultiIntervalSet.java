package priv.adt.interval;

import java.util.*;

/**
 * 表示一个时间段的集合，标签对象L可以被绑定到多个时间段上。
 * <p>要求：
 * <p>1.MultiIntervalSet的子类是mutable类型的；
 * <p>2.L对象是immutable类型的<p>
 */

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
    protected final ArrayList<Interval<L>> sum_interval;
    protected final long new_Start;
    protected final long new_End;

    /*
    AF:(sum_interval) = 时间段的集合
    (L) = 各个时间段的标签
   （Interval对象）=时间段起点与终点
   （start） = 时间段的开始时间
   （end） = 时间段的结束时间

    RI：对于每一个intervals：
    1.L不应为空，且唯一
    2.0<=new_Start<=start<=end<=new_End

    Safe from RE：
    用protected final修饰
     */


    public CommonMultiIntervalSet(long new_Start, long new_End) {
        this.new_Start = new_Start;
        this.new_End = new_End;
        this.sum_interval = new ArrayList<>();
        checkRep();
    }

    /**
     * 利用IntervalSet的实现类对象实例化一个MultiIntervalSet的具体实现类，相当于在原来的功能上
     * 增加了同一个标签对象允许被绑定到多个时间段上
     *
     * @param initial 一个IntervalSet实现类对象
     */
    public CommonMultiIntervalSet(IntervalSet<L> initial) {
        this.sum_interval = new ArrayList<>();
        Set<L> sum_label = initial.labels();
        for (L new_label : sum_label) {
            long new_start = initial.start(new_label);
            long new_end = initial.end(new_label);
            sum_interval.add(new Interval<>(new_label, new_start, new_end));
        }
        this.new_Start = initial.new_Start();
        this.new_End = initial.new_End();
        checkRep();
    }

    @Override
    public long start(L label) {
        return 0;
    }

    @Override
    public long end(L label) {
        return 0;
    }

    @Override
    public void insert(long start, long end, L label) {
        sum_interval.add(new Interval<>(label, start, end));
        checkRep();
    }

    @Override
    public Set<L> labels() {
        HashSet<L> result = new HashSet<>();
        for (Interval<L> a : sum_interval) {
            result.add(a.get_label());
        }
        return result;
    }

    @Override
    public boolean remove(L label) {
        boolean result;
        result = false;
        Iterator<Interval<L>> a = sum_interval.iterator();
        while (a.hasNext()) {
            Interval<L> b = a.next();
            if (b.get_label().equals(label)) {
                a.remove();
                result = true;
            }
        }
        checkRep();
        return result;
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
    public ArrayList<Interval<L>> get_intervalSet() {
        return new ArrayList<>(sum_interval);
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {

        CommonIntervalSet<Integer> a = new CommonIntervalSet<>(new_Start, new_End);
        int cnt = 0;
        for (Interval<L> b : sum_interval) {
            if (b.get_label().equals(label)) {
                a.insert(b.get_start(), b.get_end(), cnt++);
            }
        }
        return a;
    }

    @Override
    public String toString() {
        List<Interval<L>> new_list = to_Ordered();
        StringBuilder reu = new StringBuilder();
        reu.append("当前的时间段集合的情况：\n" + "总的开始时间 ：").append(this.new_Start).append("\n").append("总的结束时间 ：").append(this.new_End).append("\n");
        for (Interval<L> a : new_list) {
            reu.append(a.get_start()).append(" : ").append(a.get_end()).append("\n");
        }
        return reu.toString();
    }

    private List<Interval<L>> to_Ordered() {
        List<Interval<L>> new_list = new ArrayList<>(sum_interval);
        new_list.sort(Comparator.comparingLong(Interval::get_start));
        return new_list;
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
