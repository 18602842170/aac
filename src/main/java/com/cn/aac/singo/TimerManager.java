package com.cn.aac.singo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerManager {
    
    private volatile static TimerManager instance;
    
    private TimerManager() {
    }
    
    public static TimerManager getInstance() {
        if (instance == null) {
            synchronized (TimerManager.class) {
                if (instance == null) {
                    return instance = new TimerManager();
                }
            }
        }
        return instance;
    }
    
    private ScheduledFuture<AccTimerDispatchStation> accTimerDispatchStation;
    
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    
    //采用以任务ID为key的哈希表存储所有实现的定时任务
    private ConcurrentHashMap<String, AccTimerTask> taskmap = new ConcurrentHashMap<String, AccTimerTask>();
    
    protected static final Logger logger = LoggerFactory.getLogger(TimerManager.class);
    
    public void RunTimerManager() {
        AccTimerDispatchStation accTimerDispatchStation = new AccTimerDispatchStation();
        
        if (this.accTimerDispatchStation != null) {
            throw (new TimerManagerRunTimeRuntimeException("任务调度已经启动，请勿重复启动"));
        }
        // 开始调度任务每分钟执行一次
        try {
            this.accTimerDispatchStation = (ScheduledFuture<AccTimerDispatchStation>) this.service.scheduleAtFixedRate(accTimerDispatchStation, 0, 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw (new TimerManagerRunTimeRuntimeException("任务调度启动失败", e));
        }
    }
    
    /**
     * 任务添加
     * @param id
     * @param task
     */
    private void addTask(String id, AccTimerTask task) {
        taskmap.put(id, task);
    }
    
    /**
     * 将任务放入管理器
     * @param TimerRunnerFunctionalInterfaceClassName
     * @param timerRunTime
     * @return
     */
    public String starAccTimerTask(String TimerRunnerFunctionalInterfaceClassName, TimerRunTime timerRunTime) {
        TimerRunnerFunctionalInterface runFunction = null;
        try {
            Class<TimerRunnerFunctionalInterface> clz = (Class<TimerRunnerFunctionalInterface>) Class.forName(TimerRunnerFunctionalInterfaceClassName);
            runFunction = clz.newInstance();
        } catch (Exception e) {
            throw (new TimerManagerRunTimeRuntimeException("任务实例化失败", e));
        }
        // 创建任务
        AccTimerTask timerTask = new AccTimerTask(runFunction, timerRunTime);
        // 将任务放入map
        this.addTask(timerTask.getTaskID(), timerTask);
        
        return timerTask.getTaskID();
        
    }
    
    /**
     * 任务获取所有实例
     * @param taskId
     * @return
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public ConcurrentHashMap<String, AccTimerTask> getAccTimerTasks() {
        return this.taskmap;
    }
    
    /**
     * 任务终止
     * @param taskId 任务ID
     * @param mayInterruptIfRunning 
     *  当值为true时，强行终端任务。无论任务是否正在进行
     *  当值为false时，等待当前运行完成后终止
     * @return
     */
    public AccTimerTask cancelTask(String taskId) {
        return taskmap.remove(taskId);
    }
    
}
