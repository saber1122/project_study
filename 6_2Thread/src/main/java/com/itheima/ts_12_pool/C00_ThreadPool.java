package com.itheima.ts_12_pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 理解java中的线程池其实是一种：生产者-消费者 模式
 */
public class C00_ThreadPool {

    //利用阻塞队列实现生产者-消费者模式
    BlockingQueue<Runnable> workQueue;

    //保存内部工作线程
    List<WorkerThread> threads  = new ArrayList<>();

    // 构造线程池
    C00_ThreadPool(int poolSize ){
        this.workQueue = new LinkedBlockingQueue<>(poolSize);
        // 创建工作线程
        for(int idx=0; idx<poolSize; idx++){
            WorkerThread work = new WorkerThread("ThreadPool-"+idx);
            work.start();
            threads.add(work);
        }
    }


    // 提交任务到线程池
    void execute(Runnable command){
        try {
            workQueue.put(command);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 工作线程负责消费任务，并执行任务
    class WorkerThread extends Thread{
        WorkerThread(){};
        WorkerThread(String name){
            super(name);
        }

        public void run() {
            //循环取任务并执行
            while(true){
                Runnable task = null;
                try {
                    task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        C00_ThreadPool pool = new C00_ThreadPool(3);
        for (int i=0;i<10;i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" is working");
                }
            });
        }
    }

}
