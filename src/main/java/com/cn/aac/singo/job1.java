package com.cn.aac.singo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class job1 implements TimerRunnerFunctionalInterface {
    
    protected static final Logger logger = LoggerFactory.getLogger(job1.class);
    
    @Override
    public Boolean apply(AccTimerTask t) {
        logger.info("job1执行了（" + t.getTaskID() + "）：" + t.getThisTimeCount() + "次；成功执行了：" + t.getRunSuccessCount() + "次");
        return true;
    }
    
}
