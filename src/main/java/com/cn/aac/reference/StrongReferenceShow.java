package com.cn.aac.reference;

/**
 * java 4中引用详解
 * 强引用
 * @author Administrator
 *
 */
public class StrongReferenceShow {
    /**
     * 1、强引用（StrongReference）
     *  强引用是使用最普遍的引用。如果一个对象具有强引用，那垃圾回收器绝不会回收它。如下：
     */
    Object o = new Object();
    
    /**
     * 当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题。
     * 如果不使用时，要通过如下方式来弱化引用，如下：
     * @param args
     */
    public void strongReference1() {
        o = null;
    }
    
    /**
     * 显式地设置o为null，或超出对象的生命周期范围，则gc认为该对象不存在引用，这时就可以回收这个对象。具体什么时候收集这要取决于gc的算法。
     *  举例：
     *  上下两个例子中。 o 为全局变量。当赋值后，引用就会一直存在，因此需要手动o=null来弱化引用。
     *  o2为局部变量。当方法调用结束后，就是超出了其生命周期，因此就会回收。
     * @param args
     */
    public void strongReference2() {
        Object o2 = new Object();
    }
    
    /**
     * 所以：
     * 在一个方法的内部有一个强引用，这个引用保存在栈中，而真正的引用内容（Object）保存在堆中。当这个方法运行完成后就会退出方法栈，则引用内容的引用不存在，这个Object会被回收。
     * 但是如果这个o是全局的变量时，就需要在不用这个对象时赋值为null，因为强引用不会被垃圾回收。
     * 强引用在实际中有非常重要的用处，举个ArrayList的实现源代码：
     * 
     * private transient Object[] elementData;
     * public void clear() {
     *      modCount++;
     *      // Let gc do its work
     *      for (int i = 0; i < size; i++)
     *          elementData[i] = null;
     *      size = 0;
     *  }
     * 
     * 在ArrayList类中定义了一个私有的变量elementData数组，在调用方法清空数组时可以看到为每个数组内容赋值为null。
     * 不同于elementData=null，强引用仍然存在，避免在后续调用 add()等方法添加元素时进行重新的内存分配。
     * 使用如clear()方法中释放内存的方法对数组中存放的引用类型特别适用，这样就可以及时释放内存。 
     */
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
    }
    
}
