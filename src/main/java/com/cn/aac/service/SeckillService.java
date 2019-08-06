package com.cn.aac.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.cn.aac.dao.SeckillDao;
import com.cn.aac.dao.SuccessKilledDao;
import com.cn.aac.dto.Exposer;
import com.cn.aac.dto.SeckillExecution;
import com.cn.aac.entity.Seckill;
import com.cn.aac.entity.SuccessKilled;
import com.cn.aac.enums.SeckillStateEnum;
import com.cn.aac.exception.RepeatKillException;
import com.cn.aac.exception.SeckillCloseException;
import com.cn.aac.exception.SeckillException;
import com.cn.aac.singo.ThreadPoolUtils;
import com.cn.aac.utils.RedisUtil;

//@Componet @Service @Dao @Controller
@Service
public class SeckillService {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    // 注入Service依赖
    @Autowired
    private SeckillDao seckillDao;
    
    @Autowired
    private SuccessKilledDao successKilledDao;
    
    @Autowired
    RedisUtil redisUtil;
    
    // md5盐值字符串，用于混淆MD5
    private final static String slat = "skdfjksjdf7787%^%^%^FSKJFK*(&&%^%&^8DF8^%^^*7hFJDHFJ";
    
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }
    
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }
    
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    
    //    @Transactional
    public void jSeckill(long seckillId) {
        Date now = new Date();
        int count = seckillDao.reduceNumber(seckillId, now);
        System.out.println(count);
        System.out.println(System.currentTimeMillis());
    }
    
    public Exposer exportSeckillUrl(long seckillId) {
        // 优化点：缓存优化：超时的基础上维护一致性
        // 1.访问redis
        //        Seckill seckill = redisDao.getSeckill(seckillId);
        //        if (seckill == null) {
        // 2.访问数据库
        Seckill seckill = seckillDao.queryById(seckillId);
        //            if (seckill == null) {
        //                return new Exposer(false, seckillId);
        //            } else {
        //                // 3.访问redis
        //                redisDao.putSeckill(seckill);
        //            }
        //        }
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }
    
    @Transactional
    public String doSeckill() {
        Seckill s;
        byte[] key = RedisUtil.ObjTOSerialize(String.class, "1000元秒杀iphone6");
        byte[] rs = redisUtil.get(key, 0);
        s = RedisUtil.unserialize(Seckill.class, rs);
        if (s == null) {
            s = seckillDao.queryById(1000);
            byte[] value = RedisUtil.ObjTOSerialize(Seckill.class, s);
            // 插入缓存
            redisUtil.setnx(key, value);
        }
        String message = "";
        // 生成随机电话
        long number = (long) (1 + Math.random() * (18602842171L));
        // 记录购买行为
        int insertCount = successKilledDao.insertSuccessKilled(1000, number);
        message += insertCount > 0 ? "记录生成成功，" : "记录生成失败";
        // 减少库存
        int updateCount = seckillDao.reduceNumber(1000, new Date());
        message += updateCount > 0 ? "库存扣除成功，" : "库存扣除失败";
        
        return message;
    }
    
    public String setNumber() {
        String key = "1000元秒杀iphone6库存";
        Seckill s = seckillDao.queryById(1000);
        String number = "" + s.getNumber();
        //         插入缓存
        redisUtil.set(key, number, 0);
        String key2 = "1000元秒杀iphone6数量";
        redisUtil.set(key2, "0", 0);
        return "库存初始化成功";
    }
    
    /**
     * 先从redis中检验库存。然后减少Redis的库存
     * @return
     */
    public String doSeckillByRedis() {
        String key = "1000元秒杀iphone6库存";
        String numstr = redisUtil.get(key, 0);
        int number = Integer.parseInt(numstr == null ? "0" : numstr);
        String message = "";
        
        String key2 = "1000元秒杀iphone6数量";
        // 拿到数据后减少库存
        if (number >= 0) {
            // 缓存中减少库存
            redisUtil.decrBy(key, 1L);
            String numstr2 = redisUtil.get(key, 0);
            int number2 = Integer.parseInt(numstr2 == null ? "0" : numstr2);
            if (number2 < 0) {
                redisUtil.decrBy(key, -1L);
                message += "秒杀结束";
            } else {
                message += "库存减少成功,";
                redisUtil.decrBy(key2, -1L);
                insterRecod();
            }
        } else {
            message += "秒杀结束";
        }
        return message;
    }
    
    /**
     * 使用异步线程插入购买记录
     * 当分布式处理时。此处可采用消息队列处理
     */
    public void insterRecod() {
        ThreadPoolUtils.getInstance().getThreadPool().execute(() -> {
            // 生成随机电话
            long number = (long) (1 + Math.random() * (18602842171L));
            try {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(1000, number);
            } catch (Exception e) {
                insterRecod();
            }
        });
    }
    
    /**
     * 修改库存
     * @return
     */
    public String over() {
        String key = "1000元秒杀iphone6库存";
        String number = redisUtil.get(key, 0);
        System.out.println(number);
        String key2 = "1000元秒杀iphone6数量";
        String number2 = redisUtil.get(key2, 0);
        System.out.println(number2);
        // 库存为0时，修改表
        int updateCount = seckillDao.reduceNumber(1000, new Date());
        return "库存更新完成:" + updateCount;
    }
    
    @Transactional
    /**
     * 使用注解控制事务方法的优点： 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date now = new Date();
        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 唯一：seckillId,userPhone
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                if (updateCount <= 0) {
                    // 没有更新到记录 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常转换为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
    
    //    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
    //        if (md5 == null || !md5.equals(getMD5(seckillId))) {
    //            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
    //        }
    //        Date killTime = new Date();
    //        Map<String, Object> map = new HashMap<String, Object>();
    //        map.put("seckillId", seckillId);
    //        map.put("phone", userPhone);
    //        map.put("killTime", killTime);
    //        map.put("result", null);
    //        // 执行存储过程，result被赋值
    //        try {
    //            seckillDao.killByProcedure(map);
    //            // 获取result
    //            int result = MapUtils.getInteger(map, "result", -2);
    //            if (result == 1) {
    //                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
    //                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
    //            } else {
    //                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
    //            }
    //        } catch (Exception e) {
    //            logger.error(e.getMessage(), e);
    //            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
    //        }
    //    }
    
}
