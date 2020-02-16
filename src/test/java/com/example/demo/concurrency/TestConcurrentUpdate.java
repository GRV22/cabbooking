package com.example.demo.concurrency;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestConcurrentUpdate {

    @Test
    public void startListOfThreadsTest() {
        Mutation obj = new Mutation();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; ++i) {
            new Thread("Thread name : " + i) {
                @Override
                public void run() {
                    synchronized (obj) {
                        int val = obj.getVal();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(val);
                        obj.setVal(val + 1);
                    }
                }
            }.start();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time in ms" + (endTime - startTime));
    }

    @Test
    public void startExecutorTest() throws InterruptedException {
        Mutation obj = new Mutation();
        long startTime = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService service = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 100; ++i) {
            service.submit(new Thread("Thread name : " + i) {
                @Override
                public void run() {
                    synchronized (obj) {
                        int val = obj.getVal();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(val);
                        obj.setVal(val + 1);
                        countDownLatch.countDown();
                    }
                }
            });
        }
        countDownLatch.await(20, TimeUnit.MILLISECONDS);
        service.shutdown();
        while (!service.isShutdown()) {
        }
        System.out.println("Count down latch : " + countDownLatch.getCount());
        long endTime = System.currentTimeMillis();
        System.out.println("Total time in ms" + (endTime - startTime));
    }

}
