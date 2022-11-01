package org.bukkit.craftbukkit.v1_8_R3.util;

        import com.google.common.collect.Sets;

        import java.util.*;
        import java.util.concurrent.ConcurrentLinkedDeque;
        import java.util.concurrent.locks.Lock;
        import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HashTreeSet<V> implements Set<V> {

    private Set<V> hash = Sets.newHashSet();
    private TreeSet<V> tree = new TreeSet();


    public HashTreeSet() {

        }

    @Override
    public int size() {
     return hash.size();
    }

    @Override
    public boolean isEmpty() {
       return hash.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return hash.contains(o);
    }

    @Override
    public Iterator<V> iterator() {

        return new Iterator<V>() {

            private Iterator<V> it = tree.iterator();
            private V last;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public V next() {
                last = it.next();
                return last;
            }

            @Override
            public void remove() {

                if (last == null) {
                    throw new IllegalStateException();
                }
                it.remove();
                hash.remove(last);
                last = null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return hash.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return hash.toArray(a);
    }

    @Override
    public boolean add(V e) {

        hash.add(e);
        return tree.add(e);
    }

    @Override
    public boolean remove(Object o) {

        hash.remove(o);
        return tree.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return hash.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        tree.addAll(c);
        return hash.addAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        tree.retainAll(c);
        return hash.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        tree.removeAll(c);
        return hash.removeAll(c);
    }

    @Override
    public void clear() {
            hash.clear();
            tree.clear();
    }

    public V first()
    {
        return tree.first();
    }

}