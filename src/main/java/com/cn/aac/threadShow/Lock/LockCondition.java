package com.cn.aac.threadShow.Lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 在到达指定时间之后，线程会自动唤醒。但是无论是await或者awaitUntil，当线程中断时，进行阻塞的线程会产生中断异常。
 * Java提供了一个awaitUninterruptibly的方法，使即使线程中断时，进行阻塞的线程也不会产生中断异常
 * awaitUntil（Date deadline）
 */
public class LockCondition {
    int a = 0;
    private ReentrantLock lock = new ReentrantLock(); //创建Lock对象
    public Condition condition = lock.newCondition(); //创建Condition对象
    
    public void await() {
        try {
            lock.lock();
            System.out.println("当前线程是否持有锁：" + lock.isHeldByCurrentThread());
            System.out.println("当前等待个数：" + lock.getQueueLength());
            Thread.sleep(1000);
            System.out.println("await时间为：" + System.currentTimeMillis() + "--" + Thread.currentThread().getName());
            condition.await();
            a++;
            System.out.println("signal时间：" + System.currentTimeMillis() + "--" + Thread.currentThread().getName());
            condition.signalAll();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
    }
    
    public void signal() {
        try {
            lock.lock();
            condition.signalAll();
            System.out.println("signalAll：" + System.currentTimeMillis());
            condition.await();
            System.out.println("signal唤醒：" + System.currentTimeMillis());
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            lock.unlock();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        LockCondition myService = new LockCondition();
        System.out.println("是不是被一个线程持有?" + myService.lock.isLocked());
        MyThread myThread1 = new MyThread(myService);
        MyThread myThread2 = new MyThread(myService);
        MyThread myThread3 = new MyThread(myService);
        MyThread myThread4 = new MyThread(myService);
        MyThread myThread5 = new MyThread(myService);
        MyThread myThread6 = new MyThread(myService);
        MyThread myThread7 = new MyThread(myService);
        MyThread myThread8 = new MyThread(myService);
        myThread1.start();
        myThread2.start();
        myThread3.start();
        myThread4.start();
        myThread5.start();
        myThread6.start();
        myThread7.start();
        myThread8.start();
        Thread.sleep(1000);
        System.out.println("是不是被一个线程持有?" + myService.lock.isLocked());
        System.out.println("thread8iswait?" + myService.lock.hasQueuedThread(myThread8));
        System.out.println("hasQueuedThreads?" + myService.lock.hasQueuedThreads());
        myService.signal();
        Thread.sleep(500);
        System.out.println(myService.a);
    }
}

class MyThread extends Thread {
    private LockCondition myserice;
    
    public MyThread(LockCondition myserice) {
        super();
        this.myserice = myserice;
    }
    
    @Override
    public void run() {
        myserice.await();
    }
}
