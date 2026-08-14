package hospital.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Configuration for NotificationService.
 *
 * <p><strong>Architecture:</strong></p>
 * <ul>
 *   <li>Direct Exchange: notification.exchange</li>
 *   <li>Queues: notification.sms.queue, notification.email.queue, notification.inapp.queue</li>
 *   <li>Routing Keys: sms.send, email.send, inapp.send</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Configuration
public class RabbitMQConfig {

    /** Direct exchange for all notification messages. */
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";

    /** Queue for SMS notifications. */
    public static final String SMS_QUEUE = "notification.sms.queue";

    /** Queue for Email notifications. */
    public static final String EMAIL_QUEUE = "notification.email.queue";

    /** Queue for In-App notifications. */
    public static final String INAPP_QUEUE = "notification.inapp.queue";

    /** Routing key for SMS messages. */
    public static final String SMS_ROUTING_KEY = "sms.send";

    /** Routing key for Email messages. */
    public static final String EMAIL_ROUTING_KEY = "email.send";

    /** Routing key for In-App messages. */
    public static final String INAPP_ROUTING_KEY = "inapp.send";

    /**
     * Creates Direct Exchange.
     *
     * @return DirectExchange instance
     */
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    /**
     * Creates SMS Queue (durable, not auto-delete).
     *
     * @return Queue instance
     */
    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(SMS_QUEUE).build();
    }

    /**
     * Creates Email Queue (durable, not auto-delete).
     *
     * @return Queue instance
     */
    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    /**
     * Creates In-App Queue (durable, not auto-delete).
     *
     * @return Queue instance
     */
    @Bean
    public Queue inAppQueue() {
        return QueueBuilder.durable(INAPP_QUEUE).build();
    }

    /**
     * Binds SMS Queue to Exchange with routing key.
     *
     * @param smsQueue the SMS queue
     * @param notificationExchange the notification exchange
     * @return Binding instance
     */
    @Bean
    public Binding smsBinding(Queue smsQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(smsQueue).to(notificationExchange).with(SMS_ROUTING_KEY);
    }

    /**
     * Binds Email Queue to Exchange with routing key.
     *
     * @param emailQueue the Email queue
     * @param notificationExchange the notification exchange
     * @return Binding instance
     */
    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(emailQueue).to(notificationExchange).with(EMAIL_ROUTING_KEY);
    }

    /**
     * Binds In-App Queue to Exchange with routing key.
     *
     * @param inAppQueue the In-App queue
     * @param notificationExchange the notification exchange
     * @return Binding instance
     */
    @Bean
    public Binding inAppBinding(Queue inAppQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(inAppQueue).to(notificationExchange).with(INAPP_ROUTING_KEY);
    }

    /**
     * RabbitTemplate for sending messages.
     * Uses default SimpleMessageConverter (no deprecation issues).
     *
     * @param connectionFactory the connection factory
     * @return RabbitTemplate instance
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

}