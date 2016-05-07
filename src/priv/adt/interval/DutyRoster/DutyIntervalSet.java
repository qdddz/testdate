package priv.adt.interval.DutyRoster;

import priv.adt.decorator.BlankNotAllowedSet;
import priv.adt.decorator.OverlapNotAllowedSet;
import priv.adt.interval.CommonIntervalSet;
import priv.adt.interval.Interval;

/**
 * 排班系统需满足如下的要求：
 * 1.时间段必须连续
 * 2.时间段互相不可以重叠
 * 3.标签不可以重复
 * 4.无周期性
 */
public class DutyIntervalSet<L> extends CommonIntervalSet<L> {

    /*
    AF：（sum_intervals） = 排班时间表
    (L) = 员工
    (Interval对象) -> 排班表起始日期——排班结束日期
   （start） = 时间段的开始时间
   （end） = 时间段的结束时间

    RI：
    对于每一个属于intervals：
    1.L不应为空，且唯一
    2.0<=new_Start<=start<=end<=new_End

    Safe from RE：
    用protected final修饰
     */

    public DutyIntervalSet(long new_Start, long new_End) {
        super(new_Start, new_End);
    }

    //按照日期进行排序
    public void sort() {
        sum_interval.sort((Interval<L> o1, Interval<L> o2)
                -> (int) (o1.get_start() - o2.get_start()));
    }

    public void check() {
        BlankNotAllowedSet<L> check = new BlankNotAllowedSet<>(this);
        if (!(check).checkBlank())
            throw new RuntimeException("排班时间段存在空缺！");
        OverlapNotAllowedSet<L> check2 = new OverlapNotAllowedSet<>(check);
        if (check2.check_Overlap())
            throw new RuntimeException("排班时间段存在重复！");
    }
}
