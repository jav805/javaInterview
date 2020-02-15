package com.fatboy.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
/**
 *
 *
 * @author david
 *
 */
public class CyclicBarrierDemo {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception  {
        //屏障，阻拦3个线程
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1正在执行");
                try {
                    // 等待
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("线程1运行结束，时间： " + System.currentTimeMillis());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程2正在执行");
                try {
                    // 等待
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("线程2运行结束，时间： " + System.currentTimeMillis());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程3正在执行");
                try {
                    //线程3阻塞2秒，测试效果
//                    Thread.sleep(2000);
                    // 等待
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("线程3运行结束，时间： " + System.currentTimeMillis());
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程4正在执行");
                try {
                    // 等待
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("线程4运行结束，时间： " + System.currentTimeMillis());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程5正在执行");
                try {
                    // 等待
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("线程5运行结束，时间： " + System.currentTimeMillis());
            }
        }).start();
    }
}
