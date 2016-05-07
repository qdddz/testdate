package priv.adt.decorator;

import priv.adt.interval.Interval;
import priv.adt.interval.IntervalSet;

import java.util.Arrays;
import java.util.Collection;

/**
 * 对于某些类，并不允许重叠时间段
 */
public class OverlapNotAllowedSet<L> extends BaseDecorator<L> {


    public OverlapNotAllowedSet(IntervalSet<L> intervalSet) {
        super(intervalSet);
    }

    /**
     * 检查是否满足没有重叠时间段的要求<p>
     * 这个方法应该在所有的insert结束后被调用
     *
     * @return 满足要求返回true
     */
    public boolean check_Overlap() {
        //离散型时间段，直接用数组进行枚举
        long new_start = intervalSet.new_Start();
        long new_end = intervalSet.new_End();
        int[] ints = new int[(int) (new_end - new_start + 1)];
        Arrays.fill(ints, 0);

        Collection<Interval<L>> new_interval = this.intervalSet.get_intervalSet();
        for (Interval<L> a : new_interval) {
            for (int i = (int) (a.get_start() - new_start); i <= (int) (a.get_end() - new_start); i++)
                if (ints[i] == 1)
                    return true;
            Arrays.fill(ints, (int) (a.get_start() - new_start), (int) (a.get_end() - new_start) + 1, 1);
        }

        return false;
    }

    @Override
    public long start(L label) {
        return 0;
    }

    @Override
    public long end(L label) {
        return 0;
    }
}
