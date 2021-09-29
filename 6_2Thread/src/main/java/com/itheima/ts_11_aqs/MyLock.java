package com.itheima.ts_11_aqs;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 自定义一个 SDK 层面的锁
 */
public class MyLock {
    // 定义一个状态变量status：为1表示锁被持有，为0表示锁未被持有
    private volatile int status;

    private static final Unsafe unsafe = reflectGetUnsafe();
    private static final long valueOffset;

    private static final Queue<Thread> QUEUE = new LinkedBlockingQueue<>();

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (MyLock.class.getDeclaredField("status"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private static Unsafe reflectGetUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 阻塞式获取锁
     * @return
     */
    public boolean lock() {
        while (!compareAndSet(0,1)) {
            //Thread.yield();//yield+自旋,尽可能的防止CPU空转,让出CPU资源
            //ThreadUtil.sleepSeconds(1);//sleep+自旋， 时间不可控
            QUEUE.offer(Thread.currentThread());
            LockSupport.park();//线程休眠
        }
        return true;
    }

    // cas 设置 status
    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    /**
     * 释放锁
     */
    public void unlock() {
        status = 0;
        LockSupport.unpark(QUEUE.poll());
    }
}
