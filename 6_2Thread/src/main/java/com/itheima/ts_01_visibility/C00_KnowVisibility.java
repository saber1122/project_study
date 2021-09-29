package com.itheima.ts_01_visibility;

import com.itheima.util.ThreadUtil;

/**
 * 从一个小程序开始认识可见性：Visibility
 * 线程之间的可见性，一个线程修改的状态对另一个线程是可见的
 */
public class C00_KnowVisibility {

    // 多线程共享变量
    private  static volatile boolean running =  true;


    private static void t1()  {
        new Thread(()->{
            while (running) {
                //System.out.println("eat eat eat ");
                //ThreadUtil.sleepSeconds(1);
            }
            System.out.println("thread exit");
        }).start();

        ThreadUtil.sleepSeconds(5);
        running = false;

    }



    // volatile 能保障可见性但是无法保障原子性，线程安全无法保障

    private static volatile int count = 0;

    public static void t2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                count++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                count++;
            }
        });
        //启动两个线程
        t1.start();
        t2.start();

        //等待两个线程执行结束
        t1.join();
        t2.join();
        //输出count的最终结果
        System.out.println("count="+count);
        /**
         * 可能在某一时刻，t1和t2 从主内存中读到了相同的count值=100，然后经过工作内存操作之后为101，t1和t2 将工作内存中的101刷到
         * 主内存，虽然刷新了两次但是最终的结果还是101。
         */
    }



    // volatile 修饰引用类型，只能保证该引用是可见的，对于所引用对象的属性是不可见的
    static class A {
        /*volatile*/ boolean stop = false;

        private  void t3() {
            while (!stop) {

            }
            System.out.println("program stopped");
        }
    }

    private static volatile A a = new A();

    private static void t4() {
        new Thread(a::t3,"t1").start();
        ThreadUtil.sleepSeconds(5);
        a.stop = true;

    }



    // synchronized 也能保证可见性

    private static boolean run = true;
    private static void t5() {
        new Thread(()->{
            while (run) {
                /*synchronized (C00_KnowVisibility.class) {

                }*/
                ThreadUtil.sleepSeconds(1);
            }
            System.out.println("thread exit");
        }).start();

        ThreadUtil.sleepSeconds(5);
        run = false;
    }



    public static void main(String[] args) throws Exception {
        //t1();
        t2();
        //t4();
        //t5();
    }
}
