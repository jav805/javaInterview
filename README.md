#  JAVA 面试复习之路


### volatile是什么？有什么特点？
volatile就是让变量每次在使用的时候，都从主存中取。而不是从各个线程的“工作内存”，特点：
1. 可见性；
2. 禁止指令重排；
3. 不保证原子性；