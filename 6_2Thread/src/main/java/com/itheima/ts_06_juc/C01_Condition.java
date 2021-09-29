package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class C01_Condition {

    public static void main(String[] args) {
        MyBlockingQueue<String> queue = new MyBlockingQueue<>(10);
        new Thread(()->{
            for(int i=0;i<100;i++) {
                String e = "ele"+i;
                queue.offer(e);
                System.out.println("offer "+e);
            }
        }).start();
        ThreadUtil.sleepSeconds(1);
        new Thread(()->{
            while (true) {
                String s = queue.poll();
                System.out.println("poll "+s);
            }
        }).start();
    }
}

class MyBlockingQueue<E> {
    final Queue<E> queue;
    private int queuSize;
    final Lock lock = new ReentrantLock();

    //队列已满的条件变量
    final Condition notFull = lock.newCondition();
    //队列为空的条件变量
    final Condition notEmpty = lock.newCondition();

    //构建大小为：numElements 的有界队列
    public MyBlockingQueue(int numElements){
        queue = new ArrayDeque<E>(numElements);
        queuSize = numElements;
    }

    public boolean offer2(E e) {
        return queue.offer(e);
    }

    public E poll2() {
        return queue.poll();
    }

    //元素入队列
    public boolean offer(E e) {
        lock.lock();
        boolean offer;
        try {
            //如果队列已满,等待
            while (queue.size() == queuSize) {
                try {
                    notFull.await();
                } catch (InterruptedException i) {

                }
            }
            //执行入队列操作
            offer = queue.offer(e);
            // 入队列后通知可出队列
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
        return offer;
    }

    //元素出队列
    public E poll() {
        E e = null;
        lock.lock();
        try {
            //队列为空,等待
            while (queue.isEmpty()) {
                try {
                    notEmpty.await();
                } catch (InterruptedException i) {
                    i.printStackTrace();
                }
            }
            //执行元素出队列 操作
            e = queue.poll();
            //出队列后通知可入队列、
            notFull.signal();
        } finally {
            lock.unlock();
        }
        return e;
    }
}
