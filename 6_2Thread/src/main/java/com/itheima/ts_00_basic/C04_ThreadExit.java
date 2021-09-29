package com.itheima.ts_00_basic;


import com.itheima.util.ThreadUtil;

/**
 * 如何优雅的结束一个线程？
 *  ex：某线程正在执行一个任务，如何优雅的停止退出
 */
public class C04_ThreadExit {

    // 1、stop
    public static void e1() {
        Thread t1 = new Thread(() ->{
            while (true) {
                System.out.println(" thread running");
                ThreadUtil.sleepSeconds(1);
            }
        });
        t1.start();

        ThreadUtil.sleepSeconds(5);
        //粗暴退出
        t1.stop();
    }

    //2、suspend  resume
    public static void e2() {
        Thread t1 = new Thread(() ->{
            while (true) {
                System.out.println(" thread running");
                ThreadUtil.sleepSeconds(1);
            }
        });
        t1.start();

        ThreadUtil.sleepSeconds(5);
        t1.suspend();//暂停  如果持有锁是不会释放的 有死锁的风险
        ThreadUtil.sleepSeconds(5);
        t1.resume();//恢复
    }



    /**
     * 不足之处在于两点，
     * 一是没办法做精细控制，比如，变量`i`到增到100后退出，像这种依赖某些其他中间变量的状态来退出的，都无法很精细的控制；
     * 二是时间上也不可控，如果在循环过程中有一些阻塞方法一直未结束阻塞，即使共享变量`stop`变为true了，也无法及时退出。
     */

    // 3、volatile 共享变量 (标志位)  但很难做精细控制
    private static volatile boolean stop = false;
    public static void e3() {
        Thread t3 = new Thread(()->{
            long i= 0;
            while (!stop) {
                 //do something
                // wait recv accept
                i++;
            }
            System.out.println("variable i = "+i); // 20970844839  20651746222
        });
        t3.start();

        ThreadUtil.sleepSeconds(5);
        stop = true;
    }


    // 4、interrupt
    public static void e4() {
        Thread t4 = new Thread(()->{
            while (!Thread.interrupted()) {

            }
            System.out.println(" the end ");
        });
        t4.start();
        ThreadUtil.sleepSeconds(5);
        t4.interrupt();

    }


    public static void main(String[] args) {
        // e1();
        // e2();
        // e3();
        e4();
    }

}
