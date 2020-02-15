package com.fatboy.demo;

import java.util.concurrent.TimeUnit;

public class DeadLockDemo implements Runnable{
    private String lock1;
    private String lock2;
    public DeadLockDemo(String lock1,String lock2){
        this.lock1=lock1;
        this.lock2=lock2;
    }
    public static void main(String[] args){
        new Thread(new DeadLockDemo("A","B")).start();
        new Thread(new DeadLockDemo("B","A")).start();
    }

    @Override
    public void run() {
        synchronized (lock1){
            System.out.println(lock1);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2){
                System.out.println(lock2);
            }
        }
    }
}
