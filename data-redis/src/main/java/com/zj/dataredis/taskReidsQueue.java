package com.zj.dataredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class taskReidsQueue {

    @Autowired
    private RedisTemplate redisTemplate;


    @Scheduled(cron = "0 0 2 * * ?")
    public void clearEmailExecuteQueue(){
        //每隔一段时间，检查执行队列中是否存在有一些不在

    }
}
