package priv.adt.interval;

import java.util.Collection;
import java.util.Set;

/**
 * 表示一个时间段的集合，标签对象L只能绑定到某一个时间段上。
 * <p>要求：
 * <p>1.MultiIntervalSet的子类是mutable类型的；
 * <p>2.L对象是immutable类型的<p>
 */

public interface IntervalSet<L> {

    /**
     * 返回一个空集合
     *
     * @return 返回一个具体实现类的空集
     */
    static IntervalSet<?> empty() {
        return null;
    }

    /**
     * 返回和指定标签相关联的时间段的起始时间点
     *
     * @param label 待查询时间段的标签
     * @return long 时间段起始时间点，如果查不到返回-1
     */
    long start(L label);

    /**
     * 返回和指定标签相关联的时间段的结束时间点
     *
     * @param label 待查询时间段的标签
     * @return long 时间段结束时间点，如果查不到返回-1
     */
    long end(L label);

    /**
     * 向集合中添加一个时间段
     *
     * @param start 开始时间点
     * @param end   结束时间点
     * @param label 时间段的标识(tag)
     */
    void insert(long start, long end, L label);


    /**
     * 返回当前对象的标签集合
     *
     * @return java.util.Set<L>
     */
    Set<L> labels();

    /**
     * 移除一个标签相关联的时间段
     *
     * @param label 待移除时间段的标签
     * @return boolean 是否移除成功
     */
    boolean remove(L label);


    /**
     * 返回时间段集合的总的开始时间
     *
     * @return 总开始时间
     */
    long new_Start();

    /**
     * 返回时间段集合的总的结束时间
     *
     * @param
     * @return 总结束时间
     */
    long new_End();


    /**
     * 返回时间段集合
     *
     * @return 时间段集合
     */
    Collection<Interval<L>> get_intervalSet();
}
