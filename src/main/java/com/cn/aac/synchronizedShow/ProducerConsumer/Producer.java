package com.cn.aac.synchronizedShow.ProducerConsumer;

public class Producer extends Thread {
    
    //每次生产的数量
    private int num;
    
    //所属的仓库
    public AbstractStorage abstractStorage;
    
    public Producer(AbstractStorage abstractStorage) {
        this.abstractStorage = abstractStorage;
        
    }
    
    public void setNum(int num) {
        
        this.num = num;
        
    }
    
    // 线程run函数
    
    @Override
    
    public void run()
    
    {
        
        try {
            produce(num);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    // 调用仓库Storage的生产函数
    
    public void produce(int num) throws InterruptedException
    
    {
        while (true) {
            // 3秒生产一次
            Thread.sleep(3000);
            abstractStorage.produce(num);
        }
        
    }
}
