package com.cn.aac.singo;

import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此调度线程每分钟运行一次。
 * 调度多个定时任务线程的启动
 * @author Administrator
 *
 */
public class AccTimerDispatchStation extends TimerTask {
    
    protected static final Logger logger = LoggerFactory.getLogger(AccTimerDispatchStation.class);
    
    @Override
    public void run() {
        LocalDateTime time = LocalDateTime.now();
        // 获取所有的任务实例
        logger.info("寻找可执行任务");
        ConcurrentHashMap<String, AccTimerTask> taskmap = TimerManager.getInstance().getAccTimerTasks();
        if (taskmap != null) {
            taskmap.keySet().forEach((accTimerTaskKey) -> {
                ThreadPoolUtils.getInstance().getThreadPool().execute(() -> {
                    // 单个定时任务在线程中计算是否应该当前执行
                    try {
                        taskmap.get(accTimerTaskKey).calculateNextTime(time);
                    } catch (Exception e) {
                        throw (new TimerManagerRunTimeRuntimeException("定时任务获取失败，任务ID:" + accTimerTaskKey, e));
                    }
                });
            });
        }
    }
    
}
