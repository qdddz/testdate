package priv.adt.decorator;

import priv.adt.interval.Interval;
import priv.adt.interval.IntervalSet;

import java.util.Collection;
import java.util.Set;

public class BaseDecorator<L> implements IntervalSet<L> {
    protected final IntervalSet<L> intervalSet;

    public BaseDecorator(IntervalSet<L> intervalSet) {
        this.intervalSet = intervalSet;
    }

    @Override
    public Collection<Interval<L>> get_intervalSet() {
        return intervalSet.get_intervalSet();
    }

    @Override
    public long new_Start() {
        return intervalSet.new_Start();
    }

    @Override
    public long new_End() {
        return intervalSet.new_End();
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
        intervalSet.insert(start, end, label);
    }

    @Override
    public boolean remove(L label) {
        return intervalSet.remove(label);
    }

    @Override
    public Set<L> labels() {
        return intervalSet.labels();
    }

}
