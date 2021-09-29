package com.itheima.ts_10_reference_threadlocal;

import java.lang.ref.WeakReference;

/**
 * 弱引用
 *
 */
public class C02_WeakReference {
    public static void main(String[] args) {
        //创建弱引用
        WeakReference<User> w = new WeakReference<>(new User());
        //获取弱引用的对象
        System.out.println(w.get());
        System.gc();
        // 被弱引用所引用的对象，如果在没有被其他强引用多引用的情况下会被回收。
        System.out.println(w.get());


    }
}
