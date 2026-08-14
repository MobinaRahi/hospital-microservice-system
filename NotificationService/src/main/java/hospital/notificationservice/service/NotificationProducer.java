package hospital.notificationservice.service;

import hospital.notificationservice.config.RabbitMQConfig;
import hospital.notificationservice.dto.message.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Notification Producer
 * 
 * Sends messages to RabbitMQ queues for async processing.
 * 
 * Usage:
 * - Other services can inject this and call sendSms(), sendEmail(), sendInApp()
 * - Messages are queued and processed by NotificationConsumer
 * 
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Sends SMS notification to queue
     * 
     * @param message NotificationMessage with recipient phone and body
     */
    public void sendSms(NotificationMessage message) {
        message.setType("SMS");
        message.setCreatedAt(java.time.LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFICATION_EXCHANGE,
            RabbitMQConfig.SMS_ROUTING_KEY,
            message
        );
        
        log.info("SMS notification sent to queue for recipient: {}", message.getRecipient());
    }

    /**
     * Sends Email notification to queue
     * 
     * @param message NotificationMessage with recipient email, subject, and body
     */
    public void sendEmail(NotificationMessage message) {
        message.setType("EMAIL");
        message.setCreatedAt(java.time.LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFICATION_EXCHANGE,
            RabbitMQConfig.EMAIL_ROUTING_KEY,
            message
        );
        
        log.info("Email notification sent to queue for recipient: {}", message.getRecipient());
    }

    /**
     * Sends In-App notification to queue
     * 
     * @param message NotificationMessage with userId and message body
     */
    public void sendInApp(NotificationMessage message) {
        message.setType("IN_APP");
        message.setCreatedAt(java.time.LocalDateTime.now());
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.NOTIFICATION_EXCHANGE,
            RabbitMQConfig.INAPP_ROUTING_KEY,
            message
        );
        
        log.info("In-App notification sent to queue for user: {}", message.getRecipient());
    }

}
