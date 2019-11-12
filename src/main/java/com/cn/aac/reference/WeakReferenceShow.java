package com.cn.aac.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * java 4中引用详解
 * 弱引用
 * @author Administrator
 *
 */
public class WeakReferenceShow {
    /**
     * 弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。
     * 在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。
     * 不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
     * 
     * String str=new String("abc");    
     * WeakReference<String> abcWeakRef = new WeakReference<String>(str);
     * str=null;
     * 
     * 当垃圾回收器进行扫描回收时等价于：   
     * str = null;
     * System.gc();
     * 
     * 如果这个对象是偶尔的使用，并且希望在使用时随时就能获取到，但又不想影响此对象的垃圾收集，那么你应该用 Weak Reference 来记住此对象。
     * 
     * 下面的代码会让str再次变为一个强引用：   
     * String  abc = abcWeakRef.get();
     * 
     * 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
     * 当你想引用一个对象，但是这个对象有自己的生命周期，你不想介入这个对象的生命周期，这时候你就是用弱引用。
     * 这个引用不会在对象的垃圾回收判断中产生任何附加的影响
     * 
     * 弱引用技术主要适用于实现无法防止其键（或值）被回收的规范化映射。
     * 另外，弱引用分为“短弱引用（Short Week Reference）”和“长弱引用（Long Week Reference）”，
     * 其区别是长弱引用在对象的Finalize方法被GC调用后依然追踪对象。基于安全考虑，不推荐使用长弱引用。因此建议使用下面的方式创建对象的弱引用。
     * 
     * WeakReference wr = new WeakReference(obj); 
     * WeakReference wr = new WeakReference(obj, false); 
     */
    
    private static ReferenceQueue<VeryBig> rq = new ReferenceQueue<VeryBig>();
    
    /**
     * 查询队列中的引用
     */
    public static void checkQueue() {
        Reference<? extends VeryBig> ref = null;
        while ((ref = rq.poll()) != null) {
            if (ref != null) {
                /**
                 * 此时。VeryBig引用已经被gc回收。所以为null。id之所以有。是因为弱引用对象VeryBigWeakReference中存放了id
                 */
                System.out.println("In queue: " + ((VeryBigWeakReference) (ref)).id);
                System.out.println("In queue: " + ((VeryBigWeakReference) (ref)).get());
            }
        }
    }
    
    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        int size = 3;
        LinkedList<WeakReference<VeryBig>> weakList = new LinkedList<WeakReference<VeryBig>>();
        for (int i = 0; i < size; i++) {
            weakList.add(new VeryBigWeakReference(new VeryBig("weak" + i), rq));
            System.out.println("Just created weak: " + weakList.getLast());
        }
        
        System.gc();
        try { // 下面休息几秒，让上面的垃圾回收线程运行完成
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkQueue();
    }
    
}
