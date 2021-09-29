package com.itheima.ts_00_basic;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 线程数量是否越大越好？
 * 如何选择工作线程数？
 */
public class C00_NumberOfThreads {
    // 1亿大小的数组
    private static double[] nums = new double[100000000];
    private static Random random = new Random();
    static {
        //初始化数组
        for (int i=0;i< nums.length;i++) {
            nums[i] = random.nextDouble();
        }
    }

    /**
     * 单线程执行
     */
    private static void t1() {
        long start = System.currentTimeMillis();
        double count = 0.0;
        for (double num : nums) {
            count += num;
        }
        long end = System.currentTimeMillis();
        System.out.println("单线程执行耗时:"+(end-start)+"毫秒");
    }


    /**
     * 两个线程分别执行
     */
    static double result1 = 0.0,result2=0.0,result=0.0;
    private static void t2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i=0;i< nums.length/2;i++) {
                result1 += nums[i];
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i=nums.length/2;i< nums.length;i++) {
                result2 += nums[i];
            }
        });
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();;
        t2.join();
        result = result1+result2;
        long end = System.currentTimeMillis();
        System.out.println("两个线程执行耗时:"+(end-start)+"毫秒");
    }

    /**
     * 是不是线程数越多越好？
     * 事实证明不是
     */
    private static void t3() throws InterruptedException {
        final int threadCount = 10000;
        Thread[] threads = new Thread[threadCount];
        double[] results = new double[threadCount];
        final  int segCount = nums.length / threadCount;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i=0;i<threadCount;i++) {
            int m = i;
            threads[i] = new Thread(() ->{
                for (int j = m *segCount; j<(m+1)*segCount && j< nums.length;j++) {
                    results[m] += nums[j];
                }
                countDownLatch.countDown();
            });
        }

        //启动所有线程
        long start = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        countDownLatch.await();
        //计算所有结果
        double total = 0.0;
        for (double r : results) {
            total += r;
        }
        long end = System.currentTimeMillis();
        System.out.println(threadCount+"个线程执行耗时:"+(end-start)+"毫秒");

    }

    public static void main(String[] args) throws Exception{
        t1();
        t2();
        t3();
    }


    /**
     * 工作线程数设置多少合适？
     *
     * 1、实践压测，结合机器的配置不断压测得出具体的数据，一般这个线程数跟CPU的核数有关系
     *    这里并非说每个CPU核 分配一个或两个线程就是最好的，考虑到CPU除了执行我们的应用程序还需要执行其他的程序(比如系统)，所以我们的
     *    应用程序对CPU的利用不会说达到100%，另外从安全的角度来说也也需要让CPU有些许空余的余量。
     * 2、理论公式
     *      N(threads) = N(cpu) * U(cpu) * (1+ W/C)
     *
     *      N(cpu)= cpu个数
     *      U(cpu)= cpu利用率
     *      W/C= 等待实践/计算时间
     *
     *     其中 W/C 如何获取 ?
     *     借助一些性能分析工具
     *     JProfiler性能分析
     *     在线分析诊断工具Arthas (阿里)
     *     分布式工具
     *
     *
     */
}
