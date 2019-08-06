package com.cn.aac.singo;

/**
 * 
 * @author Administrator
 *
 * @param <T> 方法接收参数
 * @param <R> 返回类型
 */
@FunctionalInterface
public interface TimerRunnerFunctionalInterface {
    Boolean apply(AccTimerTask t);
}
