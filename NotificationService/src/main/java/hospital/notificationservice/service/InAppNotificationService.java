package hospital.notificationservice.service;

import hospital.notificationservice.dto.inappnotification.InAppNotificationCreateDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationResponseDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationUpdateDto;
import hospital.notificationservice.model.enums.NotificationType;

import java.util.List;

/**
 * Service interface for In-App Notification management.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each notification is linked to a specific user via userId</li>
 *   <li>Notifications can be marked as read/unread</li>
 *   <li>type categorizes the notification for different handling logic</li>
 *   <li>relatedId links to the business entity (appointment, prescription, etc.)</li>
 *   <li>Users can filter notifications by type and read status</li>
 * </ul>
 *
 * @author MobinaRahi
 */
public interface InAppNotificationService {

    /**
     * Creates a new in-app notification.
     * Initial status is unread.
     *
     * @param dto the notification creation data
     * @return the created notification
     */
    InAppNotificationResponseDto createNotification(InAppNotificationCreateDto dto);

    /**
     * Gets a notification by its ID.
     *
     * @param id the notification ID
     * @return the notification
     */
    InAppNotificationResponseDto getNotificationById(Long id);

    /**
     * Gets all notifications.
     *
     * @return list of all notifications
     */
    List<InAppNotificationResponseDto> getAllNotifications();

    /**
     * Gets notifications for a specific user.
     *
     * @param userId the user ID from AuthService
     * @return list of notifications for the user
     */
    List<InAppNotificationResponseDto> getNotificationsByUser(Long userId);

    /**
     * Gets unread notifications for a specific user.
     *
     * @param userId the user ID from AuthService
     * @return list of unread notifications
     */
    List<InAppNotificationResponseDto> getUnreadNotificationsByUser(Long userId);

    /**
     * Gets notifications by type.
     *
     * @param type the notification type
     * @return list of notifications with the type
     */
    List<InAppNotificationResponseDto> getNotificationsByType(NotificationType type);

    /**
     * Gets notifications for a user filtered by type.
     *
     * @param userId the user ID
     * @param type   the notification type
     * @return list of matching notifications
     */
    List<InAppNotificationResponseDto> getNotificationsByUserAndType(Long userId, NotificationType type);

    /**
     * Gets notifications related to a specific entity.
     *
     * @param relatedId the related entity ID
     * @return list of notifications for the entity
     */
    List<InAppNotificationResponseDto> getNotificationsByRelatedId(Long relatedId);

    /**
     * Marks a notification as read.
     * Sets isRead=true and readAt=now.
     *
     * @param id the notification ID
     * @return the updated notification
     */
    InAppNotificationResponseDto markAsRead(Long id);

    /**
     * Updates an existing notification.
     *
     * @param id  the notification ID
     * @param dto the update data
     * @return the updated notification
     */
    InAppNotificationResponseDto updateNotification(Long id, InAppNotificationUpdateDto dto);

    /**
     * Soft-deletes a notification.
     *
     * @param id the notification ID
     */
    void deleteNotification(Long id);

    /**
     * Counts unread notifications for a user.
     *
     * @param userId the user ID
     * @return number of unread notifications
     */
    long countUnreadNotificationsByUser(Long userId);
}
