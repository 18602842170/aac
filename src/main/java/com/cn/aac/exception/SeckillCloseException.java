package com.cn.aac.exception;

/**
 * 秒杀关闭异常
 * 
 * @author 李奕锋
 */
public class SeckillCloseException extends SeckillException {
    
    public SeckillCloseException(String message) {
        super(message);
    }
    
    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
