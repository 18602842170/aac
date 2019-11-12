package com.cn.aac.synchronizedShow.ProducerConsumer;

public class Consumer extends Thread {
    
    // 每次消费的产品数量
    private int num;
    
    // 所在放置的仓库
    private AbstractStorage abstractStorage1;
    
    // 构造函数，设置仓库
    public Consumer(AbstractStorage abstractStorage1)
    
    {
        
        this.abstractStorage1 = abstractStorage1;
        
    }
    
    // 线程run函数
    
    public void run()
    
    {
        
        try {
            consume(num);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    // 调用仓库Storage的生产函数
    
    public void consume(int num) throws InterruptedException
    
    {
        while (true) {
            // 10秒消费一次
            Thread.sleep(10000);
            abstractStorage1.consume(num);
        }
        
    }
    
    public void setNum(int num) {
        
        this.num = num;
        
    }
}
