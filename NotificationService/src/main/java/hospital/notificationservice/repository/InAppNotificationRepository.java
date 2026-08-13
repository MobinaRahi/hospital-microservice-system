package hospital.notificationservice.repository;

import hospital.notificationservice.model.InAppNotification;
import hospital.notificationservice.model.enums.NotificationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for InAppNotification entity.
 *
 * <p><strong>Custom Queries:</strong></p>
 * <ul>
 *   <li>findByUserId - Notifications for a specific user</li>
 *   <li>findUnreadByUserId - Unread notifications for a user</li>
 *   <li>findByType - Notifications by type</li>
 *   <li>findRecentNotifications - Recent notifications</li>
 *   <li>countUnreadByUserId - Count unread notifications for a user</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Repository
public interface InAppNotificationRepository extends BaseEntityRepository<InAppNotification, Long> {

    /**
     * Finds all notifications for a specific user.
     *
     * @param userId the user ID from AuthService
     * @return list of notifications for the user
     */
    List<InAppNotification> findByUserId(Long userId);

    /**
     * Finds unread notifications for a specific user.
     *
     * @param userId the user ID from AuthService
     * @return list of unread notifications
     */
    @Query("SELECT n FROM InAppNotification n WHERE n.userId = :userId AND n.isRead = false AND n.deleted = false ORDER BY n.createdAt DESC")
    List<InAppNotification> findUnreadByUserId(@Param("userId") Long userId);

    /**
     * Finds notifications by type.
     *
     * @param type the notification type
     * @return list of notifications with the type
     */
    List<InAppNotification> findByType(NotificationType type);

    /**
     * Finds notifications for a user filtered by type.
     *
     * @param userId the user ID
     * @param type   the notification type
     * @return list of matching notifications
     */
    List<InAppNotification> findByUserIdAndType(Long userId, NotificationType type);

    /**
     * Finds recent notifications (created within the last N hours).
     *
     * @param hoursAgo number of hours to look back
     * @return list of recent notifications
     */
    @Query("SELECT n FROM InAppNotification n WHERE n.createdAt >= :cutoff AND n.deleted = false ORDER BY n.createdAt DESC")
    List<InAppNotification> findRecentNotifications(@Param("cutoff") LocalDateTime hoursAgo);

    /**
     * Finds notifications related to a specific entity.
     *
     * @param relatedId the related entity ID
     * @return list of notifications for the entity
     */
    List<InAppNotification> findByRelatedId(Long relatedId);

    /**
     * Finds unread notifications for a user related to a specific entity.
     *
     * @param userId    the user ID
     * @param relatedId the related entity ID
     * @return list of matching notifications
     */
    List<InAppNotification> findByUserIdAndRelatedIdAndIsReadFalse(Long userId, Long relatedId);

    /**
     * Counts unread notifications for a user.
     *
     * @param userId the user ID
     * @return number of unread notifications
     */
    long countByUserIdAndIsReadFalse(Long userId);

    /**
     * Finds notifications by user ID ordered by creation date.
     *
     * @param userId the user ID
     * @return list of notifications ordered by date
     */
    List<InAppNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
