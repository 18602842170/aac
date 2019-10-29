package com.cn.aac.singo;

import java.time.LocalDateTime;

/**
 * Timer timer = new Timer();
 * AccTimerTask task = new AccTimerTask("asdsadas", 20);
 * timer.schedule(task, 0, 1000);
 * 当此任务被实例化，然后调用时，任务执行周期是 1000 此参数。单位毫秒
 * 因此。runtimeCount 可获获取任务的执行次数
 * runtime 是为了实现倒计时而用的变量。和实际的任务执行之间。没有必然联系
 * @author Administrator
 *
 */
public class AccTimerTask extends TimerRunTime {
    //任务ID
    private String taskID;
    //任务执行次数
    private int thisTimeCount;
    //任务执行成功次数
    private long runSuccessCount;
    
    //任务执行体
    private TimerRunnerFunctionalInterface runFunction;
    
    /**
     * 此参数传递任务执行本体
     * @param runFunction2
     */
    public AccTimerTask(TimerRunnerFunctionalInterface runFunction, TimerRunTime timerRunTime) {
        super();
        // 构造方法中生成随机任务ID
        this.taskID = UniqueIdGenerator.getUniqueId();
        this.thisTimeCount = 0;
        this.runSuccessCount = 0;
        this.runFunction = runFunction;
        setYear(timerRunTime.getYear());
        setMonth(timerRunTime.getMonth());
        setDay(timerRunTime.getDay());
        setWeekDay(timerRunTime.getWeekDay());
        setHour(timerRunTime.getHour());
        setMin(timerRunTime.getMin());
    }
    
    public void run() {
        // 执行次数
        thisTimeCount++;
        try {
            // 保存成功信息
            if (runFunction.apply(this)) {
                this.runSuccessCount++;
            }
        } finally {
        }
    }
    
    /**
     * 
     * @param thisTime 传入的当前时间
     * @return
     */
    public void calculateNextTime(LocalDateTime nowTime) {
        // 当执行时间信息不为0时，并且传入时间不符合。退出方法
        if (getYear() != 0 && getYear() != nowTime.getYear()) {
            return;
        }
        if (getMonth() != 0 && getMonth() != nowTime.getMonthValue()) {
            return;
        }
        if (getDay() != 0 && getDay() != nowTime.getDayOfMonth()) {
            return;
        }
        if (getWeekDay() != 0 && getWeekDay() != nowTime.getDayOfWeek().getValue()) {
            return;
        }
        if (getHour() != 0 && getHour() != nowTime.getHour()) {
            return;
        }
        if (getMin() != 0 && getMin() != nowTime.getMinute()) {
            return;
        }
        // 符合所有条件
        // 执行任务
        try {
            setLastRunTime(nowTime);
            run();
        } catch (Exception e) {
            throw new TimerManagerRunTimeRuntimeException("定时任务执行错误", e);
        }
    }
    
    public String getTaskID() {
        return taskID;
    }
    
    public long getRunSuccessCount() {
        return runSuccessCount;
    }
    
    public long getThisTimeCount() {
        return thisTimeCount;
    }
    
}
