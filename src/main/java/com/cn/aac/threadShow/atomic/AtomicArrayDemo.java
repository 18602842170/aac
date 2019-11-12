package com.cn.aac.threadShow.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * 原子更新数组
 * 通过原子的方式更新数组里的某个元素，Atomic 包提供了以下 3 个类。
 * AtomicIntegerArray
 * AtomicLongArray
 * AtomicReferenceArray
 * @author Administrator
 *
 */
public class AtomicArrayDemo {
    
    public static void main(String[] args) throws Exception {
        //        NotThreadSafeConcurrencyArray.testAtomicIntArray();
        
        ThreadSafeConcurrencyArray.testAtomicIntegerArray();
    }
    
}

class NotThreadSafeConcurrencyArray {
    
    private static int CLIENT_COUNT = 5000;
    private static int THREAD_COUNT = 200;
    private static int[] values = new int[11];
    
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private final static Semaphore semaphore = new Semaphore(THREAD_COUNT);
    private final static CountDownLatch countDownLatch = new CountDownLatch(CLIENT_COUNT);
    
    public static void testAtomicIntArray() throws Exception {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    for (int j = 0; j < 10; j++) {// 所有元素+1
                        values[j]++;
                    }
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
        for (int i = 0; i < 10; i++) {
            System.out.print(values[i] + " ");
        }
        
    }
}

class ThreadSafeConcurrencyArray {
    
    private static int CLIENT_COUNT = 5000;
    private static int THREAD_COUNT = 200;
    private static int[] values = new int[10];
    private static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(values);
    
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private final static Semaphore semaphore = new Semaphore(THREAD_COUNT);
    private final static CountDownLatch countDownLatch = new CountDownLatch(CLIENT_COUNT);
    
    public static void testAtomicIntegerArray() throws Exception {
        for (int i = 0; i < CLIENT_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    for (int j = 0; j < 10; j++) {// 所有元素+1
                        atomicIntegerArray.incrementAndGet(j);
                    }
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
        for (int i = 0; i < 10; i++) {
            System.out.print(atomicIntegerArray.get(i) + " ");
        }
        
    }
}
