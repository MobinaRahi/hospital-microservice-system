package hospital.notificationservice.repository;

import hospital.notificationservice.model.EmailLog;
import hospital.notificationservice.model.enums.EmailStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for EmailLog entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByStatus - Email logs by status</li>
 *   <li>findByTo - Email logs for a specific recipient</li>
 *   <li>findPendingEmails - Emails waiting to be sent</li>
 *   <li>findFailedEmails - Emails that failed to send</li>
 *   <li>countByStatus - Count emails by status</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface EmailLogRepository extends BaseEntityRepository<EmailLog, Long> {

    /**
     * Finds email logs by status.
     *
     * @param status the email status
     * @return list of email logs with the status
     */
    List<EmailLog> findByStatus(EmailStatus status);

    /**
     * Finds email logs for a specific recipient.
     *
     * @param to the recipient email address
     * @return list of emails sent to the recipient
     */
    List<EmailLog> findByTo(String to);

    /**
     * Finds pending emails that need to be sent.
     *
     * @return list of pending email logs
     */
    @Query("SELECT e FROM EmailLog e WHERE e.status = 'PENDING' AND e.deleted = false ORDER BY e.createdAt ASC")
    List<EmailLog> findPendingEmails();

    /**
     * Finds emails that failed to send.
     *
     * @return list of failed email logs
     */
    @Query("SELECT e FROM EmailLog e WHERE e.status = 'FAILED' AND e.deleted = false ORDER BY e.sentAt DESC")
    List<EmailLog> findFailedEmails();

    /**
     * Finds emails sent within a date range.
     *
     * @param startDate start of the range
     * @param endDate   end of the range
     * @return list of emails in the range
     */
    List<EmailLog> findBySentAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds emails by template ID.
     *
     * @param templateId the notification template ID
     * @return list of emails using the template
     */
    List<EmailLog> findByTemplateId(Long templateId);

    /**
     * Finds unopened emails (sent but not opened).
     *
     * @return list of unopened emails
     */
    @Query("SELECT e FROM EmailLog e WHERE e.status IN ('SENT', 'DELIVERED') AND e.deleted = false")
    List<EmailLog> findUnopenedEmails();

    /**
     * Counts emails by status.
     *
     * @param status the email status
     * @return number of emails with the status
     */
    long countByStatus(EmailStatus status);
}
