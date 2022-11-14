package com.zj.datajpa.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfiguration {


    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                4,
                3,
                TimeUnit.SECONDS,
//                new ArrayBlockingQueue<Runnable>(3),
                new LinkedBlockingQueue<Runnable>(3),
//                new SynchronousQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        return threadPool;
    }
}
