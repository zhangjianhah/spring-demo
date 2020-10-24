package com.zj.dataredis.service;


import com.zj.dataredis.constants.GlobalData;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 这是一个基于List的 LPUSH+BRPOP 的任务队列实现
 * 基本原理是：
 * 将待执行的消息从左边放入消息队列，不断从右边获取队列元素并执行
 *
 *
 */
@Component
@Slf4j
public class RedisLPQueueService {


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @PostConstruct 项目启动的时候，自动执行被该注解注解的方法
     * 初始执行任务
     */
    @PostConstruct
    public void startListernInit(){
        System.out.println("---------新线程开始-----------------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    //项目启动的时候，吧执行队列中的所有元素移入待执行队列以期望重新执行
                    List<Object> list = redisTemplate.boundListOps(GlobalData.getEmailQuequExecute()).range(0,-1);
//                    redisTemplate.boundListOps(GlobalData.getEmailQueueWait()).leftPushAll(list);
                    if(list != null && list.size() > 0){
                        list.stream().forEach(object->{
                            redisTemplate.boundListOps(GlobalData.getEmailQueueWait()).leftPush(object);
                        });
                    }
                    redisTemplate.delete(GlobalData.getEmailQuequExecute());

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
     *
     *
     * 把缓存从等待队列塞入执行队列
     * 执行完成后，再从执行队列移除
     * 执行失败应当移出队列，然后将其记录log
     *
     *
     * 对于在执行过程中因为断电/服务停止情况，应当视情况当做未执行或者执行失败处理
     *
     *
     */
    public void redisQueueListern(){
        while (true){
            // 本处rightPop()方法附带了参数，作用是在获取不到参数的时候阻塞之${timeout}(时间)${unit}(单位)
            //但是务必小心，阻塞时间不应当超过最大连接时间，否则需要重新连接并处理超时异常
            Object result =  redisTemplate.boundListOps(GlobalData.getEmailQueueWait()).rightPop(10, TimeUnit.SECONDS);
            if(result != null){
                redisTemplate.boundListOps(GlobalData.getEmailQuequExecute()).leftPush(result);
                try {
                    executeQueueWork(result);
                } catch (Exception e) {
                    log.error("执行失败"+result);
                    e.printStackTrace();
                }
                redisTemplate.boundListOps(GlobalData.getEmailQuequExecute()).remove(0,result);
                log.info("工作执行完毕");
            }
        }
    }




    public void executeQueueWork(Object o){
        log.info("开始"+o.toString());
    }
}
