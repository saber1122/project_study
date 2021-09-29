package com.itheima.ts_10_reference_threadlocal;

import com.itheima.util.ThreadUtil;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用
 *  管理堆外内存/直接内存
 */
public class C03_PhantomReference {

    public static void main(String[] args) {
        ReferenceQueue<User> QUEUE = new ReferenceQueue<>();
        PhantomReference<User> p = new PhantomReference<>(new User(), QUEUE);
        System.out.println(p.get());
        //p=null;

        new Thread(()->{
            while (true) {
                byte[] b = new byte[1024*1024*4];
                b=null;
                ThreadUtil.sleepSeconds(1);
                System.out.println(p.get());
            }
        }).start();

        new Thread(()->{
            while (true) {
                Reference<? extends User> reference = QUEUE.poll();
                if (reference!=null) {
                    System.out.println("---虚引用对象被jvm回收了--"+reference);
                    break;
                }
            }
        }).start();
        ThreadUtil.sleepSeconds(5);
    }
}
