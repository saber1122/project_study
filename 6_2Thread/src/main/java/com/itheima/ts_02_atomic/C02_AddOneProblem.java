package com.itheima.ts_02_atomic;

/*
    i+=1问题的解决
 */
public class C02_AddOneProblem {

    long i = 0L;

    /**
     * addOne() 的结果能对get()可见吗？
     *
     * @return
     */
    public /*synchronized*/   long get() {
        return i;
    }


    /**
     *  我们知道：i+=1 并非原子操作，会有线程安全问题
     *  要想得以解决就可以加锁
     *
     *  对于`addOne`方法，添加了`synchronized`修饰之后，无论是单核 CPU 还是多核 CPU，只有一个线程能够执行 addOne() 方法，
     *  所以一定能保证原子操作。
     *
     * addOne能保证可见性吗？
     * 1、基于 `Happens-Before`规则中有一条管程中的锁规则：对一个锁的解锁 Happens-Before 于后续对这个锁的加锁，
     *   再结合Happens-Before 的传递性原则，我们知道，`addOne`方法中+1操作的结果肯定会在释放锁之前刷到主内存，
     *  锁释放后下一个进入到`addOne`方法的线程获取锁时能够获取到上一个线程的操作结果。
     *  即前一个线程在临界区修改的共享变量，对后续进入临界区的线程是可见的。
     */
    public synchronized void addOne() {
        i+=1;
    }

}

