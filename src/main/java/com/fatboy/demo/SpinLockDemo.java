package com.fatboy.demo;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class SpinLockDemo {
    private AtomicReference<Thread> atomicStampedReference=new AtomicReference();

    private void lock(){
        Thread demo=Thread.currentThread();
        System.out.println(demo.getName()+"lock");
        while(!atomicStampedReference.compareAndSet(null,demo));
    }

    private void unlock(){
        Thread demo=Thread.currentThread();
        atomicStampedReference.compareAndSet(demo,null);
        System.out.println(demo.getName()+"unlock");
    }

    public static void main(String[] args){
        SpinLockDemo lockDemo=new SpinLockDemo();
        new Thread(()->{
            lockDemo.lock();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lockDemo.unlock();
        },"A").start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            lockDemo.lock();
            lockDemo.unlock();
        },"B").start();
    }
}
