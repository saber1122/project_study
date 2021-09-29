package com.itheima.ts_10_reference_threadlocal;

import com.itheima.util.ThreadUtil;

/**
 * 详解 threadlocal
 *
 */
public class C04_ThreadLocal {

    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        // 认识 threadlocal
        Thread t1 = new Thread(() -> {
            ThreadUtil.sleepSeconds(1);
            try {
                User user = new User();
                tl.set(user);
                System.out.println("t1 set user="+tl.get());
            }  finally {
                ThreadUtil.sleepSeconds(2);
                tl.remove();
            }
        });

        Thread t2 = new Thread(() -> {
            ThreadUtil.sleepSeconds(2);
            User user = tl.get();
            System.out.println("t2 get user="+user);
        });

        //启动两个线程
        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }


    //查看 threadlocal 的原理
    static ThreadLocal<User> t =  new ThreadLocal<User>();
    public static void threadlocal() {
        t.set(new User());
        t.get();
        t.remove();
    }

}
