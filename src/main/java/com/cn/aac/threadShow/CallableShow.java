package com.cn.aac.threadShow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Callable能接受一个泛型，然后在call方法中返回一个这个类型的值。而Runnable的run方法没有返回值
 * Callable的call方法可以抛出异常，而Runnable的run方法不会抛出异常。
 * 返回值Future也是一个接口，通过他可以获得任务执行的返回值。
 * 
 */
public class CallableShow {
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        //        callableTest1(executor);
        
        //        submitTest(executor);
        
        catchTest(executor);
        
    }
    
    /**
     * get()方法的阻塞性
     * 通过上面的输出可以看到，在调用submit提交任务之后，主线程本来是继续运行了。
     * 但是运行到future.get()的时候就阻塞住了，一直等到任务执行完毕，拿到了返回的返回值，主线程才会继续运行。
     * 这里注意一下，他的阻塞性是因为调用get()方法时，任务还没有执行完，所以会一直等到任务完成，形成了阻塞。
     * 任务是在调用submit方法时就开始执行了，如果在调用get()方法时，任务已经执行完毕，那么就不会造成阻塞。
     * 下面在调用方法前先睡4秒，这时就能马上得到返回值。
     * 
     * get(long var1, TimeUnit var3)
     * 前面都是用的get()方法获取返回值，那么因为这个方法是阻塞的，有时需要等很久。所以有时候需要设置超时时间。
     * 
     * @throws ExecutionException 
     * @throws InterruptedException 
     */
    public static void callableTest1(ExecutorService executor) throws InterruptedException, ExecutionException {
        Callable<String> myCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.currentThread().sleep(3000);
                System.out.println("calld方法执行了");
                return "call方法返回值";
            }
        };
        System.out.println("提交任务之前 " + getStringDate());
        Future<String> ft = executor.submit(myCallable);
        System.out.println("提交任务之后，获取结果之前 " + getStringDate());
        System.out.println("获取返回值: " + ft.get());
        System.out.println("获取到结果之后 " + getStringDate());
        
        //        System.out.println("提交任务之前 "+getStringDate());
        //        Future future = executor.submit(myCallable);
        //        System.out.println("提交任务之后 "+getStringDate());
        //        Thread.sleep(4000);
        //        System.out.println("已经睡了4秒,开始获取结果 "+getStringDate());
        //        System.out.println("获取返回值: "+future.get());
        //        System.out.println("获取到结果之后 "+getStringDate());
        
        //        System.out.println("提交任务之前 " + getStringDate());
        //        Future<String> ft2 = executor.submit(myCallable);
        //        System.out.println("提交任务之后，获取结果之前 " + getStringDate());
        //        try {
        //            System.out.println("获取返回值: " + ft2.get(2, TimeUnit.SECONDS));
        //        } catch (TimeoutException e) {
        //            System.out.println("超时了 " + getStringDate());
        //            e.printStackTrace();
        //        }
        //        System.out.println("获取到结果之后 " + getStringDate());
        
    }
    
    /**
     * submit(Runnable task)
     * 因为Runnable是没有返回值的，所以如果submit一个Runnable的话，get得到的为null：
     * submit(Runnable task, T result)
     * 虽然submit传入Runnable不能直接返回内容，但是可以通过submit(Runnable task, T result)传入一个载体，
     * 通过这个载体获取返回值。这个其实不能算返回值了，是交给线程处理一下。
     * 
     * 通过下方的测试，发现原来的data也变了。所以只是改变了一个对象，future的作用是告诉我们线程什么时候完成了。
     * @throws ExecutionException 
     * @throws InterruptedException 
     * 
     * 
     */
    public static void submitTest(ExecutorService executor) throws InterruptedException, ExecutionException {
        Data data = new Data();
        System.out.println("执行前的数据: " + data.getName() + ", sex: " + data.getSex());
        Future<Data> future = executor.submit(new MyThread(data), data);
        System.out.println("返回的结果 name: " + future.get().getName() + ", sex: " + future.get().getSex());
        System.out.println("原来的Data name: " + data.getName() + ", sex: " + data.getSex());
    }
    
    /**
     * 使用submit方法还有一个特点就是，他的异常可以在主线程中catch到。
     * 而使用execute方法执行任务是捕捉不到异常的。
     * 使用execute
     * 这里如果捕捉到异常，只打印一行异常信息。
     * 并没有出现抓到异常哪行日志。而且这个异常输出是在线程pool-1-thread-1中，并不是在主线程中。说明主线程的catch不能捕捉到这个异常。
     * 使用submit
     * @return
     */
    public static void catchTest(ExecutorService executor) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                executor.execute(null);
            }
        };
        
        //使用execute
        try {
            executor.execute(r);
        } catch (Exception e) {
            System.out.println("抓到异常 " + e.getMessage());
            e.printStackTrace();
        }
        
        //使用submit
        try {
            Future future = executor.submit(r);
            future.get();
        } catch (Exception e) {
            System.out.println("抓到异常 " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    public static String getStringDate() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String dateString = currentTime.format(fmt);
        return dateString;
    }
    
}

class MyThread implements Runnable {
    
    Data data;
    
    public MyThread(Data data) {
        this.data = data;
    }
    
    public void run() {
        try {
            Thread.currentThread().sleep(3000);
            System.out.println("线程 执行:");
            data.setName("新名字");
            data.setSex("新性别");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class Data {
    String name;
    String sex;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
}
