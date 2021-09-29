package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 主要用来解决一个线程等待多个线程的场景
 */
public class C08_CountDownLatch {

    // 一等多
    static void m1()  {
        Random r = new Random();
        int numsOfThread = 10;
        CountDownLatch latch = new CountDownLatch(numsOfThread);
        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                ThreadUtil.sleepSeconds(r.nextInt(numsOfThread));
                System.out.println(Thread.currentThread().getName()+" 到了");
                latch.countDown();
            }, "项目组同事"+i).start();
        }
        // 等同事都到了再开饭
        try {
            latch.await();
            //latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("干饭");
    }

    //多等一
    static void m2() {
        Random r = new Random();
        int numsOfThread = 10;
        CountDownLatch latch = new CountDownLatch(1);
        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 已准备就绪");
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 起跑");
                ThreadUtil.sleepSeconds(r.nextInt(numsOfThread)+1);
                System.out.println(Thread.currentThread().getName()+" 跑到了终点");
            }, "运动员"+i).start();
        }
        ThreadUtil.sleepSeconds(1);

        System.out.println("2s后发令员准备发号");
        ThreadUtil.sleepSeconds(2);
        System.out.println("枪响了");
        latch.countDown();
    }




    public static void main(String[] args) throws Exception{
//        m1()
//        m2();
    }
}
