package hospital.notificationservice.repository;

import hospital.notificationservice.model.SMSGateway;
import hospital.notificationservice.model.enums.SmsStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for SMSGateway entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByStatus - SMS messages by status</li>
 *   <li>findByTo - SMS messages for a specific phone number</li>
 *   <li>findPendingMessages - Messages waiting to be sent</li>
 *   <li>findFailedMessages - Messages that failed to send</li>
 *   <li>countByStatus - Count messages by status</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface SMSGatewayRepository extends BaseEntityRepository<SMSGateway, Long> {

    /**
     * Finds SMS messages by status.
     *
     * @param status the SMS status
     * @return list of SMS messages with the status
     */
    List<SMSGateway> findByStatus(SmsStatus status);

    /**
     * Finds SMS messages for a specific phone number.
     *
     * @param to the phone number
     * @return list of SMS messages sent to the number
     */
    List<SMSGateway> findByTo(String to);

    /**
     * Finds pending messages that need to be sent.
     *
     * @return list of pending SMS messages
     */
    @Query("SELECT s FROM SMSGateway s WHERE s.status = 'PENDING' AND s.deleted = false ORDER BY s.createdAt ASC")
    List<SMSGateway> findPendingMessages();

    /**
     * Finds messages that failed to send.
     *
     * @return list of failed SMS messages
     */
    List<SMSGateway> findByStatusAndDeletedFalse(SmsStatus status);

    /**
     * Finds messages sent within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of messages in the range
     */
    List<SMSGateway> findBySentAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds messages by provider.
     *
     * @param provider the SMS provider name
     * @return list of messages sent by the provider
     */
    List<SMSGateway> findByProvider(String provider);

    /**
     * Finds pending messages for a specific provider.
     *
     * @param provider the SMS provider name
     * @return list of pending messages for the provider
     */
    List<SMSGateway> findByProviderAndStatus(String provider, SmsStatus status);

    /**
     * Counts messages by status.
     *
     * @param status the SMS status
     * @return number of messages with the status
     */
    long countByStatus(SmsStatus status);

    /**
     * Finds messages by template ID.
     *
     * @param templateId the notification template ID
     * @return list of messages using the template
     */
    List<SMSGateway> findByTemplateId(Long templateId);
}
