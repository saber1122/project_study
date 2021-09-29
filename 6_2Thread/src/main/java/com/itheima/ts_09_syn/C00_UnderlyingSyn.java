package com.itheima.ts_09_syn;

import com.itheima.util.ThreadUtil;
import org.openjdk.jol.info.ClassLayout;

/**
 * 认识 synchronized 的底层
 */
public class C00_UnderlyingSyn  {

    // 从字节码层面认识 synchronized---->查看该方法生成的字节码
    public static void synClass() {
        Object obj = new Object();
        synchronized (obj) {

        }
    }

    // 从JVM层面认识 synchronized
    public static void synJvm() {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());

        }
    }

    // 未使用过锁的状态 hashcode
    public static void noSyn() {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        //如果调用了hashcode
        int hashCode = obj.hashCode();
        System.out.println(Integer.toBinaryString(hashCode));
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());

    }

    // 偏向未启动，synchronized默认 轻量级锁
    public static void lightweightSyn() {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }


    // 等待偏向锁启动，synchronized上偏向锁 如果有线程竞争，撤销偏向锁，升级轻量级锁
    public static void biasedSyn() {
        ThreadUtil.sleepSeconds(5);//也可添加启动参数 -XX:BiasedLockingStartupDelay=0 可立即启动，但不建议
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }
    // 等待启动偏向锁，但是如果调用了hashcode，则无法使用偏向锁 原因是markword中存了hashcode后没位置存偏向锁线程id了
    public static void hashBiasedSyn() {
        ThreadUtil.sleepSeconds(5);
        Object o = new Object();
        System.out.println("hashcode="+Integer.toBinaryString(o.hashCode()));;
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
            System.out.println("hashcode="+Integer.toBinaryString(o.hashCode()));;
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }



    // 锁消除 append方法本身是添加了synchronized的，但sb变量是线程私有的不会发生竞争
    public static void lockEliminate() {
        StringBuffer sb = new StringBuffer();
        sb.append("hello").append("ts");
    }
    // 锁粗化
    public static String lockCoarsening() {
        int i=0;
        StringBuffer sb = new StringBuffer();
        while (i<100) {
            sb.append(i);
            i++;
        }
        return sb.toString();
    }



    // 查看 synchronized 对应的汇编指令
    // 1,下载插件：hsdis-amd64.dll，放到%JAVA_HOME%\jre\bin\server下，
    // 2，运行需要添加参数：  -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
    static volatile int count = 0;
    static void m1() {
        for (int i=0;i<10000;i++) {
            m2();
            m3(i);
        }
    }

    private synchronized static void m2() {

    }

    private static void m3(int i) {
        count = i;
    }






    // 测试 synchronized 是否是可重入的：结论，synchronized可重入的
    static void reeentrantOfSyn() {
        Object o = new Object();
        synchronized (o) {
            System.out.println("first get lock");
            ThreadUtil.sleepSeconds(2);
            synchronized (o) {
                System.out.println("second get lock");
            }
        }
    }

    //测试 synchronized 是否是公平的
    static void fairOfSyn() {
        Object o = new Object();
        int num = 10;
        Thread[] threads = new Thread[num];
        for (int i=0;i<num;i++) {
            threads[i] = new Thread(()->{
                synchronized (o) {
                    ThreadUtil.sleepMillisSeconds(100);
                    System.out.println(Thread.currentThread().getName()+" get lock");
                }
            },"thread"+i);
        }
        synchronized (o) {
            for (int i=0;i<num;i++) {
                threads[i].start();
                ThreadUtil.sleepMillisSeconds(200);
            }
        }
        ThreadUtil.sleepSeconds(10);

    }

    public static void main(String[] args) {
        //noSyn();
        //lightweightSyn();
        //biasedSyn();
        //hashBiasedSyn();
        //synJvm();
        //lockCoarsening();
        //m1();
        //reeentrantOfSyn();
        fairOfSyn();
    }
}
