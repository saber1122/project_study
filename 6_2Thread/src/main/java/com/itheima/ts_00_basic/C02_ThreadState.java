package com.itheima.ts_00_basic;

import com.itheima.util.ThreadUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程的6种状态
 */
public class C02_ThreadState {

    // NEW ---> RUNNABLE ----> TERMINATED
    public static void t1() throws Exception {
        Thread t1 = new Thread(()->{
            //运行阶段
            System.out.println(Thread.currentThread().getState());
            ThreadUtil.sleepSeconds(3);
        });
        // 刚创建未启动
        System.out.println(t1.getState());
        t1.start();
        // t1执行join()方法加入当前线程（main），main等待t1结束后，在继续往下执行
        t1.join();
        System.out.println(t1.getState());
    }

    // WAITING    TIMED_WAITING
    public static void t2() {
        Thread t2 = new Thread(()->{
            //当前线程阻塞
            LockSupport.park();
            ThreadUtil.sleepSeconds(5);
        });
        t2.start();
        ThreadUtil.sleepSeconds(1);
        System.out.println(t2.getState());

        LockSupport.unpark(t2);
        ThreadUtil.sleepSeconds(1);
        System.out.println(t2.getState());
    }

    // BLOCKED
    public static void t3() {
       Object o = new Object();
       new Thread(()->{
           synchronized (o) { //持有锁 5s
               ThreadUtil.sleepSeconds(5);
           }
       }).start();

        Thread t3 = new Thread(() -> {
            synchronized (o) { //等待锁,BLOCKED
                ThreadUtil.sleepSeconds(1);
            }
        });
        t3.start();

        ThreadUtil.sleepSeconds(1);
        System.out.println(t3.getState());
    }

    /**
     * Lock 和 synchronized 在等待锁时的线程状态
     *
     * synchronized: BLOCKED (其他情况是WAITING，synchronized是需要经过操作系统调度的，这种阻塞是重量级的，线程状态是BLOCKED)
     * Lock: WAITING
     */
    public static void t4() {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            try {
                lock.lock();
                ThreadUtil.sleepSeconds(5);
            }finally {
                lock.unlock();
            }
        }).start();

        Thread t4 = new Thread(() -> {
            try {
                lock.lock();
                ThreadUtil.sleepSeconds(1);
            }finally {
                lock.unlock();
            }
        });
        t4.start();
        ThreadUtil.sleepSeconds(1);
        System.out.println(t4.getState());
    }




    public static void main(String[] args) throws Exception {
        t1();
        //t2();
        //t3();
        //t4();
    }


}
