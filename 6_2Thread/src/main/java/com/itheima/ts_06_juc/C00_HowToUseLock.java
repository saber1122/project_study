package com.itheima.ts_06_juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock
 * lock()/unlock()
 */
public class C00_HowToUseLock {
    //ReentrantLock：支持公平非公平模式
    private static Lock lock = new ReentrantLock();
    //累加结果
    private static int count;

    public static void main(String[] args) throws Exception {
        //启动numOfThreads个线程
        int numOfThreads = 1000;
        Thread[] threads = new Thread[numOfThreads];
        for (int i=0;i<numOfThreads;i++) {
            threads[i] = new Thread(() -> {
                //每个线程执行累加操作
                addCount();
            });
            threads[i].start();
        }
        //等待执行结束
        for (Thread t:threads) {
            t.join();
        }
        System.out.println("count="+count);
    }

    private static void addCount() {

        /*for (int i=0;i<1000;i++) {
            count++;
        }*/

        // 1、lock/unlock  经典：try {} finally{}

        try {
            lock.lock();
            for (int i=0;i<1000;i++) {
                count++;
            }
        }finally {
            lock.unlock();
        }

        // 2、支持超时获取锁且支持中断  tryLock(5, TimeUnit.SECONDS)
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                try {
                    for (int i=0;i<1000;i++) {
                        count++;
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3、非阻塞获取锁  tryLock()
        if (lock.tryLock()) {
            try {
                for (int i=0;i<1000;i++) {
                    count++;
                }
            } finally {
                lock.unlock();
            }
        }

        //4, 获取锁时支持被中断
        try {
            try {
                lock.lockInterruptibly();
                for (int i=0;i<1000;i++) {
                    count++;
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
