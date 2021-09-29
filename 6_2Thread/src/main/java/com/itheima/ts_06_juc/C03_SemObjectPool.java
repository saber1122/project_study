package com.itheima.ts_06_juc;

import com.itheima.util.ThreadUtil;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 利用计数信号量实现对象池限流操作
 */
public class C03_SemObjectPool {
    public static void main(String[] args) throws Exception {
        //创建对象池
        ObjectPool<Bbd,String> pool = new ObjectPool(10,Bbd.class);
        int numsOfThread = 1000;
        CountDownLatch c = new CountDownLatch(numsOfThread);
        for (int i=0;i<numsOfThread;i++) {
            int finalI = i;
            new Thread(()->{
                //向对象池提交任务
                String s = pool.execute((obj) -> {
                    System.out.println("从对象池中获取的对象"+obj);
                    ThreadUtil.sleepSeconds(1);
                    return obj.hurt("唐三"+ finalI);
                });
                System.out.println(s);
                c.countDown();
            }).start();
        }

        c.await();
    }
}
class ObjectPool<T,R> {

    //对象池
    private final List<T> pool;

    // 用信号量实现限流器
    private final  Semaphore SEMAPHORE;

    ObjectPool(int poolSize,Class<?> clas) {
        pool = new Vector<>(poolSize); //能否换成ArrayList
        //按照给定的大小初始化对象池
        for (int i=0;i<poolSize;i++) {
            try {
                pool.add((T) clas.newInstance());
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            }
        }
        //创建计数信号量
        SEMAPHORE = new Semaphore(poolSize);
    }

    /**
     * 向对象池中提交任务,由对象池中的对象来执行
     * @param function
     * @return
     */
    R execute(Function<T,R> function) {
        T obj = null;
        try {
            //获得信号量
            SEMAPHORE.acquire();
            try {
                //从对象池中获取对象
                obj = pool.remove(0);
                // 执行对象操作
                return function.apply(obj);
            } finally {
                //将对象归还池
                pool.add(obj);
                //释放信号量
                SEMAPHORE.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
// 比比东
class Bbd {

    public String hurt(String name) {
        String r = "hury you " +name;
        return r;
    }

}
