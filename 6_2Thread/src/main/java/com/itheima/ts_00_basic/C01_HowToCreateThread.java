package com.itheima.ts_00_basic;

import java.util.concurrent.*;

/**
 * 面试题：创建线程有多少种方法？
 */
public class C01_HowToCreateThread {

    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("MyThread "+Thread.currentThread().getName()+" is running!");
        }
    }


    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            System.out.println("MyTask "+Thread.currentThread().getName()+" is running");
        }
    }


    public static void main(String[] args) throws Exception {
        // 方式1：继承Thread
        MyThread thread1 = new MyThread();
        thread1.start();

        // 方式2：实现Runnable
        Thread thread2 = new Thread(new MyRunnable());
        thread2.start();

        // 方式3：lambda
        Thread thread3 = new Thread(() -> {
            System.out.println("lambda runnable "+Thread.currentThread().getName()+" is running");
        });
        thread3.start();

        // 方式4：使用 ThreadPoll 中的线程

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        // 4.1：提交Runnable任务到线程池中执行 无返回值，调用线程中获取不到异步线程的执行结果，除非通过共享变量
        executorService.execute(()->{
            System.out.println("ExecutorService Thead "+Thread.currentThread().getName()+" is running");
        });

        // 4.2：提交 Callable任务，结合Future，拿到返回值,
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("Callable task "+Thread.currentThread().getName()+" is running");
                return "success";
            }
        });
        String asyncResult = future.get();
        System.out.println("asyncResult is " +asyncResult);


        // 4.3：不使用线程池,通过new Thread 并获得异步线程的返回值，使用 FutureTask
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("FutureTask Callable task "+Thread.currentThread().getName()+" is running");
                return "FutureTask success";
            }
        });
        Thread thread4 = new Thread(futureTask);
        thread4.start();

        String futureTaskResult = futureTask.get();
        System.out.println("futureTaskResult is " +futureTaskResult);
        /**
         * public class FutureTask<V> implements RunnableFuture<V>{...}
         *
         * public interface RunnableFuture<V> extends Runnable, Future<V> {...}
         *
         * FutureTask既可以执行，又能将执行结果装到自己里面
         *
         *
         *  其实：向线程池通过submit提交Callable任务时，底层也是将其包装成一个FutureTask，可查看源码
         *  java.util.concurrent.AbstractExecutorService#submit(java.util.concurrent.Callable<T>)
         */

        executorService.shutdown();
    }




}
