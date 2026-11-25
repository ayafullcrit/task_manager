package com.phong.testsec.service;

import com.phong.testsec.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationProducer
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendTaskNotification(String taskTitle)
    {
        String message = "Một task vừa được tạo với tiêu đề là '" + taskTitle + "'";
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE,"", message);
        System.out.println("Đã ném thư vào bưu điện: " + message);
    }
}
