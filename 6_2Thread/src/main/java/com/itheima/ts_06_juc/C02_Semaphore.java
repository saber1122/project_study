package com.itheima.ts_06_juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * 如何用信号量实现互斥操作
 */
public class C02_Semaphore {

    private static int count;

    //初始化信号量
    private static final Semaphore s = new Semaphore(1);

    //用信号量保证线程安全
    static void addOne() {
        try {
            try {
                s.acquire();
                count++;
            } finally {
                s.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void subOne() {
        try {
            try {
                s.acquire();
                count--;
            } finally {
                s.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception{
        int numsOfThread = 100000;
        CountDownLatch c = new CountDownLatch(numsOfThread*2);
        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                addOne();
                c.countDown();
            }).start();
        }

        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                subOne();
                c.countDown();
            }).start();
        }
        c.await();
        System.out.println("count="+count);

    }
}
