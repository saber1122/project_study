package com.itheima.ts_02_atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Lock-Resources Relationship
 * 锁和资源的关系
 *  1:N的关系
 *
 */


// 这样的加锁方式有什么问题 ?  add线程和sub线程之间是不可见的
class LR{

    static long i = 0L;

    synchronized void subOne() {
        i-=1;
    }

    static synchronized void addOne() {
        i+=1;
    }
}



// 锁粒度 对多个无关联的资源可以用细粒度的锁 各自分配各自的锁

class Account {
    // 锁：保护账户余额
    private final Object balLock = new Object();
    // 账户余额
    private  Integer balance;

    // 锁：保护账户密码
    private final Object pwLock = new Object();
    // 账户密码
    private String password;

    public Account(){};

    public Account(Integer balance) {
        this.balance = balance;
    }


    // 取款
    void withdraw(Integer amt) {
        synchronized(balLock) {
            if (this.balance > amt){
                this.balance -= amt;
            }
        }
    }
    // 查看余额
    Integer getBalance() {
        synchronized(balLock) {
            return balance;
        }
    }

    // 更改密码
    void updatePassword(String pw){
        synchronized(pwLock) {
            this.password = pw;
        }
    }
    // 查看密码
    String getPassword() {
        synchronized(pwLock) {
            return password;
        }
    }




    // 模拟转账操作
    void  transfer(Account target, int amt){
        if (this.balance > amt) {
            this.balance -= amt;//当前账号扣钱
            target.balance += amt;//目标账号加钱
        }
    }


    synchronized void  transfer1(Account target, int amt){
        if (this.balance > amt) {
            this.balance -= amt;//当前账号扣钱
            target.balance += amt;//目标账号加钱
        }
    }

    void  transfer2(Account target, int amt){
        synchronized (Account.class) {
            if (this.balance > amt) {
                this.balance -= amt;//当前账号扣钱
                target.balance += amt;//目标账号加钱
            }
        }
    }

    // 测试 transfer1 transfer2
    private static void test1() throws Exception{
        long start = System.currentTimeMillis();
        for (long i=0;i<100000;i++) {

            Account a = new Account(200);
            Account b = new Account(200);
            Account c = new Account(200);

            Thread t1 = new Thread(()->{
                a.transfer1(b,100); // 进入transfer1的锁对象是 a
                //a.transfer2(b,100);
            });
            Thread t2 = new Thread(()->{
                b.transfer1(c,100); // 进入transfer1的锁对象是 b
                //b.transfer2(c,100);
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();

            if (b.balance !=200 ) {
                System.out.println("oh 出错了!"+","+(System.currentTimeMillis() - start));
                break;
            }
        }
    }









    // 降低锁粒度 提升性能 但要预防死锁
    void  transfer3(Account target, int amt){
        synchronized (this) { //锁定转出账户
            synchronized (target) { // 锁定转入账户
                if (this.balance > amt) {
                    this.balance -= amt;
                    target.balance += amt;
                }
            }
        }
    }

    /**
     * 比较简单的能一眼看出来会有死锁问题的发生
     * 比如：
     *  当前账户this==a，tgrget==b
     *  在不同的线程中同时调用；
     *      a.transfer3(b,100)
     *      a.xxxOperation(b)
     *
     *      就会发生死锁问题
     *
     *  不太容易发现的死锁问题，见 test2()
     * @param target
     */
    void xxxOperation(Account target) {
        synchronized (target) {
            synchronized (this) {
                // operation
            }
        }
    }



    // 预防死锁1：破坏保持和等待  一次性申请所有的资源:变相的申请了一把能保护转出和转入账户的锁，

    void  transfer4(Account target, int amt){
        Allocator allocator = Allocator.getInstance();
        //一次性申请要操作的两个资源，如果申请不到相当于循环等待
        while (!allocator.apply(this,target)) {} ;

        try {
            synchronized (this) { //锁定转出账户
                synchronized (target) { // 锁定转入账户
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        } finally {
            //操作完成释放对应的资源
            allocator.free(this,target);
        }
    }



    /**
     * 预防死锁2：破坏不可被剥夺/抢占
     * 破坏不可抢占条件看上去很简单，核心是要能够主动释放它占有的资源，这一点 synchronized 是做不到的。
     * 原因是 synchronized 申请资源的时候，如果申请不到，线程直接进入阻塞状态了，而线程进入阻塞状态，啥都干不了，
     * 也释放不了线程已经占有的资源（即阻塞时不会释放已获取的锁）
     */

    /**
     * 预防死锁3：破坏循环等待
     * 破坏这个条件相对容易，需要对资源进行排序，然后按序申请资源，
     * 只要不出现  transfer3() xxxOperation() 这种写法即可
     */




    // 使用 等待-通知机制优化 预防死锁1：破坏保持和等待中的循环等待

    void  transfer5(Account target, int amt){
        Allocator allocator = Allocator.getInstance();
        //申请要操作的资源
        allocator.apply2(this,target);
        try {
            synchronized (this) { //锁定转出账户
                synchronized (target) { // 锁定转入账户
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        } finally {
            //操作完成释放对应的资源
            allocator.free2(this,target);
        }
    }



    // 测试 transfer3 / transfer4 方法 死锁的发生
    private static void test2() throws Exception{
        long start = System.currentTimeMillis();
        final int max_count = 100000;
        Account a = new Account(20000),b=new Account(20000);
        CountDownLatch latch = new CountDownLatch(max_count);
        for (int i=0;i<max_count;i++) {
            if (i % 2==0) {
                Thread t = new Thread(() -> {
                    //a.transfer3(b,100);
                    //a.transfer4(b,100);
                    a.transfer5(b,100);
                    latch.countDown();
                });
                //t.setPriority((i % 10)+1);
                t.start();
            }
            if (i % 2 ==1) {
                Thread t = new Thread(() -> {
                    //b.transfer3(a,100);
                    //b.transfer4(a,100);
                    b.transfer5(a,100);
                    latch.countDown();
                });
                // t.setPriority((i % 10)+1);
                t.start();
            }

        }
        latch.await();
        System.out.println("耗时:"+(System.currentTimeMillis()-start)+"毫秒");

    }


    public static void main(String[] args) throws Exception{
        test1();
        //test2();
    }



}

// Allocator 应该为单例
class Allocator {
    private static volatile Allocator instance;
    private Allocator() {}

    public static Allocator getInstance(){
        if (instance == null) {
            synchronized (Allocator.class)  {
                if (instance == null) {
                    instance = new Allocator();
                }
            }
        }
        return instance;
    }

    private List<Object> als =  new ArrayList<>();

    // 一次性申请所有资源
    synchronized boolean apply(Object from, Object to){
        if(als.contains(from) || als.contains(to)){
            return false;//某一资源已被占用
        } else {
            als.add(from);
            als.add(to);
        }
        return true;
    }

    // 归还资源
    synchronized void free( Object from, Object to){
        als.remove(from);
        als.remove(to);
    }


    // 使用 等待---通知机制 优化； Allocator是单例的，this 作为互斥锁

    synchronized void apply2(Object from, Object to){
        //经典写法，这里为什么要用wile？用if行不行？
        while (als.contains(from) || als.contains(to)) {
            try {
                this.wait();
            } catch (InterruptedException e) {
            }
        }
        als.add(from);
        als.add(to);
    }
    synchronized void free2( Object from, Object to){
        als.remove(from);
        als.remove(to);
        this.notifyAll();
        //this.notify();//随机唤醒，先看优先级，然后随机
    }
}


public class C03_LrRelationship {


}
