package com.fatboy.demo;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RetranLockDemo {
    private HashMap<Integer,Integer> hashMap=new HashMap<>();
    private ReentrantReadWriteLock lock=new ReentrantReadWriteLock();

    private void set(Integer key,Integer value){
        lock.writeLock().lock();
        System.out.println("input key:"+key+",value="+value);
        hashMap.put(key,value);
        lock.writeLock().unlock();
    }

    private void get(Integer key){
        lock.readLock().lock();
        System.out.println("read :"+hashMap.get(key));
        lock.readLock().unlock();
    }

    public static void main(String[] args){
        RetranLockDemo demo=new RetranLockDemo();
        for (int i = 0; i<30; i++){
            final int input=i;
            new Thread(()->{
                demo.set(input,input);
            },"A").start();
            try {
                Thread.sleep(10l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(()->{
                demo.get(input);
            },"B").start();
            new Thread(()->{
                demo.get(input);
            },"B").start();
        }
        /*try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
       /* for (int i = 0; i<10; i++){
            final int input=i;
            new Thread(()->{
                demo.get(input);
            },"A").start();
        }*/

    }
}
