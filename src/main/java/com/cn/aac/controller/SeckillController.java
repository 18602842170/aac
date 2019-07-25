package com.cn.aac.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.aac.entity.Seckill;
import com.cn.aac.service.SeckillService;
import com.cn.aac.utils.RedisUtil;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    
    @Autowired
    SeckillService seckillService;
    
    @Autowired
    RedisUtil redisUtil;
    
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1000);
    
    @RequestMapping("/index")
    public Object index() {
        System.out.println(System.currentTimeMillis());
        for (int i = 0; i < 100; i++) {
            int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第" + index + "此执行线程：");
                    seckillService.jSeckill(1000L);
                }
            });
        }
        return "down";
    }
    
    @RequestMapping("/index2")
    public Object index2() {
        System.out.println(System.currentTimeMillis());
        Seckill s = seckillService.getById(1000);
        byte[] bs = RedisUtil.ObjTOSerialize(Seckill.class, s);
        System.out.println(bs);
        Seckill rs = RedisUtil.unserialize(Seckill.class, bs);
        System.out.println(rs);
        
        return "down";
    }
    
    /**
     * 查询并发测试。redis查询
     * @return
     */
    @RequestMapping("/index3")
    public Object index3() {
        Seckill s;
        byte[] key = RedisUtil.ObjTOSerialize(String.class, "1000元秒杀iphone6");
        byte[] rs = redisUtil.get(key, 0);
        s = RedisUtil.unserialize(Seckill.class, rs);
        if (s == null) {
            s = seckillService.getById(1000);
            byte[] value = RedisUtil.ObjTOSerialize(Seckill.class, s);
            // 插入缓存
            redisUtil.setnx(key, value);
        }
        System.out.println(s);
        return s;
    }
    
    /**
     * 查询并发测试。数据库查询
     * @return
     */
    @RequestMapping("/index4")
    public Object index4() {
        Seckill s = seckillService.getById(1000);
        System.out.println(s);
        return s;
    }
    
    /**
     * 事物并发测试 
     * 当数据库等待时间过长时，会造成链接超时，
     * 多次测试1000次。时间11S左右。88.4/s
     */
    @RequestMapping("/index5")
    public Object index5() {
        return seckillService.doSeckill();
    }
    
    /**
     * 异步并发设置库存
     * @return
     */
    @RequestMapping("/index6/setNumber")
    public Object index6SetNumber() {
        return seckillService.setNumber();
    }
    
    /**
     * 异步并发
     * @return
     */
    @RequestMapping("/index6")
    public Object index6() {
        return seckillService.doSeckillByRedis();
    }
    
    /**
     * 异步并发，秒杀结束
     * @return
     */
    @RequestMapping("/index6/over")
    public Object index6over() {
        return seckillService.over();
    }
}
