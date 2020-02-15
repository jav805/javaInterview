package com.fatboy.demo;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.misc.VM;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OOMDemo {
    public static void stackOverFlowError(){
        stackOverFlowError();//java.lang.StackOverflowError
    }

    public static void outOfMemory(){
        byte a[]=new byte[20*1024*1024];//java.lang.OutOfMemoryError: Java heap space
    }
    /*
    * java.lang.OutOfMemoryError: GC overhead limit exceeded
    * GC超过98%的时间做GC并且回收了不到2%的堆内容
    * 那就是GC清理的这么点内存很快会再次填满，迫使GC再次致谢导致恶性循环
    * CPU使用率一直在100%，而GC没有效果
    * */
    public static void gcOverHead(){
        List list=new ArrayList<String>();
        int i=0;
        while (true){
            list.add(new String(i+++""));
        }
    }
    /*
    * java.lang.OutOfMemoryError: Direct buffer memory
    * 堆外内存OutOfMemoryError
    * -XX:MaxDirectMemorySize=5m 设置堆外内存大小
    * */
    public static void directMemoryError(){
        System.out.println(VM.maxDirectMemory()/1024/1024+"MB");
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(10*1024*1024);
    }
    /*
     * 在linux 系统里面执行
     * */
    public static void unableCreateNativeThread(){
        int i=0;
       while (true){
           final int temp=i++;
           new Thread(()->{
               try {
                   System.out.println(temp);
                   Thread.sleep(Integer.MAX_VALUE);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }).start();
       }
    }
    static class OOMTest{}
    /*
    * java.lang.OutOfMemoryError: Metaspace
    * */
    public static void metaSpiceError(){
        int i=1;
        while (true){
            i++;
            Enhancer enhancer=new Enhancer();
            enhancer.setSuperclass(OOMTest.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o,null);
                }
            });
            enhancer.create();
            System.out.println(i);

        }
    }
    public static void main(String[] args){
        //stackOverFlowError();
        //outOfMemory();
        //gcOverHead();
        //directMemoryError();
        //unableCreateNativeThread();
        metaSpiceError();
    }
}
