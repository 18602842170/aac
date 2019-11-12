package com.cn.aac.synchronizedShow.volatileShow;

public class volatileShow {
    
    public static void main(String[] args) throws InterruptedException {
        
        StoppableTask t1 = new StoppableTask();
        
        System.out.println("开始运行！2秒后修改FLAG");
        t1.start();
        
        Thread.sleep(2000);
        // 停止代码
        t1.tellMeToStop();
        
    }
    
}
