package com.cn.aac.synchronizedShow;

public class synchronizedShow {
    
    public static void main(String[] args) {
        CountMangerLock lock = new CountMangerLock(1);
        //        Object lock2 = new Object();
        
        Thread thread1 = new Thread(new OutputThread(1, lock));
        Thread thread2 = new Thread(new OutputThread(2, lock));
        
        thread1.start();
        thread2.start();
    }
}

class CountMangerLock {
    
    public int count;
    
    public CountMangerLock(int start) {
        this.count = start;
    }
    
    public void selfIncrease() {
        count++;
    }
    
    @Override
    public String toString() {
        return "" + count;
    }
}

class OutputThread implements Runnable {
    
    private int num;
    private CountMangerLock lock;
    
    public OutputThread(int num, CountMangerLock lock) {
        super();
        this.num = num;
        this.lock = lock;
    }
    
    public void run() {
        int count = 1;
        try {
            while (count <= 10) {
                /**
                 * 对象的变化就会造成唤醒的失败。
                 * 简单理解就是锁不一样了。
                 * 
                 * 如果lock换成 integer类。
                 * 当一个线程执行自增长之后。 lock 从1变为2。由于引用常量，地址值编号，就是对象换了。
                 * 所以1对象的wait()就不能被2对象的notify所唤醒。
                 */
                synchronized (lock) {
                    Thread.sleep(500);
                    lock.notifyAll();
                    //                    lock.notify();
                    lock.wait();
                    System.out.println("第" + num + "线程运行第" + count + "次。Lock值：" + lock);
                    lock.selfIncrease();
                    count++;
                }
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
