package com.itheima.ts_06_juc;

import java.util.concurrent.locks.StampedLock;

/**
 * ReadWriteLock支持两种模式：读锁和写锁
 *
 *  StampedLock支持三种模式模式：
 *
 *  写锁
 *  悲观读锁
 *  乐观读（无锁）
 *
 */
public class C06_StampedLock {

    private final StampedLock sl = new StampedLock();

    //写锁
    public void write(Object v) {
        //获取写锁
        long stamp = sl.writeLock();
        try {
            //省略相关业务代码

        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // 悲观读锁
    public void pessimisticRead() {
        long stamp = sl.readLock();
        try {
            //省略相关业务代码

        } finally {
            sl.unlockRead(stamp);
        }
    }


    // 乐观读 见 官方提供的 Point
    public void optimisticRead() {

    }
}

// 摘自StampedLock 官方文档示例
class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) { // an exclusively locked method
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }
    // 乐观读操作
    double distanceFromOrigin() { // A read-only method
        //乐观读
        long stamp = sl.tryOptimisticRead();
        // 读到局部变量中
        double currentX = x, currentY = y;
        // 判断执行读操作期间，是否存在写操作，如果存在，则sl.validate返回false
        if (!sl.validate(stamp)) {
            // 需要重新读取数据，升级为悲观读锁
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                //释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    // 悲观读锁升级为写锁
    void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                }
                else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }


    public static void main(String[] args) {
        Point p = new Point();
        p.moveIfAtOrigin(1.0,2.0);

    }
}
