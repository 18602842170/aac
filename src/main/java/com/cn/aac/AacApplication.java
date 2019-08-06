package com.cn.aac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cn.aac.singo.TimerManager;
import com.cn.aac.singo.TimerRunTime;

@SpringBootApplication
@MapperScan({ "com.cn.aac.dao" })
public class AacApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AacApplication.class, args);
        // 放入定时任务进任务管理器
        TimerRunTime runTime = new TimerRunTime(2019, 8, 6, 0, 0, 0);
        TimerManager.getInstance().starAccTimerTask("com.cn.aac.singo.job1", runTime);
        // 启动定时任务调度器
        TimerManager.getInstance().RunTimerManager();
    }
    
}
