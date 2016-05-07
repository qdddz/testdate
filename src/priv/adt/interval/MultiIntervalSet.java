package priv.adt.interval;

/**
 * 表示一个时间段的集合，标签对象L可以被绑定到多个时间段上。
 * <p>要求：
 * <p>1.MultiIntervalSet的子类是mutable类型的；
 * <p>2.L对象是immutable类型的<p>
 */

public interface MultiIntervalSet<L> extends IntervalSet<L> {

    /**
     * 返回一个空集合
     *
     * @return 返回一个具体实现类的空集
     */
    static MultiIntervalSet<?> empty() {
        return null;
    }

    /**
     * 返回和一个标签相关联的所有时间段
     *
     * @param label 待查询的时间段标签
     * @return IntervalSet 和这个标签所有相关联的时间段，并且按照开始时间点的顺序升序进行排列，label按自然数升序
     */
    IntervalSet<Integer> intervals(L label);
}
