package hospital.notificationservice.service.impl;

import hospital.notificationservice.dto.inappnotification.InAppNotificationCreateDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationResponseDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationUpdateDto;
import hospital.notificationservice.exception.inappnotification.AlreadyReadNotificationException;
import hospital.notificationservice.exception.inappnotification.InAppNotificationNotFoundException;
import hospital.notificationservice.mapper.InAppNotificationMapper;
import hospital.notificationservice.model.InAppNotification;
import hospital.notificationservice.model.enums.NotificationType;
import hospital.notificationservice.repository.InAppNotificationRepository;
import hospital.notificationservice.service.InAppNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link InAppNotificationService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InAppNotificationServiceImpl implements InAppNotificationService {

    private final InAppNotificationRepository inAppNotificationRepository;
    private final InAppNotificationMapper inAppNotificationMapper;

    @Override
    public InAppNotificationResponseDto createNotification(InAppNotificationCreateDto dto) {
        log.info("Creating notification for user: {}", dto.getUserId());

        InAppNotification notification = inAppNotificationMapper.toEntity(dto);
        InAppNotification saved = inAppNotificationRepository.save(notification);
        log.info("Notification created with id: {}", saved.getId());

        return inAppNotificationMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InAppNotificationResponseDto getNotificationById(Long id) {
        log.debug("Fetching notification by id: {}", id);

        InAppNotification notification = inAppNotificationRepository.findNotDeletedById(id)
                .orElseThrow(() -> InAppNotificationNotFoundException.byId(id));

        return inAppNotificationMapper.toResponseDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getAllNotifications() {
        log.debug("Fetching all notifications");

        List<InAppNotification> notifications = inAppNotificationRepository.findAllNotDeleted();
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getNotificationsByUser(Long userId) {
        log.debug("Fetching notifications for user: {}", userId);

        List<InAppNotification> notifications = inAppNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getUnreadNotificationsByUser(Long userId) {
        log.debug("Fetching unread notifications for user: {}", userId);

        List<InAppNotification> notifications = inAppNotificationRepository.findUnreadByUserId(userId);
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getNotificationsByType(NotificationType type) {
        log.debug("Fetching notifications by type: {}", type);

        List<InAppNotification> notifications = inAppNotificationRepository.findByType(type);
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getNotificationsByUserAndType(Long userId, NotificationType type) {
        log.debug("Fetching notifications for user: {} and type: {}", userId, type);

        List<InAppNotification> notifications = inAppNotificationRepository.findByUserIdAndType(userId, type);
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InAppNotificationResponseDto> getNotificationsByRelatedId(Long relatedId) {
        log.debug("Fetching notifications by related id: {}", relatedId);

        List<InAppNotification> notifications = inAppNotificationRepository.findByRelatedId(relatedId);
        return inAppNotificationMapper.toResponseDtoList(notifications);
    }

    @Override
    public InAppNotificationResponseDto markAsRead(Long id) {
        log.info("Marking notification as read: {}", id);

        InAppNotification notification = inAppNotificationRepository.findNotDeletedById(id)
                .orElseThrow(() -> InAppNotificationNotFoundException.byId(id));

        if (notification.isReadNotification()) {
            throw AlreadyReadNotificationException.byId(id);
        }

        notification.markAsRead();
        InAppNotification saved = inAppNotificationRepository.save(notification);

        return inAppNotificationMapper.toResponseDto(saved);
    }

    @Override
    public InAppNotificationResponseDto updateNotification(Long id, InAppNotificationUpdateDto dto) {
        log.info("Updating notification id: {}", id);

        InAppNotification notification = inAppNotificationRepository.findNotDeletedById(id)
                .orElseThrow(() -> InAppNotificationNotFoundException.byId(id));

        inAppNotificationMapper.updateEntity(dto, notification);
        InAppNotification saved = inAppNotificationRepository.save(notification);

        return inAppNotificationMapper.toResponseDto(saved);
    }

    @Override
    public void deleteNotification(Long id) {
        log.info("Soft-deleting notification id: {}", id);

        InAppNotification notification = inAppNotificationRepository.findNotDeletedById(id)
                .orElseThrow(() -> InAppNotificationNotFoundException.byId(id));

        notification.softDelete(null);
        inAppNotificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotificationsByUser(Long userId) {
        return inAppNotificationRepository.countByUserIdAndIsReadFalse(userId);
    }
}
