package com.itheima.ts_05_cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS
 * 自旋
 * 底层的：
 * lock cmpxchgl 指令
 */
public class C00_KnowCAS {

    public static void main(String[] args) {
        AtomicInteger count = new AtomicInteger();
        count.incrementAndGet();
    }
}
