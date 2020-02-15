package com.fatboy.demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockNotifyDemo {
    private int number=0;
    private int print=1;
    private Lock lock=new ReentrantLock();
    private Condition c1=lock.newCondition();
    private Condition c2=lock.newCondition();
    private Condition c3=lock.newCondition();
    private void add(){
        lock.lock();
        try {
            while (number!=0) {
                lock.newCondition().await();
            }
                number++;
                System.out.println(Thread.currentThread().getName()+":"+number);
                lock.newCondition().signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
    }

    private void decrease(){
        lock.lock();
        try {
        while (number==0) {
            lock.newCondition().await();
        }
                number--;
                System.out.println(Thread.currentThread().getName()+":"+number);
              lock.newCondition().signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
    }

    private void print5(){
        lock.lock();
        try {
            while (print!=1) {
                c1.await();;
            }
            for (int i=0;i<5;i++){
                System.out.println(Thread.currentThread().getName()+":"+print);
            }
            print=2;
            c2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void print10(){
        lock.lock();
        try {
            while (print!=2) {
                c2.await();
            }
            for (int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName()+":"+print);
            }
            print=3;
            c3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private void print15(){
        lock.lock();
        try {
            while (print!=3) {
                c3.await();
            }
            for (int i=0;i<15;i++){
                System.out.println(Thread.currentThread().getName()+":"+print);
            }
            print=1;
            c1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args){
        LockNotifyDemo demp=new LockNotifyDemo();
        for (int i=0;i<10;i++){
           /* new Thread(()->{
                demp.print5();
            },"A").start();*/
            new Thread(()->{
                demp.print10();
            },"B").start();
            new Thread(()->{
                demp.print15();
            },"C").start();
        }
        /*for (int i=0;i<10;i++){
            new Thread(()->{
                demp.add();
            },"A").start();
            new Thread(()->{
                demp.decrease();
            },"B").start();
        }*/

    }
}
