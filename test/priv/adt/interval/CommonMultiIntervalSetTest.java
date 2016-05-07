package priv.adt.interval;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * 对于CommonMultiIntervalSet实现的测试
 */
public class CommonMultiIntervalSetTest {

    /*
    测试策略：
    1.先测试空集合
    2.再加入几个时间段测试
     */
    @Test
    public void labels() {
        CommonIntervalSet<String> new_intervalSet = new CommonIntervalSet<>(0,25);
        assertEquals(Collections.emptySet(),new_intervalSet.labels());
        HashSet<String> new_Set = new HashSet<>();
        ArrayList<String> new_list1 = new ArrayList<>();
        ArrayList<String> new_list2 = new ArrayList<>();

        new_intervalSet.insert(0,10,"DVA");
        new_intervalSet.insert(11,16,"VIVY");
        new_Set.add("DVA");
        new_Set.add("VIVY");
        new_list1.addAll(new_intervalSet.labels());
        new_list2.addAll(new_Set);

        new_list2.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        new_list1.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        for (int i = 0; i < new_list1.size(); i++) {
            assertEquals(new_list1.get(i), new_list2.get(i));
        }
    }

    /*
   测试策略：
   1.先测试一个空的时间段
   2.加入两项再测试是否非空
   */
    @Test
    public void get_interval() {

        CommonIntervalSet<String> new_interval = new CommonIntervalSet<>(0,25);
        ArrayList<Interval<String>> new_list = new_interval.get_intervalSet();
        assertEquals(Collections.emptyList(),new_list);
        new_interval.insert(0,10,"DVA");
        new_interval.insert(11,16,"VIVY");
        new_list = new_interval.get_intervalSet();
        assertNotEquals(Collections.emptyList(),new_list);
    }

    /*
    测试策略：
    1.移除集合中已有的，应返回true
    2.移除集合中没有的，应返回false
    */
    @Test
    public void remove() {
        CommonIntervalSet<String> new_interval = new CommonIntervalSet<>(0,25);
        new_interval.insert(0,20,"DVA");
        new_interval.insert(21,25,"VIVY");
        assertTrue(new_interval.remove("DVA"));
        assertFalse(new_interval.remove("GRACE"));
    }


    @Test
    public void new_Start() {
        CommonIntervalSet<String> new_interval = new CommonIntervalSet<>(0, 25);
        assertEquals(0, new_interval.new_Start());
        assertEquals(25, new_interval.new_End());
    }

    /*
    测试策略：
    1.给出三个时间段
    2.有一个标签对应的时间段，数量等于1
    3.有多个标签对应的时间段，数量大于1
    4.没有标签对应的时间段，数量等于0
    */
    @Test
    public void intervals() {
        CommonMultiIntervalSet<String> new_interval = new CommonMultiIntervalSet<>(0,25);
        new_interval.insert(11,15,"VIVY");
        new_interval.insert(0,5,"DVA");
        new_interval.insert(6,10,"DVA");

        IntervalSet<Integer> test_1 = new_interval.intervals("DVA");
        IntervalSet<Integer> test_2 = new_interval.intervals("VIVY");
        IntervalSet<Integer> test_3 = new_interval.intervals("GRACE");

        assertEquals(0,test_3.get_intervalSet().size());
        assertEquals(2,test_1.get_intervalSet().size());
        assertEquals(1,test_2.get_intervalSet().size());
    }
}
