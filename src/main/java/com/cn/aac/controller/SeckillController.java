package com.cn.aac.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cn.aac.entity.Seckill;
import com.cn.aac.service.SeckillService;
import com.cn.aac.utils.RedisUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

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
    
    @RequestMapping("/index3")
    public Object index3() {
        
        RuntimeSchema<String> schema = RuntimeSchema.createFrom(String.class);
        Seckill s = seckillService.getById(1000);
        byte[] key = ProtostuffIOUtil.toByteArray(s.getName(), schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        byte[] value = RedisUtil.ObjTOSerialize(Seckill.class, s);
        // 插入缓存
        redisUtil.setnx(key, value);
        
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    byte[] rs = redisUtil.get(key, 0);
                    System.out.println(RedisUtil.unserialize(Seckill.class, rs));
                    System.out.println("第" + index + "此执行线程：" + (System.currentTimeMillis() - start));
                }
            });
        }
        return "down";
    }
    
    @RequestMapping("/index4")
    public Object index4() {
        Long start = System.currentTimeMillis();
        // 从DB取1000次数据
        for (int i = 0; i < 1000000; i++) {
            int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Seckill s = seckillService.getById(1000);
                    System.out.println(s);
                    System.out.println("第" + index + "此执行线程：" + (System.currentTimeMillis() - start));
                }
            });
        }
        return "down";
    }
    
    /**
     * 事物并发测试 1W次
     * 当数据库等待时间过长时，会造成链接超时，
     * 多次测试10000次修改。时间80S。失败400次左右
     */
    @RequestMapping("/index5")
    public Object index5() {
        Long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    seckillService.doSeckill(1000L);
                    System.out.println("第" + index + "此执行线程：" + (System.currentTimeMillis() - start));
                }
            });
        }
        
        return "down";
    }
    
}
