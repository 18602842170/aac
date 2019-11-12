package com.cn.aac.threadShow;

/**
 * 理解。一句话：每一个线程在threadLocal 中都有自己独立的KEY VALUE映射，此变量的作用为为线程，2线程永远改不了1线程的threadLocal的值。
 * @author Administrator
 * ！！！！！业务场景：
 * 比如我们有一个service。service 中有userDao 和  rommDao。
 * 如果我们要从最原始的开始写。那么就应该写一个 connectionManager,来管理DB的连接，提交，回滚，关闭等。
 * 正常写法下。。我们userDao 中需要获取一个 connection，rommDao中也需要一个。就会获取两次，然后提交两次，关闭两次连接。
 * 如果要达到service中事务的效果。我们还要自己写判断，判断两次使用dao有没有异常。没有才提交两个connection。
 * 那么我们一个service中要处理n个表的增删改查，那不是要有n个connection的获取连接提交关闭，效率低下。
 * 如果使用 ThreadLocal： 
 *  比如在connectionManager中的 connection对象改为：
 *  ThreadLocal<connection> threadLocal = new ThreadLocal<connection>()
 *  当我们需要一个connection时候：
 *  public static void BeginTrans(boolean beginTrans) throws Exception {
 *      if (tl.get() == null || ((Connection) tl.get()).isClosed()) {
            conn = DBConnection.getInstance().getConnection();
            conn = new ConnectionSpy(conn);
            if (beginTrans) {
                conn.setAutoCommit(false);
                }
            tl.set(conn);
           }
         }
 *  在需要connection时从threadLocal获取，好处？？？
 *  前面说到了，每一个线程会有个单独私有的connection存在threadLocal中。key为当前线程：Thread.currentThread()
 *  当我们处理一次请求的时候，就是一个线程，在这个线程中，无论我们在什么地方获取多少次Connection都是同一个connection，
 *  并且我们可以用他来实现事务的同时提交，回滚，n次使用dao，使用的也是同一个connection，不会多次连接关闭。      
 */
public class ThreadLocalShow extends Thread {
    
    private ResultData data;
    
    public ThreadLocalShow(ResultData data) {
        this.data = data;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "---" + "i---" + i + "--num:" + data.getNum());
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        ResultData data = new ResultData();
        ThreadLocalShow threadLocaDemo1 = new ThreadLocalShow(data);
        ThreadLocalShow threadLocaDemo2 = new ThreadLocalShow(data);
        ThreadLocalShow threadLocaDemo3 = new ThreadLocalShow(data);
        threadLocaDemo1.start();
        threadLocaDemo2.start();
        threadLocaDemo3.start();
        Thread.sleep(300);
        System.out.println(ResultData.count);
    }
    
}

class ResultData {
    // 生成序列号共享变量
    public static Integer count = 0;
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    
    public Integer getNum() {
        int count = threadLocal.get() + 1;
        threadLocal.set(count);
        return count;
    }
}
