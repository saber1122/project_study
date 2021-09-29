package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用ReadWriteLock实现一个通用缓存工具类
 *
 */
public class C05_ReadWriteLock {

    private static Cache<String,String> cache = new Cache<>();

    /**
     * 验证读写锁ReadWriteLock 的三个原则
     * - 允许多个线程同时读共享变量；但读的时候禁止写
     *
     * - 只允许一个线程写共享变量；
     *
     * - 如果一个写线程正在执行写操作，此时禁止读线程读共享变量（由第二条可知自然也禁止其他线程写）。
     */
    public static void main(String[] args) throws Exception{
        int numsOfThread = 100;
        CountDownLatch c1 = new CountDownLatch(1);
        for (int i=0;i<numsOfThread;i++) {
            String finalI = i+"";
            if (i % 2 ==0) {
                new Thread(() -> {
                    try {
                        c1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cache.put(finalI,finalI);

                }, finalI).start();
            }else {
                new Thread(() -> {
                    try {
                        c1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cache.get(finalI);
                }, finalI).start();
            }

        }
        c1.countDown();

        ThreadUtil.sleepSeconds(5);


        String s = cache.get2("aaa");
    }

}
class Cache<K,V> {

    //缓存数据的存储
    private final Map<K,V> cache = new HashMap<>();

    // 构建 ReadWriteLock
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    //读锁
    private final Lock read = rwl.readLock();
    //写锁
    private final Lock write = rwl.writeLock();


    //向缓存中
    public V put(K k,V v) {
        write.lock();
        //System.out.println(Thread.currentThread().getName()+" get write lock");
        try {
            return cache.put(k, v);
        } finally {
            //System.out.println(Thread.currentThread().getName()+" release write lock");
            write.unlock();
        }
    }

    // 从缓存中读数据
    public V get(K k) {
        read.lock();
        //System.out.println(Thread.currentThread().getName()+" get read lock");
        try {
            return cache.get(k);
        } finally {
            //System.out.println(Thread.currentThread().getName()+" release read lock");
            read.unlock();
        }
    }



    // 读写锁嵌套：读锁升级写锁     读锁未释放无法获取写锁

    public V get2(K k) {
        V v = null;
        read.lock();
        try {
            v = cache.get(k);
            if (v == null) {
                try {
                    //从数据库查询并写入缓存
                    write.lock();
                    v = queryFromDb(k);
                    v = cache.put(k,v);
                } finally {
                    write.unlock();
                }
            }
        } finally {
            read.unlock();
        }
        return v;
    }

    /**
     * 这样看上去好像是没有问题的，先是获取读锁，然后再升级为写锁，对此还有个专业的名字，叫锁的升级。
     * 可惜 ReadWriteLock 并不支持这种升级。在上面的代码示例中，读锁还没有释放，此时获取写锁，会导致写锁永久等待，
     * 最终导致相关线程都被阻塞，永远也没有机会被唤醒。锁的升级是不允许的，这个一定要注意
     *
     * 但是写锁降级为读锁是可以的，见下方的官方示例
     */

    private V queryFromDb(K k) {
        //省略从数据库查询的代码
        return null;
    }
}
// 摘自 ReentrantReadWriteLock 官方示例文档
class CachedData  {
    Object data;
    volatile boolean cacheValid;
    final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    void processCachedData() {
        rwl.readLock().lock();
        if (!cacheValid) {
            // Must release read lock before acquiring write lock
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                if (!cacheValid) {
                    // data = ... ; // 查询数据
                    cacheValid = true;
                }
                // Downgrade by acquiring read lock before releasing write lock
                rwl.readLock().lock();
            } finally {
                rwl.writeLock().unlock(); // Unlock write, still hold read
            }
        }

        try {
            // use(data);//使用数据
        } finally {
            rwl.readLock().unlock();
        }
    }
}
