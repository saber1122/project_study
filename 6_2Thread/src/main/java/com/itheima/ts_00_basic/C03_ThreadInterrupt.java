package com.itheima.ts_00_basic;

import com.itheima.util.ThreadUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程中断 : Just to set the interrupt flag
 *  设置线程中断标志位
 */
public class C03_ThreadInterrupt {

    //1、 interrupt +  isInterrupted   也是让线程优雅结束的一种方案
    public static void it1(){
        Thread t1 = new Thread(()->{
            for (;;){
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName()+" isInterrupted ");
                    System.out.println(Thread.currentThread().getName()+" interrupt flag is "+Thread.currentThread().isInterrupted());
                    break;
                }else {
                    System.out.println(Thread.currentThread().getName()+" interrupt flag is "+Thread.currentThread().isInterrupted());
                }
            }
        });
        t1.start();

        ThreadUtil.sleepSeconds(2);

        t1.interrupt();
    }

    //2、 Thread.interrupted() 查询 当前 线程的 interrupt flag 并重置
    public static void it2(){
        Thread t2 = new Thread(()->{
            for (;;){
                if (Thread.interrupted()) {
                    System.out.println(Thread.currentThread().getName()+" isInterrupted ");
                    System.out.println(Thread.currentThread().getName()+" interrupt flag is "+Thread.interrupted());
                    break;
                }else {
                    System.out.println(Thread.currentThread().getName()+" interrupt flag is "+Thread.interrupted());
                }
            }
        });
        t2.start();

        ThreadUtil.sleepSeconds(2);

        t2.interrupt();

    }


    //3、 interrupt + wait,sleep,join  产生 InterruptedException 也是另一种让线程优雅退出的方案
    public static void it3() {
        Thread t3 = new Thread(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread is Interrupted");
                // 产生InterruptedException 后会自动复位
                System.out.println(" interrupt flag is "+Thread.currentThread().isInterrupted());
            }
        });
        t3.start();
        ThreadUtil.sleepSeconds(5);

        t3.interrupt();

    }

    // 4、interrupt + synchronized/lock : interrupt不会打断 锁的竞争过程

    public static void it4() {
        Object o = new Object();
        new Thread(()->{
            synchronized (o) {
                ThreadUtil.sleepSeconds(10);//sleep是不会释放锁的
            }
        }).start();

        Thread t4 = new Thread(()->{
            synchronized (o) {
                System.out.println("t4 get locked");
            }
            System.out.println("t4 will be finished");
        });

        t4.start();
        ThreadUtil.sleepSeconds(1);
        t4.interrupt();
    }
    public static void it5() {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            try {
                lock.lock();
                ThreadUtil.sleepSeconds(10);
            } finally {
                lock.unlock();
            }

        }).start();

        Thread t5 = new Thread(()->{
            try {
                lock.lock(); // WAITING
                System.out.println("t5 get locked");
            } finally {
                lock.unlock();
                System.out.println("t5 will be finished");
            }
        });

        t5.start();

        ThreadUtil.sleepSeconds(1);
        t5.interrupt();
    }


    // 5、interrupt能否打断锁的竞争过程： 能，使用lock.lockInterruptibly去获取锁，竞争的过程能被打断
    public static void it6() {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            try {
                lock.lock();
                ThreadUtil.sleepSeconds(10);
            } finally {
                lock.unlock();
            }

        }).start();

        Thread t6 = new Thread(()->{
            try {
                lock.lockInterruptibly();
                System.out.println("t5 get locked");
            } catch (InterruptedException e) {
                System.out.println("Thread is Interrupted");
            } finally {
                lock.unlock();
            }
        });

        t6.start();

        ThreadUtil.sleepSeconds(1);
        t6.interrupt();
    }

    public static void main(String[] args) {
        // it1();
        // it2();
         //it3();
        it4();
        // it5();
        //it6();
    }
}
