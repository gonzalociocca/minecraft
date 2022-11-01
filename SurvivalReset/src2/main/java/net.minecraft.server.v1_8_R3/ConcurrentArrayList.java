package net.minecraft.server.v1_8_R3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentArrayList<T> extends ArrayList<T> {
/*
    public final ReentrantLock relock = new ReentrantLock();

    @Override
    public boolean add(T e){
        relock.lock();
        try{
            return super.add(e);
        }finally{
            relock.unlock();
        }
    }

    @Override
    public boolean remove(Object e){
        relock.lock();
        try{
            return super.remove(e);
        }finally{
            relock.unlock();
        }
    }

    @Override
    public T remove(int index){
        relock.lock();
        try{
            return super.remove(index);
        }finally{
            relock.unlock();
        }
    }

    @Override
    public T get(int index){
        relock.lock();
        try{
            return super.get(index);
        }finally{
            relock.unlock();
        }
    }
    @Override
    public int size(){
        relock.lock();
        try{
            return super.size();
        }finally{
            relock.unlock();
        }
    }
    @Override
    public int indexOf(Object o){
        relock.lock();
        try{
            return super.indexOf(o);
        }finally {
            relock.unlock();
        }
    }

    @Override
    public boolean contains(Object o) {
        relock.lock();
        try{
            return super.contains(o);
        }finally{
            relock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        relock.lock();
        try{
            return super.isEmpty();
        }finally{
            relock.unlock();
        }
    }
    @Override
    public void clear() {
        relock.lock();
        try{
            super.clear();
        }finally{
            relock.unlock();
        }
    }
    @Override
    public boolean removeAll(Collection<?> var1) {
        relock.lock();
        try{
            return super.removeAll(var1);
        }finally{
            relock.unlock();
        }
    }
    @Override
    public boolean addAll(Collection<? extends T> var1) {
        relock.lock();
        try{
            return super.addAll(var1);
        }finally{
            relock.unlock();
        }
    }

    @Override
    public Iterator iterator(){

        relock.lock();
        try {
            //return super.iterator();
            return new ArrayList<T>(this).iterator();
        }finally{
            relock.unlock();
        }//^ we iterate over an snapshot of our list


    }
}*/


    public final Lock readLock;

    public final Lock writeLock;


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
            return new ArrayList<T>(this).iterator();
        }finally{
            readLock.unlock();
        }//^ we iterate over an snapshot of our list


    }
}