package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.concurrent.Semaphore;

/**
 * 二值信号量
 * 用于线程同步
 */
public class C04_BinarySem {
    //二值信号量 初始值0
    private static final Semaphore SEMAPHORE = new Semaphore(0);

    public static void main(String[] args) {
        //可以控制两个线程/进程 顺序执行
        new Thread(()->{
            try {
                SEMAPHORE.acquire();
                System.out.println("get semaphore");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtil.sleepSeconds(5);
        }).start();

        new Thread(()->{
            System.out.println("t1 start");
            ThreadUtil.sleepSeconds(5);
            SEMAPHORE.release();
        }).start();
    }
}
