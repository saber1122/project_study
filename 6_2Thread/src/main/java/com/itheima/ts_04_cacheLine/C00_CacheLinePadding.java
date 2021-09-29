package com.itheima.ts_04_cacheLine;

import java.util.concurrent.CountDownLatch;

/**
 * 缓存行对齐
 *
 */
public class C00_CacheLinePadding {

    public static long count = 1000L;


    private static class C{
        //private long p1,p2,p3,p4,p5,p6,p7;
        private /*volatile*/ long x = 0L ; // 缓存行的相关概念本身和volatile无关
        //private long p9,p10,p11,p12,p13,p14,p15;
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
