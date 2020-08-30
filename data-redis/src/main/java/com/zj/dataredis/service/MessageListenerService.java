package com.zj.dataredis.service;


import com.zj.dataredis.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 这是一个基于PUB/SUB机制的消息队列
 * 实现方式为：
 * 1.在配置项配置订阅监听的频道
 * 2.实现MessageListener接口，重写onMessage()方法
 * 3.通过convertAndSend()向指定频道发送信息
 * 这样redis收到消息后，会通知监听的频道
 *
 */

@Component
@Slf4j
public class MessageListenerService  implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * @param message sub收到的消息，其中包含channel和消息内容
     * @param pattern 其实就是sub收到的通信频道，这里点重复了都
     */
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
