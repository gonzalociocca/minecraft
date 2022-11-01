package net.minecraft.server.v1_8_R3;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLinkedList<T> extends LinkedList<T> {

    /** use this to lock for write operations like add/remove */
    private final Lock readLock;
    /** use this to lock for read operations like get/iterator/contains.. */
    private final Lock writeLock;
    /** the underlying list*/

    {
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    @Override
    public boolean add(T e){
        writeLock.lock();
        try{
            return super.add(e);
        }finally{
            writeLock.unlock();
        }
    }

    @Override
    public boolean remove(Object e){
        writeLock.lock();
        try{
            return super.remove(e);
        }finally{
            writeLock.unlock();
        }
    }

    @Override
    public T remove(int index){
        writeLock.lock();
        try{
            return super.remove(index);
        }finally{
            writeLock.unlock();
        }
    }

    @Override
    public T get(int index){
        readLock.lock();
        try{
            return super.get(index);
        }finally{
            readLock.unlock();
        }
    }

    @Override
    public int size(){
        readLock.lock();
        try{
            return super.size();
        }finally{
            readLock.unlock();
        }
    }
    @Override
    public int indexOf(Object o){
        readLock.lock();
        try{
            return super.indexOf(o);
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        readLock.lock();
        try{
            return super.contains(o);
        }finally{
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try{
            return super.isEmpty();
        }finally{
            readLock.unlock();
        }
    }
    @Override
    public void clear() {
        writeLock.lock();
        try{
            super.clear();
        }finally{
            writeLock.unlock();
        }
    }
    @Override
    public boolean removeAll(Collection<?> var1) {
        writeLock.lock();
        try{
            return super.removeAll(var1);
        }finally{
            writeLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> var1) {
        writeLock.lock();
        try{
            return super.addAll(var1);
        }finally{
            writeLock.unlock();
        }
    }

    @Override
    public Iterator iterator(){
        readLock.lock();
        try {
            //return super.iterator();
            return new ArrayList<T>( this ).iterator();
            //^ we iterate over an snapshot of our list
        } finally{
            readLock.unlock();
        }
    }
}