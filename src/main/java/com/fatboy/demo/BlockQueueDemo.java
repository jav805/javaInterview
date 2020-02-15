package com.fatboy.demo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockQueueDemo {
    private volatile Boolean flag=true;
    private BlockingQueue blockingQueue=null;
    private AtomicInteger atomicInteger=new AtomicInteger();
    public BlockQueueDemo(BlockingQueue blockingQueue){
        this.blockingQueue=blockingQueue;
    }

    private void product(){
        String returnValue;
        while (flag){
            returnValue=atomicInteger.incrementAndGet()+"";
            try {
                if(blockingQueue.offer(returnValue,2,TimeUnit.SECONDS)){
                    System.out.println("add value:"+returnValue);
                }else{
                    System.out.println("add fail:"+returnValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void consumer(){
        while (flag){
            String obj= null;
            try {
                obj = blockingQueue.poll(2,TimeUnit.SECONDS)+"";
                if(null==obj||"".equals(obj)){
                    flag=false;
                    System.out.println("==========");
                }else{
                    System.out.println("consumer:"+obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
/**
 * queue 大小太小例如10很快就加满导致加入失败poll取得时候就为NULL，写入的时候没有blockingQueue.offer(i+""+i,1,TimeUnit.SECONDS);加时间延迟，很有可能写入失败
 */
    public static void main(String[] args){
        BlockQueueDemo demo=new BlockQueueDemo(new ArrayBlockingQueue(1));
        for (int i=0;i<1;i++){
            new Thread(()->{
                demo.product();
            },"A").start();
            new Thread(()->{
                demo.consumer();
                demo.consumer();
            },"B").start();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(10);
            demo.flag=false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
