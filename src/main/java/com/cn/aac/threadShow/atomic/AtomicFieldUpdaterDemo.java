package com.cn.aac.threadShow.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 原子更新字段
 * 如果需原子地更新某个类里的某个字段时，就需要使用原子更新字段类，Atomic 包提供了以下 3 个类进行原子字段更新。
 * 
 * AtomicIntegerFieldUpdater：原子更新整型的字段的更新器。
 * AtomicLongFieldUpdater：原子更新长整型字段的更新器。
 * AtomicFieldUpdater
 * 
 * 要想原子地更新字段类需要两步。
 * 第一步，因为原子更新字段类都是抽象类，每次使用的时候必须使用静态方法 newUpdater() 创建一个更新器，并且需要设置想要更新的类和属性。
 * 第二步，更新类的字段（属性）必须使用 public volatile 修饰符。
 * @author Administrator
 *
 */
public class AtomicFieldUpdaterDemo {
    
    public static void main(String[] args) throws Exception {
        ThreadSafeConcurrencyFiled.testAtomicIntegerFieldUpdater();
    }
    
}

class ThreadSafeConcurrencyFiled {
    private static AtomicIntegerFieldUpdater<User> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");
    private static User user;
    
    /**
     * 字段的更新，就是将对象放入更新器，
     * 用更新器来原子的更新字段
     * @throws Exception
     */
    public static void testAtomicIntegerFieldUpdater() throws Exception {
        user = new User("user ", 100);
        atomicIntegerFieldUpdater.incrementAndGet(user);
        System.out.println("ConcurrencyDemo:" + atomicIntegerFieldUpdater.get(user));
        System.out.println("ConcurrencyDemo:" + user.getOld());
        
        //当传入的old符合CAS时更新
        atomicIntegerFieldUpdater.compareAndSet(user, 101, 120);
        // 等待线程池所有任务执行结束 只会打印更新的字段
        System.out.println("ConcurrencyDemo:" + atomicIntegerFieldUpdater.get(user));
        System.out.println("ConcurrencyDemo:" + user.getOld());
        
        user.old = 110;
        //当传入的user是同一个对象时update
        atomicIntegerFieldUpdater.updateAndGet(user, (old) -> {
            old += 10;
            return old;
        });
        // 等待线程池所有任务执行结束 只会打印更新的字段
        System.out.println("ConcurrencyDemo:" + atomicIntegerFieldUpdater.get(user));
        System.out.println("ConcurrencyDemo:" + user.getOld());
    }
}

class User {
    private String name;
    public volatile int old;//必须使用 volatile 标识，并且是 非 static
    
    public User(String name, int old) {
        this.name = name;
        this.old = old;
    }
    
    public String getName() {
        return name;
    }
    
    public int getOld() {
        return old;
    }
}
