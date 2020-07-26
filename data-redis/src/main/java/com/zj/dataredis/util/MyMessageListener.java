package com.zj.dataredis.util;

import com.zj.dataredis.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.lettuce.core.pubsub.RedisPubSubListener;

@Component
@Slf4j
public class MyMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info(message.toString());

        //使用string的反序列化方式获取channel
        String channel = (String) redisTemplate.getStringSerializer().deserialize(message.getChannel());
        if(StringUtils.equals(channel,"testChannel1")){
            // 获取消息
            byte[] body = message.getBody();
            // 使用值序列化器转换
            Integer msgBody = (Integer) redisTemplate.getValueSerializer().deserialize(body);
            log.info(msgBody.toString());
        }else{
            // 获取消息
            byte[] body = message.getBody();
            // 使用值序列化器转换
            Student msgBody = (Student) redisTemplate.getValueSerializer().deserialize(body);
            log.info(msgBody.toString());
        }


    }
}
