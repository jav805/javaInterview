package com.fatboy.demo;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {

    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(5);
        for (int i=1;i<=10;i++){
            final int temp=i;
            new Thread(()->{
                try {
                    semaphore.acquire();
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
                System.out.println(temp);
            },"A").start();
            new Thread(()->{
                try {
                    semaphore.acquire();
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
                System.out.println(temp+100);
            },"B").start();
            new Thread(()->{
                try {
                    semaphore.acquire();
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    semaphore.release();
                }
                System.out.println(temp+10000);
            },"C").start();

        }
    }
}
