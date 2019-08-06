package com.cn.aac.singo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例线程池
 * @author Administrator
 *
 */
public class ThreadPoolUtils {
    
    private static ExecutorService cachedThreadPool;
    
    private ThreadPoolUtils() {
        //手动创建线程池. 
        cachedThreadPool = Executors.newCachedThreadPool();
    }
    
    private static class PluginConfigHolder {
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }
    
    public static ThreadPoolUtils getInstance() {
        return PluginConfigHolder.INSTANCE;
    }
    
    public ExecutorService getThreadPool() {
        return cachedThreadPool;
    }
}
