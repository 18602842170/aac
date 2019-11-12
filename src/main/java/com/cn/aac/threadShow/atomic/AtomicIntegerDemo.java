package com.cn.aac.threadShow.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 更多原子更新基本类型
 * AtomicBoolean
 * AtomicInteger
 * AtomicLong
 * AtomicReference 原子更新引用类型，可指定泛型为自定义类
 * @author Administrator
 *
 */
public class AtomicIntegerDemo {
    
    //    public static AtomicReference<Consumer> ar = new AtomicReference<Consumer>(); 
    
    public static void main(String[] args) throws Exception {
        //        NotThreadSafeConcurrency.testAtomicInt();
        
        ThreadSafeConcurrency.testAtomicInteger();
    }
    
}

/**
 * 线程不安全的高并发实现
 * 客户端模拟执行 5000 个任务，线程数量是 200，每个线程执行一次，就将 count 计数加 1，当执行完以后，打印 count 的值。
 * @author Administrator
 *
 */
class NotThreadSafeConcurrency {
    
    private static int CLIENT_COUNT = 5000;
    private static int THREAD_COUNT = 200;
    private static int count = 0;
    
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 信标
     * Semaphore 是一个计数信号量，必须由获取它的线程释放。
     * 常用于限制可以访问某些资源的线程数量，例如通过 Semaphore 限流。
     * Semaphore 只有3个操作：初始化,增加,减少
     */
    private final static Semaphore semaphore = new Semaphore(THREAD_COUNT);
    /**
     * countDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行。
     * 是通过一个计数器来实现的，计数器的初始值是线程的数量。每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了
     */
    private final static CountDownLatch countDownLatch = new CountDownLatch(CLIENT_COUNT);
    
    public static void testAtomicInt() throws Exception {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            /**
             * 使用信标阻塞，一次最多有200个线程同时运行。
             * 使用countDownLatch每次减1.
             * countDownLatch.await()阻塞
             * 直到所有线程完成。打印数量
             */
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // count每加 1，进行减 1 计数
                countDownLatch.countDown();
            });
        }
        // 等待线程池所有任务执行结束
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("ConcurrencyDemo:" + count);
    }
    
    private static void add() {
        count++;
    }
}

/**
 * 线程安全的高并发实现 AtomicInteger
 * AtomicInteger 保证原子性
 * CAS
 * CAS 就是 Compare and Swap 的意思，比较并操作。很多的 CPU 直接支持 CAS 指令。
 * CAS 是一项乐观锁技术，当多个线程尝试使用 CAS 同时更新同一个变量时，只有其中一个线程能更新变量的值，而其它线程都失败，失败的线程并不会被挂起，而是被告知这次竞争中失败，并可以再次尝试。
 * CAS 操作包含三个操作数 —— 内存位置（V）、预期原值（A）和新值（B）。当且仅当预期值 A 和内存值 V 相同时，将内存值 V 修改为 B，否则什么都不做。
 * 在 CAS 操作中，会出现 ABA 问题。就是如果 V 的值先由 A 变成 B，再由 B 变成 A，那么仍然认为是发生了变化，并需要重新执行算法中的步骤。
 * 有简单的解决方案：不是更新某个引用的值，而是更新两个值，包括一个引用和一个版本号，即使这个值由 A 变为 B，然后 B 变为 A，版本号也是不同的。
 * AtomicStampedReference 和 AtomicMarkableReference 支持在两个变量上执行原子的条件更新。
 * AtomicStampedReference 更新一个 “对象-引用” 二元组，通过在引用上加上 “版本号”，从而避免 ABA 问题，
 * AtomicMarkableReference 将更新一个“对象引用-布尔值”的二元组。
 * @author Administrator
 *
 */
class ThreadSafeConcurrency {
    
    private static int CLIENT_COUNT = 5000;
    private static int THREAD_COUNT = 200;
    private static AtomicInteger count = new AtomicInteger(0);
    
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private final static Semaphore semaphore = new Semaphore(THREAD_COUNT);
    private final static CountDownLatch countDownLatch = new CountDownLatch(CLIENT_COUNT);
    
    public static void testAtomicInteger() throws Exception {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // count每加 1，进行减 1 计数
                countDownLatch.countDown();
            });
        }
        // 等待线程池所有任务执行结束
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("ConcurrencyDemo:" + count);
    }
    
    private static void add() {
        count.incrementAndGet();
    }
}
