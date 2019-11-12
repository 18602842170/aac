package com.cn.aac.synchronizedShow.ProducerConsumer;

public class Start {
    
    public static void main(String[] args) {
        // 仓库对象
        AbstractStorage abstractStorage = new Storage();
        
        // 生产者对象
        Producer p1 = new Producer(abstractStorage);
        Producer p2 = new Producer(abstractStorage);
        Producer p3 = new Producer(abstractStorage);
        
        // 消费者对象
        Consumer c1 = new Consumer(abstractStorage);
        Consumer c2 = new Consumer(abstractStorage);
        Consumer c3 = new Consumer(abstractStorage);
        
        // 设置生产者产品生产数量
        p1.setNum(10);
        p2.setNum(10);
        p3.setNum(10);
        
        // 设置消费者产品消费数量
        c1.setNum(30);
        c2.setNum(20);
        c3.setNum(40);
        
        // 线程开始执行
        c1.start();
        c2.start();
        c3.start();
        
        p1.start();
        p2.start();
        p3.start();
    }
}
