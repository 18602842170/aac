package com.cn.aac.singo;

import java.time.LocalDateTime;

public class TimerRunTime {
    //任务时间点
    private int year;
    //任务时间点
    private int month;
    //任务时间点
    private int day;
    //任务时间点
    private int weekDay;
    //任务时间点
    private int hour;
    //任务时间点
    private int min;
    //任务时间点
    private LocalDateTime lastRunTime;
    
    public TimerRunTime() {
    }
    
    public TimerRunTime(int year, int month, int day, int weekDay, int hour, int min) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.weekDay = weekDay;
        this.hour = hour;
        this.min = min;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int month) {
        this.month = month;
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
    }
    
    public int getWeekDay() {
        return weekDay;
    }
    
    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }
    
    public int getHour() {
        return hour;
    }
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    public int getMin() {
        return min;
    }
    
    public void setMin(int min) {
        this.min = min;
    }
    
    public LocalDateTime getLastRunTime() {
        return lastRunTime;
    }
    
    public void setLastRunTime(LocalDateTime lastRunTime) {
        this.lastRunTime = lastRunTime;
    }
    
}
