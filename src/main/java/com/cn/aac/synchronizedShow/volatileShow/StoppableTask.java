package com.cn.aac.synchronizedShow.volatileShow;

public class StoppableTask extends Thread {
    private boolean pleaseStop;
    
    public void run() {
        
        while (!pleaseStop) {
            System.out.println("开始运行！");
            try {
                System.out.println("修改booelan为true！");
                pleaseStop = true;
                System.out.println("等待5秒！");
                Thread.sleep(5000);
                System.out.println("等待结束！");
                System.out.println("booelan为" + pleaseStop);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        System.out.println("停止运行！");
    }
    
    public void tellMeToStop() {
        
        this.pleaseStop = false;
        System.out.println("修改flag为false！");
        
    }
}
