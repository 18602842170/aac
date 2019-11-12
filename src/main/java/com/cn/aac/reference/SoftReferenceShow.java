package com.cn.aac.reference;

import java.lang.ref.SoftReference;

/**
 * java 4中引用详解
 * 软引用
 * @author Administrator
 *
 */
public class SoftReferenceShow {
    
    /**
     * 如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它；
     * 如果内存空间不足了，就会回收这些对象的内存。
     * 只要垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存。
     * 
     *  String str=new String("abc");                                     // 强引用
     *  SoftReference<String> softRef=new SoftReference<String>(str);     // 软引用
     *  当内存不足时，等价于： 
     *  If(JVM.内存不足()) {
     *      str = null;  // 转换为软引用
     *      System.gc(); // 垃圾回收器进行回收
     *  }
     * 
     * 
     * 软引用在实际中有重要的应用，例如浏览器的后退按钮。按后退时，这个后退时显示的网页内容是重新进行请求还是从缓存中取出呢？这就要看具体的实现策略了。
     *  （1）如果一个网页在浏览结束时就进行内容的回收，则按后退查看前面浏览过的页面时，需要重新构建
     *  （2）如果将浏览过的网页存储到内存中会造成内存的大量浪费，甚至会造成内存溢出
     *  
     * 这时候就可以使用软引用
     *  Browser prev = new Browser();               // 获取页面进行浏览
     *  SoftReference sr = new SoftReference(prev); // 浏览完毕后置为软引用        
     *  if(sr.get()!=null){ 
     *      rev = (Browser) sr.get();           // 还没有被回收器回收，直接获取
     *  }else{
     *      prev = new Browser();               // 由于内存吃紧，所以对软引用的对象回收了
     *      sr = new SoftReference(prev);       // 重新构建
     *  }
     *  
     * 这样就很好的解决了实际的问题。
     * 软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
     * 关于引用队列，会在弱引用中用到
     */
    
    public static void main(String[] args) {
        String str = "AABBCC";
        System.out.println("第一次使用：" + str);
        SoftReference<String> srStr = new SoftReference<String>(str);
        str = null;
        System.out.println("使用完成置为空：" + str);
        System.out.println("等待10秒");
        try {
            /**
             *  public static Thread currentThread()返回对当前正在执行的线程对象的引用。 
             *  返回：当前执行的线程。返回的是一个Thread
             *  该方法主要是为了协助  实现通过Runnable接口来对线程进行设置和获取线程的名称的
             * */
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        str = srStr.get();
        System.out.println("从软引用中取出" + str);
    }
}
