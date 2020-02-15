package com.fatboy.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
    public static void main(String[] args) throws Exception  {
        /*运行最多线程数为 maxinumPoolSize 加上队列的长度LinkedBlockingQueue(3)
        //ThreadPoolExecutor.CallerRunsPolicy 会返回给调用线程自己处理
        ThreadPoolExecutor.AbortPolicy 直接报错 超出例子的8个线程
        ThreadPoolExecutor.DiscardOldestPolicy 抛弃等待最久的线程
        ThreadPoolExecutor.DiscardPolicy 直接抛弃线程
        maxinumPoolSize 的设置为cpu核数+1 cpu型
        maxinumPoolSize 的设置为cpu核数*2 io密集型
        maxinumPoolSize 的设置为cpu核数/(1-0.9) io密集型
        */
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(2,
                Runtime.getRuntime().availableProcessors()+1,
                1,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i=1;i<10;i++){
            final int temp=i;
            poolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName()+"-number:"+temp);
            });
        }
        poolExecutor.shutdown();
    }
}
