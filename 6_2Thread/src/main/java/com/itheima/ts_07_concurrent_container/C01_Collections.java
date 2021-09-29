package com.itheima.ts_07_concurrent_container;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 认识 Collections
 *
 */
public class C01_Collections {

    static void t1() {
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        Set<Object> set = Collections.synchronizedSet(new HashSet<>());
        Map<Object, Object> map = Collections.synchronizedMap(new HashMap<>());
    }

    // 单个操作虽然是原子的，但是组合操作并不能保证原子性，需要自己来保证
    static List<String> list = Collections.synchronizedList(new ArrayList<String>());
    static boolean addIfAbsent(String name) {
        if (!list.contains(name)) {
            list.add(name);
            return true;
        }
        return false;
    }
    static void test() throws Exception{
        int num = 10000;
        Thread[] threads = new Thread[num];
        CountDownLatch c = new CountDownLatch(1);
        for (int i=0;i<num;i++) {
            threads[i] = new Thread(()->{
                try {
                    c.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                addIfAbsent("唐僧老师");
            });
            threads[i].start();
        }

        c.countDown();
        for (Thread t:threads) {
            t.join();
        }
        System.out.println(list);
    }

    public static void main(String[] args) throws Exception{
        test();
    }
}
