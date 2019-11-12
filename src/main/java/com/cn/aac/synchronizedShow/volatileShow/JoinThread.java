package com.cn.aac.synchronizedShow.volatileShow;

import java.util.concurrent.atomic.AtomicInteger;

public class JoinThread extends Thread {
    
    /**
     * 因此，在使用volatile关键字时要慎重，并不是只要简单类型变量使用volatile修饰，对这个变量的所有操作都是原来操作，
     * 当变量的值由自身的上一个决定时，
     * 如n=n+1、n++ 等，volatile关键字将失效，只有当变量的值和自身上一个值无关时对该变量的操作才是原子级别的，
     * 如n = m + 1，这个就是原级别的。所以在使用volatile关键时一定要谨慎，
     * 如果自己没有把握，可以使用synchronized来代替volatile。 
     */
    
    public static volatile AtomicInteger n = new AtomicInteger(0);
    
    public void run() {
        for (int i = 0; i < 10; i++)
            try {
                n.addAndGet(1);
                sleep(3); //  为了使运行结果更随机，延迟3毫秒 
            } catch (Exception e) {
            }
    }
    
    public static void main(String[] args) throws Exception {
        
        Thread threads[] = new Thread[100];
        
        for (int i = 0; i < threads.length; i++)
            //  建立100个线程 
            threads[i] = new JoinThread();
        
        for (int i = 0; i < threads.length; i++)
            //  运行刚才建立的100个线程 
            threads[i].start();
        
        for (int i = 0; i < threads.length; i++)
            //  100个线程都执行完后继续 
            threads[i].join();
        
        System.out.println(" n= " + JoinThread.n);
    }
}
