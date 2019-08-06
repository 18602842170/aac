package com.cn.aac.singo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class job2 implements TimerRunnerFunctionalInterface {
    
    protected static final Logger logger = LoggerFactory.getLogger(job2.class);
    
    @Override
    public Boolean apply(AccTimerTask t) {
        logger.info("job2执行了（" + t.getTaskID() + "）：" + t.getThisTimeCount() + "次；成功执行了：" + t.getRunSuccessCount() + "次");
        return true;
    }
    
}
