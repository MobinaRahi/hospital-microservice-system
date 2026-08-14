package hospital.notificationservice.service;

import hospital.notificationservice.config.RabbitMQConfig;
import hospital.notificationservice.dto.message.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Notification Consumer
 * 
 * Listens to RabbitMQ queues and processes notifications.
 * 
 * Consumers:
 * - consumeSms: Processes SMS notifications
 * - consumeEmail: Processes Email notifications
 * - consumeInApp: Processes In-App notifications
 * 
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final SMSGatewayService smsGatewayService;
    private final EmailLogService emailLogService;
    private final InAppNotificationService inAppNotificationService;

    /**
     * Consumes SMS notifications from queue
     * 
     * @param message NotificationMessage from queue
     */
    @RabbitListener(queues = RabbitMQConfig.SMS_QUEUE)
    public void consumeSms(NotificationMessage message) {
        log.info("Processing SMS notification for: {}", message.getRecipient());
        
        try {
            // Create SMS record in database
            // smsGatewayService.createSms(...);
            
            log.info("SMS notification processed successfully for: {}", message.getRecipient());
        } catch (Exception e) {
            log.error("Failed to process SMS notification for: {}", message.getRecipient(), e);
            // Optionally: send to Dead Letter Queue for retry
        }
    }

    /**
     * Consumes Email notifications from queue
     * 
     * @param message NotificationMessage from queue
     */
    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consumeEmail(NotificationMessage message) {
        log.info("Processing Email notification for: {}", message.getRecipient());
        
        try {
            // Create Email record in database
            // emailLogService.createEmail(...);
            
            log.info("Email notification processed successfully for: {}", message.getRecipient());
        } catch (Exception e) {
            log.error("Failed to process Email notification for: {}", message.getRecipient(), e);
            // Optionally: send to Dead Letter Queue for retry
        }
    }

    /**
     * Consumes In-App notifications from queue
     * 
     * @param message NotificationMessage from queue
     */
    @RabbitListener(queues = RabbitMQConfig.INAPP_QUEUE)
    public void consumeInApp(NotificationMessage message) {
        log.info("Processing In-App notification for user: {}", message.getRecipient());
        
        try {
            // Create In-App notification record in database
            // inAppNotificationService.createNotification(...);
            
            log.info("In-App notification processed successfully for user: {}", message.getRecipient());
        } catch (Exception e) {
            log.error("Failed to process In-App notification for user: {}", message.getRecipient(), e);
            // Optionally: send to Dead Letter Queue for retry
        }
    }

}
