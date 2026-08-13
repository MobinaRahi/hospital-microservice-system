package hospital.notificationservice.service;

import hospital.notificationservice.dto.emaillog.EmailLogCreateDto;
import hospital.notificationservice.dto.emaillog.EmailLogResponseDto;
import hospital.notificationservice.model.enums.EmailStatus;

import java.util.List;

/**
 * Service interface for Email Log management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each email must have a recipient address, subject, and body</li>
 *   <li>Email status workflow: PENDING → SENT → DELIVERED → OPENED (or FAILED)</li>
 *   <li>TemplateId links to NotificationTemplate for reusable content</li>
 *   <li>Failed emails store error message for debugging</li>
 *   <li>OPENED status requires email tracking pixel support</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface EmailLogService {

    /**
     * Creates a new email log entry.
     * Initial status is PENDING.
     *
     * @param dto the email creation data
     * @return the created email log
     */
    EmailLogResponseDto createEmail(EmailLogCreateDto dto);

    /**
     * Gets an email log by its ID.
     *
     * @param id the email log ID
     * @return the email log
     */
    EmailLogResponseDto getEmailById(Long id);

    /**
     * Gets all email logs.
     *
     * @return list of all email logs
     */
    List<EmailLogResponseDto> getAllEmails();

    /**
     * Gets email logs by status.
     *
     * @param status the email status
     * @return list of email logs with the status
     */
    List<EmailLogResponseDto> getEmailsByStatus(EmailStatus status);

    /**
     * Gets email logs for a specific recipient.
     *
     * @param to the recipient email address
     * @return list of email logs sent to the recipient
     */
    List<EmailLogResponseDto> getEmailsByRecipient(String to);

    /**
     * Gets pending emails waiting to be sent.
     *
     * @return list of pending email logs
     */
    List<EmailLogResponseDto> getPendingEmails();

    /**
     * Gets failed emails.
     *
     * @return list of failed email logs
     */
    List<EmailLogResponseDto> getFailedEmails();

    /**
     * Gets unopened emails (sent but not opened).
     *
     * @return list of unopened emails
     */
    List<EmailLogResponseDto> getUnopenedEmails();

    /**
     * Marks an email as sent.
     * Sets status to SENT and records sentAt timestamp.
     *
     * @param id the email log ID
     * @return the updated email log
     */
    EmailLogResponseDto markAsSent(Long id);

    /**
     * Marks an email as delivered.
     * Sets status to DELIVERED.
     *
     * @param id the email log ID
     * @return the updated email log
     */
    EmailLogResponseDto markAsDelivered(Long id);

    /**
     * Marks an email as opened.
     * Sets status to OPENED.
     *
     * @param id the email log ID
     * @return the updated email log
     */
    EmailLogResponseDto markAsOpened(Long id);

    /**
     * Marks an email as failed.
     * Sets status to FAILED and stores the error message.
     *
     * @param id           the email log ID
     * @param errorMessage the error message describing the failure
     * @return the updated email log
     */
    EmailLogResponseDto markAsFailed(Long id, String errorMessage);

    /**
     * Soft-deletes an email log.
     *
     * @param id the email log ID
     */
    void deleteEmail(Long id);

    /**
     * Counts email logs by status.
     *
     * @param status the email status
     * @return number of email logs with the status
     */
    long countEmailsByStatus(EmailStatus status);
}
