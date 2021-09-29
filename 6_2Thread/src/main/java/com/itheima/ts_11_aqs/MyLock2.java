package com.itheima.ts_11_aqs;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于 aqs实现锁
 */
public class MyLock2 implements Lock {

    //同步器
    private Sync syn ;

    MyLock2 () {
        syn = new NoFairSync();
    }

    MyLock2 (boolean fair) {
        syn = fair ? new FairSync():new NoFairSync();
    }


    @Override
    public void lock() {
        //调用模板方法
        syn.acquire(1);
    }

    @Override
    public void unlock() {
        //调用模板方法
        syn.release(1);
    }

    // Lock接口其他方法暂时先不实现
    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return false;
    }

    @NotNull
    @Override
    public Condition newCondition() {
        return null;
    }


    // 实现一个独占同步器
    class Sync extends AbstractQueuedSynchronizer{


        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean realRelease = false;
            int nextState = getState() - arg;
            if (nextState == 0) {
                realRelease = true;
                setExclusiveOwnerThread(null);
            }
            setState(nextState);
            return realRelease;
        }

    }

    class FairSync extends Sync {
        @Override
        protected boolean tryAcquire(int arg) {
            final Thread currentThread = Thread.currentThread();
            int currentState = getState();
            if (currentState == 0 ) { // 可以获取锁
                //先判断等待队列中是否有线程在排队 没有线程排队则直接去获取锁
                if (!hasQueuedPredecessors() && compareAndSetState(0,arg)) {
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            }else if (currentThread == getExclusiveOwnerThread()) {
                //重入逻辑 增加 state值
                int nextState = currentState + arg;
                if (nextState < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextState);
                return true;
            }
            return false;
        }
    }

    class NoFairSync extends Sync {
        @Override
        protected boolean tryAcquire(int arg) {
            final Thread currentThread = Thread.currentThread();
            int currentState = getState();
            if (currentState ==0 ) { // 可以获取锁
                //直接去获取锁
                if (compareAndSetState(0,arg)) {
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            }else if (currentThread == getExclusiveOwnerThread()) {
                //重入逻辑 增加 state值
                int nextState = currentState + arg;
                if (nextState < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextState);
                return true;
            }
            return false;
        }
    }

}
