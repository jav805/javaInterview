package com.fatboy.demo;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class GCDemo {
    /*
     * 强引用内存不够用也不会回收，报OOM
     * */
    public static void strong(){
        Object obj=new Object();
        Object obj2=obj;
        obj=null;
        System.gc();
        System.out.println(obj);
        System.out.println(obj2);
    }
    /*
    * jvm配置 故意产生大对象并配置小的内存，使它内存不够用导致OOM,查看软引用回收情况
    * -xmx10m xms10m -XX:PrintGCDetails 软引用内存不够用就回收
    * */
    public static void soft(){
        Object obj=new Object();
        SoftReference<Object> obj2=new SoftReference<>(obj);
        obj=null;
        try{
            byte[] a=new byte[20*1024*1024];
        }finally {
            System.out.println(obj);
            System.out.println(obj2.get());
        }
    }

    /*
     * 弱引用假设垃圾收集器在某个时间点确定对象是弱可到达的。到那时，它将自动清除对该对象的所有弱引用
     * */
    public static void weak(){
        Object obj=new Object();
        WeakReference<Object> obj2=new WeakReference<>(obj);
        obj=null;
        System.gc();
        System.out.println(obj);
        System.out.println(obj2.get());
    }

    public static void weakMap(){
        WeakHashMap<Integer,String> map=new WeakHashMap<>();
        map.put(1,"1");
        System.out.println(map);
        System.gc();
        System.out.println(map.size());
    }

    public static void phantom(){
        ReferenceQueue<Object> queue=new ReferenceQueue();
        Object object=new Object();
        WeakReference<Object> obj=new WeakReference<>(object,queue);
        System.out.println(object);
        System.out.println(obj.get());
        System.out.println(queue.poll());
        System.gc();
        object=null;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("================");
        System.out.println(object);
        System.out.println(obj.get());
        System.out.println(queue.poll());

    }
    /*
    * 新生代串型垃圾回收器  老年代默认也是串型
    *-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseSerialGC (DefNew+Tenured)
    * [GC (Allocation Failure) [新生代DefNew: 2752K->320K(3072K), 0.0027046 secs] 2752K->916K(9920K), 0.0035551 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
    * [Full GC (Allocation Failure) [老年代Tenured: 6847K->6798K(6848K), 0.0208123 secs] 9919K->8138K(9920K), [Metaspace: 3457K->3457K(1056768K)], 0.0208560 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
     *
     * 新生代并行垃圾回收器 老年代默认也是串型
     * Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParNewGC (ParNew+Tenured)
     * [GC (Allocation Failure) [新生代ParNew: 2752K->319K(3072K), 0.0015517 secs] 2752K->932K(9920K), 0.0016179 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * [Full GC (Allocation Failure) [老年代Tenured: 6848K->6596K(6848K), 0.0242226 secs] 9919K->8011K(9920K), [Metaspace: 3452K->3452K(1056768K)], 0.0242700 secs] [Times: user=0.05 sys=0.00, real=0.02 secs]
     * -XX:ParallelGCThreads 限制线程数量 默认跟CPU核数一致
     *
     * 新生代并行垃圾回收器 老年代默认也是并行
     * Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseParallelGC 或-XX:+UseParallelOldGC (PSYoungGen+ParOldGen)
     * [GC (Allocation Failure) [新生代PSYoungGen: 2536K->494K(2560K)] 2799K->1422K(9728K), 0.0018690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * [Full GC (Ergonomics) [PSYoungGen: 416K->0K(2048K)] [老年代ParOldGen: 6174K->6544K(7168K)] 6590K->6544K(9216K), [Metaspace: 3457K->3457K(1056768K)], 0.0501224 secs] [Times: user=0.06 sys=0.00, real=0.05 secs]
     * -XX:MaxGCPauseMills 合适的停顿时间
     *
     * 老年代标记清除垃圾回收器 新生代默认并行
     * Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseConcMarkSweepGC （ParNew+CMS）
     * [GC (Allocation Failure) [ParNew: 2752K->320K(3072K), 0.0054403 secs] 2752K->930K(9920K), 0.0055245 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
     * [GC (CMS Initial Mark)初始化标记 [1 CMS-initial-mark: 3687K(6848K)] 4061K(9920K), 0.0002157 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * [CMS-concurrent-mark 并发标记: 0.005/0.005 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
     * [GC (CMS Final Remark)重新标记 [YG occupancy: 2170 K (3072 K)][Rescan (parallel) , 0.0007271 secs][weak refs processing, 0.0000243 secs][class unloading, 0.0004512 secs][scrub symbol table, 0.0006720 secs][scrub string table, 0.0001416 secs][1 CMS-remark: 5914K(6848K)] 8084K(9920K), 0.0021438 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     * [CMS-concurrent-sweep-start]标记清除
     * [GC (Allocation Failure) [ParNew: 3072K->3072K(3072K), 0.0000241 secs][CMS[CMS-concurrent-sweep: 0.004/0.004 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
     *(concurrent mode failure): 5814K->6803K(6848K), 0.0299667 secs] 8886K->6803K(9920K), [Metaspace: 3457K->3457K(1056768K)], 0.0300757 secs] [Times: user=0.03 sys=0.00, real=0.03 secs]
     *[GC (Allocation Failure) [ParNew: 2752K->2752K(3072K), 0.0000235 secs][CMS: 6803K->6466K(6848K), 0.0261421 secs] 9555K->7774K(9920K), [Metaspace: 3457K->3457K(1056768K)], 0.0262274 secs] [Times: user=0.02 sys=0.00, real=0.03 secs]
     *
     * 老年代串型垃圾回收器
     * Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseSerialOldGC
     *
     * G1并发整理回收器
     *Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:+UseG1GC
     * [GC pause (G1 Evacuation Pause) (young) (initial-mark初始化标记), 0.0024461 secs]
     * [GC concurrent-root-region-scan-start]并发开发
     * [GC concurrent-mark-start]并发开发
     * [GC concurrent-mark-end, 0.0188541 secs]
     * [GC remark [Finalize Marking最终标记, 0.0000908 secs] [GC ref-proc, 0.0001521 secs] [Unloading, 0.0008543 secs], 0.0012744 secs]
     * -XX:G1HeapRegionSize=2 设置G1 region块大小默认2的幂 最大32
     * -XX:MaxGCPauseMills=？ 最大GC停顿时间
     * -XX:InitiatingHeapOccupancyPercent 堆占用多少就触发GC 默认45
     * -XX:ConcGCThreads=? 并发GC使用线程数
     * -XX:G1ReservePercent=? 设置空闲空间预留内存百分比，以降低目标空间溢出的风险，默认值10%
     * */
    public static void useSerialGC(){
        List list=new ArrayList<String>();
        int i=0;
        while (true){
            list.add(new String(i+++""));
        }
    }
    /**
     * jps 查看java 进程ID
     * jinfo -flag 参数名(PrintGCDetail) 查看具体参数
     * jinfo -flags 查看所有默认
     * java -XX:+PrintFlagsInitial 初始化
     * java -XX:+PrintFlagsFinal 最后修改过的参数
     * java -XX:+PrintFlagsFinal -XX:MetaspaceSize=128m 类（A） 动态修改参数
     *
     * xms -XX:InitialHeapSize 堆内存最小 默认为物理内存的1/64
     * xmx -XX:MaxHeapSize 堆最大内存 默认物理内存1/4
     * xss -XX:ThreadStackSize 线程栈大小 一般默认512-1024kb
     * xmn 设置年轻代大小
     * xx:MetaSpaceSize java8元空间 默认为20m，建议跟堆大小一致
     * xx:SurvivorRatio=8 就是 eden:s0:s1=8:1:1 urvivorRatio=4 eden:s0:s1=4:1:1
     * xx:NewRatio=2  设置老年代的占比，默认2，新生代占1，老年代占2 年轻代占堆得1/3 NewRatio=4 新生代占1，老年代占4 年轻代占堆得1/5
     * xx:MaxTenuringTheShold 最大垃圾年龄 默认15
     * DefNew Default New Generation
     * Tenured old
     * ParNew Parallel New Generation
     * PSYoungGen Parallel Scavenge
     * ParOldGen Parallel Old Generation
     */
    public static void main(String[] args){
        System.out.println("====================");
       // strong();
        //soft();
        //weak();
       // weakMap();
        //phantom();
        /*long totalMemory=Runtime.getRuntime().totalMemory();//xms
        long maxMemory=Runtime.getRuntime().maxMemory();//xmx
        System.out.println("xms:"+totalMemory/1024/1024+",xmx:"+maxMemory/1024/1024);*/
        useSerialGC();

    }
}
