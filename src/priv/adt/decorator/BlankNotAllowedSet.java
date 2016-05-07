package priv.adt.decorator;

import priv.adt.interval.Interval;
import priv.adt.interval.IntervalSet;

import java.util.Arrays;
import java.util.Collection;

/**
 * 对于不允许有空白时间段的辅助
 */
public class BlankNotAllowedSet<L> extends BaseDecorator<L> {

    //定义状态

    public BlankNotAllowedSet(IntervalSet<L> intervalSet) {
        super(intervalSet);
    }

    /**
     * 检查是否满足没有空白时间段的要求
     * 这个方法应该在所有的insert结束后被调用
     *
     * @return 满足要求返回true
     */
    public boolean checkBlank() {
        //离散型时间段，直接用数组进行枚举
        long new_Start = intervalSet.new_Start();
        long new_End = intervalSet.new_End();
        long size = new_End - new_Start + 1;
        int[] ints = new int[(int) size];
        Arrays.fill(ints, 0);

        Collection<Interval<L>> new_interval = this.intervalSet.get_intervalSet();
        for (Interval<L> a : new_interval)
            Arrays.fill(ints, (int) (a.get_start() - new_Start), (int) (a.get_end() - new_Start) + 1, 1);
        for (int i = 0; i < size; i++)
            if (ints[i] == 0)
                return false;
        return true;
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
