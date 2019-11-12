package com.cn.aac.synchronizedShow.ProducerConsumer;

public interface AbstractStorage {
    
    void consume(int num);
    
    void produce(int num);
    
}
