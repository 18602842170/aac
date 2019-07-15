package com.cn.aac;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.aac.service.SeckillService;

public class SeckillServiceImplTest extends BaseTest {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SeckillService seckillService;
    
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    
    @Test
    public void testGetSeckillList() throws Exception {
        for (int i = 0; i < 10; i++) {
            int index = i;
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第" + index + "此执行线程：");
                    seckillService.jSeckill(1000L);
                }
            });
        }
    }
    
    //    @Test
    //    public void testGetById() throws Exception {
    //        long id = 1000;
    //        Seckill seckill = seckillService.getById(id);
    //        logger.info("seckill={}", seckill);
    //    }
    //    
    //    // 测试代码完整逻辑，注意可重复执行
    //    @Test
    //    public void testSeckillLogic() throws Exception {
    //        long id = 1001;
    //        Exposer exposer = seckillService.exportSeckillUrl(id);
    //        if (exposer.isExposed()) {
    //            logger.info("exposer={}", exposer);
    //            long phone = 13631231234L;
    //            String md5 = exposer.getMd5();
    //            try {
    //                SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
    //                logger.info("execution={}", execution);
    //            } catch (RepeatKillException e) {
    //                logger.error(e.getMessage());
    //            } catch (SeckillCloseException e) {
    //                logger.error(e.getMessage());
    //            }
    //        } else {
    //            // 秒杀未开启
    //            logger.error("exposer={}", exposer);
    //        }
    //    }
    
    //    @Test
    //    public void testExecuteSeckillProcedure() throws Exception {
    //        long seckillId = 1001;
    //        long phone = 13631231234L;
    //        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
    //        if (exposer.isExposed()) {
    //            String md5 = exposer.getMd5();
    //            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
    //            logger.info(execution.getStateInfo());
    //        }
    //    }
    
}
