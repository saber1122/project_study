package com.itheima.ts_04_cacheLine;

import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;

/**
 * 通过注解
 * 运行时需要添加jvm参数：-XX:-RestrictContended
 * jdk1.8生效
 */
public class C01_Contended {


    public static long count = 1000L;


    private static class C{

        @Contended
        private /*volatile*/ long x = 0L ;
    }


    public static C[] arr = new C[2];
    static {
        arr[0] = new C();
        arr[1] = new C();
    }


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(()->{
            for (long i=0;i<count;i++) {
                arr[0].x= i;
            }
            countDownLatch.countDown();
        });

        Thread t2 = new Thread(()->{
            for (long i=0;i<count;i++) {
                arr[1].x= i;
            }
            countDownLatch.countDown();
        });

        long start = System.nanoTime();
        t1.start();
        t2.start();;
        countDownLatch.await();
        System.out.println("耗时:"+(System.nanoTime()-start));

    }
}
