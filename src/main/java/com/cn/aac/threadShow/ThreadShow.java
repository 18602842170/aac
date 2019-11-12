package com.cn.aac.threadShow;

public class ThreadShow extends Thread {
    
    public ThreadShow() {
        super();
    }
    
    public ThreadShow(ThreadGroup g, String name) {
        super(g, name);
    }
    
    public void run() {
        try {
            sleep(50000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print("run");
    }
    
    public static void main(String[] args) throws Exception {
        /**
         * 此处重点说一下线程的优先级和状态。
         * 线程执行有优先级，优先级越高先执行机会越大(并不是一定先执行！！)。优先级用int的priority参数表示。
         * 线程优先级最高为10，最低为1。默认为5
         */
        ThreadShow ts = new ThreadShow();
        // 设置优先级 0-10
        ts.setPriority(10);
        /**
         * Thread对象共有6种状态：NEW(新建)，RUNNABLE(运行)，BLOCKED(阻塞)，WAITING(等待)，TIMED_WAITING(有时间的等待)，TERMINATED(终止);
         * 下方为获取状态
         * 也有一种说法，我认为也可以：
         * 线程只有”就绪”、”阻塞”、”运行”三种状态(新建[NEW]”和”终止[TERMINATED]”状态的线程并不是线程，只是代表一个线程对象还存在)：
         * 1. RUNNABLE，对应”就绪”和”运行”两种状态，也就是说处于就绪和运行状态的线程在java.lang.Thread中都表现为”RUNNABLE”
         * 2. BLOCKED，对应”阻塞”状态，此线程需要获得某个锁才能继续执行，而这个锁目前被其他线程持有，所以进入了被动的等待状态，直到抢到了那个锁，才会再次进入”就绪”状态
         * 3. WAITING，对应”阻塞”状态，代表此线程正处于无限期的主动等待中，直到有人唤醒它，它才会再次进入就绪状态
         * 4. TIMED_WAITING，对应”阻塞”状态，代表此线程正处于有限期的主动等待中，要么有人唤醒它，要么等待够了一定时间之后，才会再次进入就绪状态
         */
        System.out.print(ts.getState());
        /**
         * Thread()对外提供了8个构造器，但是都是接收不同参数，然后调用init(ThreadGroup g, Runnable target, String name, long stackSize)方法。
         * 故我们只需分析次init()方法即可。init()方法共有4个参数，分别代表：
         * ThreadGroup g 指定当前线程的线程组，未指定时线程组为创建该线程所属的线程组。线程组可以用来管理一组线程，通过activeCount() 来查看活动线程的数量。其他没有什么大的用处。　
         * Runnable target 指定运行其中的Runnable，一般都需要指定，不指定的线程没有意义，或者可以通过创建Thread的子类并重新run方法。
         * String name 线程的名称，不指定自动生成。
         * long stackSize 预期堆栈大小，不指定默认为0，0代表忽略这个属性。与平台相关，不建议使用该属性。
         */
        ThreadGroup group = new ThreadGroup("testgroup");
        String name = "test 1";
        ts = new ThreadShow(group, name);
        ts.setPriority(10);
        ts.start();
        // 打印信息Thread[test 1,10,testgroup] 10为线程优先级
        System.out.println(ts);
        ts.getUncaughtExceptionHandler();
        
        /**
         * 线程中重点方法
         */
        // 该方法是本地静态方法，用于获取当前线程，返回当前的线程对象。
        Thread thist = Thread.currentThread();
        //boolean interrupted() & void interrupt() & boolean isInterrupted()
        //这三个方法是针对线程中断标记的方法， 
        //interrupt():中断本线程(将中断状态标记为true)
        //isInterrupted():检测本线程是否已经中断 。如果已经中断，则返回true，否则false。中断状态不受该方法的影响。 如果中断调用时线程已经不处于活动状态，则返回false。
        //interrupted():检测当前线程是否已经中断 。如果当前线程存在中断，返回true，并且修改标记为false。再调用isIterruoted()会返回false。如果当前线程没有中断标记，返回false，不会修改中断标记。
        /**
         * 测试代码运行过程：
         * 首先运行t1线程，然后interrupt终止。
         * 在线程组赛时，调用interrupt会立即进入InterruptedException异常的catch中
         */
        System.out.println("interrupt测试");
        Thread t1 = new Thread(new InterruptThread());
        Thread t2 = new Thread(new InterruptThread());
        t1.start();
        t1.interrupt();
        // 等待t1结束
        t1.join();
        System.out.println("t1.interrupt()");
        //        System.out.println("1" + t1.isInterrupted());
        //        System.out.println("2" + t1.isInterrupted());
        //        System.out.println("3" + t1.interrupted());
        //        t2.start();
        
    }
    
}

class InterruptThread extends Thread {
    
    @Override
    public void run() {
        
        try {
            sleep(5000);
            System.out.println("4" + currentThread().getName() + interrupted());
            System.out.println("5" + currentThread().getName() + interrupted());
        } catch (InterruptedException e) {
            System.out.println("t1.interrupt()后，阻塞线程进入异常");
            e.printStackTrace();
        }
        
    }
}
