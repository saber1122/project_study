package com.itheima.ts_02_atomic;

/**
 * 线程切换带来的原子性问题
 */
public class C00_KnowAtomic {

    static class AtomicRunnable implements Runnable {
        int i = 0;

        @Override
        public void run() {
            i+=1;
            System.out.println("---"+i);
        }
    }

    public static void main(String[] args) {
        AtomicRunnable runnable = new AtomicRunnable();
        for (int i=0;i<100000;i++) {
            new Thread(runnable).start();
        }
    }

}
