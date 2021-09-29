package com.itheima.ts_06_juc;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * 使用 StampedLock 的注意事项
 * 1、StampedLock 不支持重入
 * 2、StampedLock 的悲观读锁、写锁都不支持条件变量
 * 3、如果线程阻塞在 StampedLock 的 readLock() 或者 writeLock() 上时，此时调用该阻塞线程的 interrupt() 方法，会导致 CPU 飙升
 * 4、需要支持中断功能，一定使用可中断的悲观读锁 readLockInterruptibly() 和写锁 writeLockInterruptibly()
 */
public class C07_StampedAttention {

    final static StampedLock lock  = new StampedLock();

    public static void main(String[] args) throws Exception{
        Thread T1 = new Thread(()->{
            // 获取写锁
            lock.writeLock();
            // 永远阻塞在此处，不释放写锁
            LockSupport.park();
        });
        T1.start();
        // 保证T1获取写锁
        Thread.sleep(100);

        Thread T2 = new Thread(()->
            //阻塞在悲观读锁
            lock.readLock()
        );
        T2.start();
        // 保证T2阻塞在读锁
        Thread.sleep(100);

        //中断线程T2
        //会导致线程T2所在CPU飙升
        T2.interrupt();
        T2.join();
    }
}
