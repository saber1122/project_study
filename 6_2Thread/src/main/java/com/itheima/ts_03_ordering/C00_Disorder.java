package com.itheima.ts_03_ordering;

import java.util.concurrent.CountDownLatch;

/**
 * 验证程序的确会有乱序执行现象的发生
 *
 */
public class C00_Disorder {

    private static int x = 0 , y = 0;
    private static int a = 0 , b = 0;

    public static void main(String[] args) throws Exception{

        for (long i=0;i<Long.MAX_VALUE;i++) {
            a = 0;
            b = 0;

            x = 0;
            y = 0;

            CountDownLatch latch = new CountDownLatch(2);
            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
                latch.countDown();
            });
            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
                latch.countDown();
            });

            t1.start();
            t2.start();
            latch.await();
            if (x == 0 && y == 0) {
                System.out.println("第"+i+"次执行结果是x=0,y=0,执行结束");
                break;
            }
        }

    }

}
