package com.zj.datajpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.*;

@Service
public class DemoService {

    @Autowired
    private ThreadPoolExecutor executorService;

    public  void test(){


        Future<?> submit = executorService.submit(() -> {
            System.out.println("----");
        });


//        CompletableFuture.runAsync()
        System.out.println("start----------------");
        CompletableFuture.runAsync(()->{
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep-------------");
        },executorService);
        System.out.println("end-------------");
    }
}
