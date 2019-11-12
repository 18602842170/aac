package com.cn.aac.threadShow.atomic;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子更新引用
 * 原子更新基本类型的 AtomicInteger，只能更新一个变量，如果要原子更新多个变量，就需要使用这个原子更新引用类型提供的类。Atomic 包提供了以下 3 个类。
 * AtomicReference：原子更新引用类型。
 * AtomicReferenceFieldUpdater：原子更新引用类型里的字段。
 * AtomicMarkableReference：原子更新带有标记位的引用类型。
 * AtomicStampedReference:原子更新带有版本号的引用类型
 * @author Administrator
 *
 */
public class AtomicReferenceDemo {
    
    private static AtomicReference<Integer> atomicRef = new AtomicReference<Integer>(0);
    
    /**
     * 原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
     */
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(0, 0);
    
    /**
     * 带boolean的原子引用更新。原理同时，把版本号换为了boolean值
     */
    private static AtomicMarkableReference<Integer> atomicMarkableReference = new AtomicMarkableReference(0, false);
    
    public static void main(String[] args) throws Exception {
        testAtomicReference();
    }
    
    private static void testAtomicReference() throws Exception {
        atomicRef.compareAndSet(0, 2);
        atomicRef.compareAndSet(0, 1);
        atomicRef.compareAndSet(1, 3);
        atomicRef.compareAndSet(2, 4);
        atomicRef.compareAndSet(3, 5);
        System.out.println("ConcurrencyDemo:" + atomicRef.get().toString());
        
        /**
         * 带版本号的原子引用类型
         * 当原值与原版本都符合时才进行变更
         */
        int stamp = atomicStampedReference.getStamp();
        Integer reference = atomicStampedReference.getReference();
        System.out.println(reference + "============" + stamp);
        atomicStampedReference.compareAndSet(0, 10, 0, 1);
        atomicStampedReference.compareAndSet(0, 10, 0, 1);
        atomicStampedReference.compareAndSet(10, 15, 1, 2);
        
        System.out.println(atomicStampedReference.getReference() + "============" + atomicStampedReference.getStamp());
        
        /**
         * 带booelan值的原子引用类型
         * 当原值与booelan值都符合时才进行变更
         */
        System.out.println(atomicMarkableReference.getReference());
        atomicMarkableReference.compareAndSet(0, 10, false, true);
        atomicMarkableReference.compareAndSet(10, 5, true, true);
        atomicMarkableReference.compareAndSet(5, 15, false, true);
        
        System.out.println(atomicMarkableReference.getReference());
    }
    
}
