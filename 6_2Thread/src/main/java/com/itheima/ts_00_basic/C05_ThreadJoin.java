package com.itheima.ts_00_basic;

import com.itheima.util.ThreadUtil;

/**
 * 线程 join
 */
public class C05_ThreadJoin {

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(()->{
            System.out.println("t1 线程开始执行了,5s后执行结束");
            ThreadUtil.sleepSeconds(5);
        });
        t1.start();
        System.out.println("主线程等待t1线程执行结束");
        t1.join();
        System.out.println("t1 线程执行结束了");
    }
}

