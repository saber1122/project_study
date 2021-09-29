package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier：回环屏障
 *
 * 主要用于：线程间相互等待
 * CyclicBarrier是一种同步机制允许一组线程相互等待，等到所有线程都到达一个屏障点才退出await方法
 *
 * CyclicBarrier不会阻塞主线程，只会阻塞子线程
 *
 */
public class C09_CyclicBarrier {

    static void m1() {
        Random r = new Random();
        int numsOfThread = 10;
        // 带Runnable的构造,当所有线程都到达屏障点后优先执行该Runnable
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numsOfThread,()->{
            System.out.println("所有运动员已就位,枪响了");
        });
        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 已准备就绪");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 起跑");
                ThreadUtil.sleepSeconds(r.nextInt(numsOfThread)+1);
                System.out.println(Thread.currentThread().getName()+" 跑到了终点");
            }, "男子运动员"+i).start();
        }


        ThreadUtil.sleepSeconds(10);
        System.out.println("============女子组开始比赛===========");
        //重置cyclicBarrier  因为cyclicBarrier可以重复利用
        //cyclicBarrier.reset();//会自动重置

        for (int i=0;i<numsOfThread;i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 已准备就绪");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" 起跑");
                ThreadUtil.sleepSeconds(r.nextInt(numsOfThread)+1);
                System.out.println(Thread.currentThread().getName()+" 跑到了终点");
            }, "女子运动员"+i).start();
        }
    }


    public static void main(String[] args) {
        m1();
    }
}
