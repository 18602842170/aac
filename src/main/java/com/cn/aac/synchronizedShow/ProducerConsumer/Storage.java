package com.cn.aac.synchronizedShow.ProducerConsumer;

import java.util.LinkedList;

public class Storage implements AbstractStorage {
    //仓库最大容量
    private final int MAX_SIZE = 100;
    //仓库存储的载体
    private LinkedList<Object> list = new LinkedList<Object>();
    
    //生产产品
    public void produce(int num) {
        //同步
        synchronized (list) {
            //仓库剩余的容量不足以存放即将要生产的数量，暂停生产
            while (list.size() + num > MAX_SIZE) {
                System.out.println("【要入仓库的数量】:" + num + "\t【库存量】:" + list.size() + "\t暂时不能放入仓库!");
                
                try {
                    //条件不满足，生产阻塞
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            for (int i = 0; i < num; i++) {
                list.add(new Object());
            }
            
            System.out.println("【已入仓库的数量】:" + num + "\t【现仓储量为】:" + list.size());
            
            list.notifyAll();
        }
    }
    
    //消费产品
    public void consume(int num) {
        synchronized (list) {
            
            //不满足消费条件
            while (num > list.size()) {
                System.out.println("【要消费的产品数量】:" + num + "\t【库存量】:" + list.size() + "\t暂时不能执行消费任务!");
                
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            //消费条件满足，开始消费
            for (int i = 0; i < num; i++) {
                list.remove();
            }
            
            System.out.println("【已经消费产品数】:" + num + "\t【现仓储量为】:" + list.size());
            
            list.notifyAll();
        }
    }
}
