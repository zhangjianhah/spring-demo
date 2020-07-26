package com.zj.dataredis.controller;


import com.zj.dataredis.constants.GlobalData;
import com.zj.dataredis.entity.Student;
import com.zj.dataredis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequestMapping("/redis")
@RestController
public class RedisController {

    private static int ExpireTime = 60;   // redis中存储的过期时间60s

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;



    @RequestMapping("set")
    public Boolean redisset(){
        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentName("zhangsan");
        student.setAddr("江苏");
//        student.setCreateTime(new Date());
//        student.setDelflag(false);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);

        redisUtil.set("studentList",studentList);


        //return redisUtil.set(key,userEntity,ExpireTime);

//        return redisUtil.set("stuednt",student);
        redisTemplate.boundHashOps("map").put("studentList",studentList);
//        redisTemplate.boundValueOps();
//        redisTemplate.opsForHash()

        return null;
    }

    @RequestMapping("get")
    public Object redisget(String key){
        key = "stuednt";
        key = "stuedntList";
//        Student student = (Student) redisUtil.get(key);
//        List<Student> studentList = (List<Student>) redisUtil.get(key);
        List<Student> studentList = (List<Student>) redisTemplate.boundHashOps("map").get("studentList");
        System.out.println(studentList);



        return null;
    }

    @RequestMapping("test")
    public void test(String key){

        while (true){

            System.out.println("---------listern init-----------------");
            Object result =  redisTemplate.boundListOps("email").rightPop(10, TimeUnit.SECONDS);

            if(result != null){
                System.out.println("---------start get-----------------");
                System.out.println(result.toString());
                System.out.println("----------end get---------");
            }
        }




    }

    @RequestMapping("expire")
    public boolean expire(String key){
        return redisUtil.expire(key,ExpireTime);
    }



    /**
     * 最简单的消息队列
     * 从左边塞，从右边拉
     * 1.考虑轮询的耗时，所以可以用brpop,没有消息或者获取达到一定时间会阻塞
     * 2.长时间的阻塞会导致连接断开，所以redis的超时时间必须短于阻塞时间
     */
    @RequestMapping(value = "/lpush")
    public String simpleQueue(){
        String res = UUID.randomUUID().toString();
        System.out.println("塞入字符串");
        redisTemplate.boundListOps(GlobalData.getEmailQueueWait()).leftPush(res);

        return null;
    }



    /**
     * 最简单的消息队列
     * 从左边塞，从右边拉
     * 1.考虑轮询的耗时，所以可以用brpop,没有消息或者获取达到一定时间会阻塞
     * 2.长时间的阻塞会导致连接断开，所以redis的超时时间必须短于阻塞时间
     */
    @RequestMapping(value = "/pubSub")
    public String pubSub(){

        Student student = new Student();
        student.setStudentId(1L);
        student.setStudentName("zhangsan");
        student.setAddr("江苏");

        redisTemplate.convertAndSend("testChannel1",952);
        redisTemplate.convertAndSend("testChannel2",student);

        return null;
    }
}