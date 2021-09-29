package com.itheima.ts_11_aqs;

import com.itheima.util.ThreadUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {


    static int count = 0;

    // 测试锁的正确性
    static void test1() throws Exception{
        long start = System.currentTimeMillis();
        int nums = 10000;
        Thread[] threads = new Thread[nums];
        CountDownLatch latch = new CountDownLatch(1);
        MyLock lock = new MyLock();
        Lock lock2 = new MyLock2();

        for (int i=0;i<nums;i++) {
            threads[i] = new Thread(()->{
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //lock.lock();
                //lock2.lock();
                try {
                    for (int j = 0;j<100000;j++) {
                        count++;
                    }
                } finally {
                    //lock.unlock();
                    //lock2.unlock();
                }

            });
            threads[i].start();
        }
        latch.countDown();

        //等待执行结束
        for (Thread t:threads) {
            t.join();
        }
        System.out.println("count="+count+",time="+(System.currentTimeMillis()-start)+" ms");
    }


    /**
     * 测试锁的公平性  但是这个输出结果是不可靠的，不具备验证是否公平的能力
     * 比如：
     * 0 1 2 三个线程
     * 0获取到锁，1，2并发来抢锁，此时等待队列没有等待线程，1，2都可以直接获取锁，通过CAS操作，有可能2先获取锁，
     * @throws Exception
     */
    static void test2() throws Exception{
        int nums = 50;
        Thread[] threads = new Thread[nums];
        Lock lock = new MyLock2(true);
        Lock rl = new ReentrantLock(true);
        for (int i=0;i<nums;i++) {
            threads[i] = new Thread(()->{
                //lock.lock();
                rl.lock();
                ThreadUtil.sleepMillisSeconds(100);
                System.out.println(Thread.currentThread().getName()+" get lock");
                try {
                    for (int j = 0;j<100000;j++) {
                        count++;
                    }
                } finally {
                    //lock.unlock();
                    rl.unlock();
                }

            },"thread-"+i);
            threads[i].start();
            //ThreadUtil.sleepMillisSeconds(1);
        }

        //等待执行结束
        for (Thread t:threads) {
            t.join();
        }
    }


    // 测试锁的可重入性
    static Lock lock = new MyLock2(true);
    static int value = 0;
    static void test3() {
        lock.lock();
        try {
            System.out.println("test3 get lock,then do something ");
            for (int i=0;i<10000;i++) {
                value++;
            }
            test4();
        } finally {
            lock.unlock();
        }
    }

    static void test4() {
        lock.lock();
        try {
            System.out.println("test4 get lock,then do something ");
            for (int i=0;i<10000;i++) {
                value++;
            }
        } finally {
            lock.unlock();
        }
    }
    static void testReentrant() throws Exception{
        int nums = 5000;
        Thread[] threads = new Thread[nums];
        Lock rl = new ReentrantLock(true);
        for (int i=0;i<nums;i++) {
            threads[i] = new Thread(()->{
                test3();
            });
            threads[i].start();
        }
        //等待执行结束
        for (Thread t:threads) {
            t.join();
        }
        System.out.println("value="+value);
    }

    public static void main(String[] args) throws Exception{
        //test1();
        //test2();
        testReentrant();
    }
}
