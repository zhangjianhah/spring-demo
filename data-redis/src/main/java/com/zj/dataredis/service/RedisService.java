package com.zj.dataredis.service;

import com.zj.dataredis.constants.GlobalData;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 这是最简单的lpop/push队列
     */
    @PostConstruct
    public void startListernInit(){
        System.out.println("---------新线程开始-----------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    redisQueueListern();
                } catch (Exception e) {
                    redisQueueListern();
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("---------新线程开启完毕-----------------");
    }


    /**
     * 监听任务队列并执行
     * 1.这里并没有对执行失败的任务进行处理，而是简单的抛出，具体情况具体分析即可
     * 2.如果本身消费者是异步的，那么应当先把队列中元素丢到执行队列中，由执行队列先获取执行，执行成功再排出
     */
    public void redisQueueListern(){
        while (true){
            Object result =  redisTemplate.boundListOps(GlobalData.getEmailQueueWait()).rightPop(10, TimeUnit.SECONDS);
            if(result != null){

                try {
                    executeQueueWork(result);
                } catch (Exception e) {
                    log.error("执行失败"+result);
                    e.printStackTrace();
                }
                log.info("工作执行完毕");
            }
        }
    }




    public void executeQueueWork(Object o){
        log.info("开始");
    }
}
