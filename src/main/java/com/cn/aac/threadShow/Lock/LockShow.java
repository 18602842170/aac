package com.cn.aac.threadShow.Lock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

//此外，JAVA还提供isFair()来判断一个锁是不是公平锁。
//lock.isFair();

//Java提供了getHoldCount()方法来获取当前线程的锁定个数。
//所谓锁定个数就是当前线程调用lock方法的次数。
//一般一个方法只会调用一个lock方法，但是有可能在同步代码中还有调用了别的方法，那个方法内部有同步代码。
//这样，getHoldCount()返回值就是大于1。
//lock.getHoldCount();

//获取等待锁的线程数
//Java提供了getQueueLength()方法来得到等待锁释放的线程的个数。
//lock.getQueueLength();

//查询该Thread是否等待该lock对象的释放。
//同样，Java提供了一个简单判断是否有线程在等待锁释放即hasQueuedThreads()。
//lock.hasQueuedThread(thread)

//查询当前线程是否持有锁定
//判断当前线程是否有此锁定。
//isHeldByCurrentThread()

//简单判断一个锁是不是被一个线程持有
//isLocked()

//实现加锁，但是当线程被中断的时候，就会加锁失败，进行异常处理阶段。
//一般这种情况出现在该线程已经被打上interrupted的标记了。
//lockInterruptibly()

//尝试加锁，只有该锁未被其他线程持有的基础上，才会成功加锁。
//tryLock()

/**
 * LOCK有一重要特性，可重入，
 * 在同一个线程中，方法A中调用方法B中调用方法C，
 * ABC方法体中都可以调用lock.lock()，加锁三次。
 * 解锁时，也许要在此线程解锁三次才能真正解锁。
 * @author Administrator
 *
 */
public class LockShow {
    
    private Integer ticket = 100;
    
    /**
     * 实现公平锁
     * 在实例化锁对象的时候，构造方法有2个，一个是无参构造方法，一个是传入一个boolean变量的构造方法。当传入值为true的时候，该锁为公平锁。默认不传参数是非公平锁。
     * 公平锁：按照线程加锁的顺序来获取锁
     * 非公平锁：随机竞争来得到锁
     */
    private ReentrantLock lock = new ReentrantLock(true);
    
    public boolean getTicket() {
        lock.lock();
        try {
            if (ticket > 0) {
                ticket = ticket - 1;
                System.out.println(Thread.currentThread().getName() + "买到一张票，还有" + this.ticket + "张票");
                return true;
            } else {
                System.out.println("没有票了");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
        
    }
    
    public static void main(String[] args) {
        LockShow ls = new LockShow();
        
        Semaphore semaphore = new Semaphore(10);
        
        ExecutorService executorService = Executors.newCachedThreadPool();
        
        Callable<Boolean> runC = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    semaphore.acquire();
                    Thread.sleep(3000);
                    return ls.getTicket();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
                return false;
            }
        };
        
        for (int i = 0; i < 100; i++) {
            Future<Boolean> ft = executorService.submit(runC);
        }
        
    }
    
}
