package hospital.notificationservice.service;

import hospital.notificationservice.dto.smsgateway.SMSGatewayCreateDto;
import hospital.notificationservice.dto.smsgateway.SMSGatewayResponseDto;
import hospital.notificationservice.model.enums.SmsStatus;

import java.util.List;

/**
 * Service interface for SMS Gateway management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each SMS must have a recipient phone number and message content</li>
 *   <li>SMS status workflow: PENDING → SENT → DELIVERED (or FAILED)</li>
 *   <li>Provider and providerMessageId are set when SMS is sent via gateway</li>
 *   <li>Cost is tracked per message for billing purposes</li>
 *   <li>Failed messages store error message for debugging</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface SMSGatewayService {

    /**
     * Creates a new SMS message.
     * Initial status is PENDING.
     *
     * @param dto the SMS creation data
     * @return the created SMS
     */
    SMSGatewayResponseDto createSms(SMSGatewayCreateDto dto);

    /**
     * Gets an SMS by its ID.
     *
     * @param id the SMS ID
     * @return the SMS
     */
    SMSGatewayResponseDto getSmsById(Long id);

    /**
     * Gets all SMS messages.
     *
     * @return list of all SMS messages
     */
    List<SMSGatewayResponseDto> getAllSms();

    /**
     * Gets SMS messages by status.
     *
     * @param status the SMS status
     * @return list of SMS messages with the status
     */
    List<SMSGatewayResponseDto> getSmsByStatus(SmsStatus status);

    /**
     * Gets SMS messages for a specific phone number.
     *
     * @param to the phone number
     * @return list of SMS messages sent to the number
     */
    List<SMSGatewayResponseDto> getSmsByRecipient(String to);

    /**
     * Gets pending SMS messages waiting to be sent.
     *
     * @return list of pending SMS messages
     */
    List<SMSGatewayResponseDto> getPendingSms();

    /**
     * Gets failed SMS messages.
     *
     * @return list of failed SMS messages
     */
    List<SMSGatewayResponseDto> getFailedSms();

    /**
     * Marks an SMS as sent.
     * Sets status to SENT and records sentAt timestamp.
     *
     * @param id                the SMS ID
     * @param providerMessageId the message ID from the SMS gateway provider
     * @return the updated SMS
     */
    SMSGatewayResponseDto markAsSent(Long id, String providerMessageId);

    /**
     * Marks an SMS as delivered.
     * Sets status to DELIVERED and records deliveredAt timestamp.
     *
     * @param id the SMS ID
     * @return the updated SMS
     */
    SMSGatewayResponseDto markAsDelivered(Long id);

    /**
     * Marks an SMS as failed.
     * Sets status to FAILED and stores the error message.
     *
     * @param id           the SMS ID
     * @param errorMessage the error message describing the failure
     * @return the updated SMS
     */
    SMSGatewayResponseDto markAsFailed(Long id, String errorMessage);

    /**
     * Cancels a pending SMS.
     *
     * @param id the SMS ID
     * @return the cancelled SMS
     */
    SMSGatewayResponseDto cancelSms(Long id);

    /**
     * Soft-deletes an SMS.
     *
     * @param id the SMS ID
     */
    void deleteSms(Long id);

    /**
     * Counts SMS messages by status.
     *
     * @param status the SMS status
     * @return number of SMS messages with the status
     */
    long countSmsByStatus(SmsStatus status);
}
