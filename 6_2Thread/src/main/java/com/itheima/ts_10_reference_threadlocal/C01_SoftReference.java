package com.itheima.ts_10_reference_threadlocal;


import com.itheima.util.ThreadUtil;

import java.lang.ref.SoftReference;

/**
 * 软引用
 *
 */
public class C01_SoftReference {
    // 做实验时 设置heap最大20M，-Xmx20M
    public static void main(String[] args) {
        SoftReference<byte[]> s = new SoftReference<>(new byte[1024*1024*10]);
        //获取软引用的数据
        System.out.println(s.get());
        //gc
        System.gc();
        ThreadUtil.sleepSeconds(2);
        //再次获取软引用的数据
        System.out.println(s.get());
        //在堆中再次分配一个byte[] 导致heap分配不下，此时会触发垃圾回收，如果空间不够会把软引用的内存回收
        byte[] bytes = new byte[1024*1024*12];
        System.out.println(s.get());
    }
    // 软引用一般主要应用到缓存上
}
