package com.cn.aac.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cn.aac.singo.AccTimerTask;
import com.cn.aac.singo.TimerManager;
import com.cn.aac.singo.TimerRunTime;

@RestController
@RequestMapping("/timer")
public class TimerController {
    
    @RequestMapping("/index")
    public Object index() {
        // 放入定时任务进任务管理器
        TimerRunTime runTime = new TimerRunTime(2019, 8, 7, 0, 0, 0);
        TimerManager.getInstance().starAccTimerTask("com.cn.aac.singo.job2", runTime);
        // 放入定时任务进任务管理器
        TimerRunTime runTime2 = new TimerRunTime(2019, 8, 7, 0, 0, 5);
        TimerManager.getInstance().starAccTimerTask("com.cn.aac.singo.job2", runTime2);
        return "down";
    }
    
    @RequestMapping("/index2")
    public Object index2() throws InterruptedException {
        
        System.out.println("Start:");
        // 此处直接传递本类中的方法。方法要符合需要的参数类型
        //        String taskId1 = TimerManager.getInstance().StartTimer(0, 1, TimeUnit.SECONDS, this::taskRunnuer);
        //        String taskId2 = TimerManager.getInstance().StartTimer(0, 1, TimeUnit.SECONDS, (AccTimerTask runtask) -> {
        //            System.out.println("任务：" + runtask.getTaskID() + "执行了：" + runtask.getRuntimeCount() + "次");
        //            return true;
        //        });
        //        Thread.sleep(2000);
        
        // 任务终止
        //        System.out.println("任务：" + taskId1 + "终止：" + TimerManager.getInstance().cancelTask(taskId1, true));
        //        
        //        Thread.sleep(5000);
        //        
        //        // 任务终止
        //        System.out.println("任务：" + taskId1 + "终止：" + TimerManager.getInstance().cancelTask(taskId2, true));
        
        return "down";
    }
    
    // 就可实现线程中的事物操作
    @Transactional
    public Boolean taskRunnuer(AccTimerTask runtask) {
        //        System.out.println("任务：" + runtask.getTaskID() + "执行了：" + runtask.getRuntimeCount() + "次");
        return true;
    }
    
    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * 
     * @return
     */
    @PostMapping(value = "/testpost")
    @ResponseBody
    public Object testpost() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "1000000");
        map.put("msg", "未登录");
        return map;
    }
    
}
