package com.fatboy.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * author david
 * date 2020.2.14
 * volatile就是让变量每次在使用的时候，都从主存中取。而不是从各个线程的“工作内存”
 * */
public class VolatileDemo {
    /*
     * volatile可见性代码demo
     * */
    public static void visible() {
        Test t = new Test();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "进入线程");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.add();
            System.out.println(Thread.currentThread().getName() + t.i);
        }, "A").start();

        while (t.i == 0) {
            //不打印任何东西
        }
        System.out.println(Thread.currentThread().getName() + t.i);
    }

    /*
     * volatile没有原子性
     * 100个线程每个线程+1，如果保证原子性正常最后应该是10，但结果不是100
     * */
    public static void unatomity() {
        CountDownLatch latch=new CountDownLatch(100);
        Test t = new Test();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                t.add2();
                latch.countDown();
            }, "B").start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("unatomity:" + t.i);
    }

    /*
     * 换成AtomicInteger
     * 100个线程每个线程+1，保证原子性正常最后应该是100
     * */
    public static void atomity() {
        CountDownLatch latch=new CountDownLatch(100);
        Test t = new Test();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                t.add3();
                latch.countDown();
            }, "C").start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("atomity:" + t.number);
    }

    public static void main(String[] args) {
        //visible();
        //unatomity();
        //atomity();
    }
}

class Test {
    //不增加volatile 代码在while会卡住不会打印出i修改后的值，增加volatile之后正常
    volatile int i = 0;
    AtomicInteger number = new AtomicInteger();

    public void add() {
        this.i = 100;
    }

    public void add2() {
        i++;
    }

    public void add3() {
        number.getAndIncrement();
    }

}