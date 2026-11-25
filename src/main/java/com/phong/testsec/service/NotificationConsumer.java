package com.phong.testsec.service;

import com.phong.testsec.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NotificationConsumer
{
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void processEmail(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException
    {
        System.out.println("📧 [Email Worker] Nhận được yêu cầu: " + message);
        try
        {
            Thread.sleep(3000);
            if(message.contains("lỗi")) throw new RuntimeException("Server gửi mail bị sập!");
            System.out.println("   -> Đang gửi Email cho người dùng...");
            channel.basicAck(tag, false);
        }
        catch(Exception e)
        {
            System.err.println("❌ Có lỗi xảy ra: " + e.getMessage());
            channel.basicNack(tag, false, false);
            System.err.println("⚰️ Đã tống cổ bức thư vào Nghĩa địa (DLQ)!");
        }
    }

    @RabbitListener(queues = RabbitMQConfig.POINT_QUEUE)
    public void processPoint(String message) {
        System.out.println("🎁 [Point Worker] Nhận được yêu cầu: " + message);
        System.out.println("   -> Đã cộng +10 điểm vào tài khoản!");
    }
}
