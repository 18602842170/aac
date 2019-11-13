package com.cn.aac.threadShow.Lock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁分成两个锁，一个锁是读锁，一个锁是写锁。
 * 读锁与读锁之间是共享的，
 * 读锁与写锁之间是互斥的，
 * 写锁与写锁之间也是互斥的。
 */
public class ReadWriteLockDemo {
    
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void read() {
        try {
            lock.readLock().lock();
            Thread.sleep(1000);
            System.out.println("获得读锁" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void write() {
        try {
            lock.writeLock().lock();
            Thread.sleep(1000);
            System.out.println("获得写锁" + Thread.currentThread().getName() + " " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        //        Thread t1 = new Thread(demo::read, "T1");
        //        Thread t2 = new Thread(demo::read, "T2");
        //        t1.start();
        //        t2.start();
        
        Thread t1 = new Thread(demo::write, "T1");
        Thread t2 = new Thread(demo::write, "T2");
        Thread t3 = new Thread(demo::read, "T3");
        Thread t4 = new Thread(demo::read, "T4");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        
        ThreadFactory fact = new ThreadFactory() {
            
            @Override
            public Thread newThread(Runnable r) {
                // TODO Auto-generated method stub
                return new Thread(r);
            }
        };
        
        new ThreadPoolExecutor(10, Integer.MAX_VALUE, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), fact, (Runnable r, ThreadPoolExecutor executor) -> {
            System.out.println(r);
            System.out.println(executor);
        });
    }
}
