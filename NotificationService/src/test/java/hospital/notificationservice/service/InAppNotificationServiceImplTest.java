package hospital.notificationservice.service;

import hospital.notificationservice.dto.inappnotification.InAppNotificationCreateDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationResponseDto;
import hospital.notificationservice.dto.inappnotification.InAppNotificationUpdateDto;
import hospital.notificationservice.exception.inappnotification.AlreadyReadNotificationException;
import hospital.notificationservice.exception.inappnotification.InAppNotificationNotFoundException;
import hospital.notificationservice.mapper.InAppNotificationMapper;
import hospital.notificationservice.model.InAppNotification;
import hospital.notificationservice.model.enums.NotificationType;
import hospital.notificationservice.repository.InAppNotificationRepository;
import hospital.notificationservice.service.impl.InAppNotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InAppNotificationServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class InAppNotificationServiceImplTest {

    @Mock private InAppNotificationRepository inAppNotificationRepository;
    @Mock private InAppNotificationMapper inAppNotificationMapper;

    @InjectMocks
    private InAppNotificationServiceImpl inAppNotificationService;

    private InAppNotification testNotification;

    @BeforeEach
    void setUp() {
        testNotification = InAppNotification.builder()
                .id(1L)
                .userId(100L)
                .title("Test Title")
                .message("Test Message")
                .type(NotificationType.APPOINTMENT_REMINDER)
                .relatedId(200L)
                .isRead(false)
                .build();
    }

    @Nested
    @DisplayName("Create Notification")
    class CreateNotificationTests {

        @Test
        @DisplayName("should create notification successfully")
        void shouldCreateNotification() {
            InAppNotificationCreateDto dto = InAppNotificationCreateDto.builder()
                    .userId(100L)
                    .title("Test Title")
                    .message("Test Message")
                    .type(NotificationType.APPOINTMENT_REMINDER)
                    .relatedId(200L)
                    .build();

            when(inAppNotificationMapper.toEntity(any(InAppNotificationCreateDto.class))).thenReturn(testNotification);
            when(inAppNotificationRepository.save(any(InAppNotification.class))).thenReturn(testNotification);
            when(inAppNotificationMapper.toResponseDto(any(InAppNotification.class)))
                    .thenReturn(InAppNotificationResponseDto.builder().id(1L).build());

            InAppNotificationResponseDto result = inAppNotificationService.createNotification(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(inAppNotificationRepository).save(any(InAppNotification.class));
        }
    }

    @Nested
    @DisplayName("Read Notification")
    class ReadNotificationTests {

        @Test
        @DisplayName("should get notification by id")
        void shouldGetById() {
            InAppNotificationResponseDto expected = InAppNotificationResponseDto.builder().id(1L).build();

            when(inAppNotificationRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testNotification));
            when(inAppNotificationMapper.toResponseDto(testNotification)).thenReturn(expected);

            InAppNotificationResponseDto result = inAppNotificationService.getNotificationById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(inAppNotificationRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inAppNotificationService.getNotificationById(999L))
                    .isInstanceOf(InAppNotificationNotFoundException.class);
        }

        @Test
        @DisplayName("should get notifications by user")
        void shouldGetByUser() {
            when(inAppNotificationRepository.findByUserIdOrderByCreatedAtDesc(100L))
                    .thenReturn(List.of(testNotification));
            when(inAppNotificationMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(InAppNotificationResponseDto.builder().id(1L).build()));

            assertThat(inAppNotificationService.getNotificationsByUser(100L)).hasSize(1);
        }

        @Test
        @DisplayName("should get unread notifications by user")
        void shouldGetUnreadByUser() {
            when(inAppNotificationRepository.findUnreadByUserId(100L))
                    .thenReturn(List.of(testNotification));
            when(inAppNotificationMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(InAppNotificationResponseDto.builder().id(1L).build()));

            assertThat(inAppNotificationService.getUnreadNotificationsByUser(100L)).hasSize(1);
        }

        @Test
        @DisplayName("should get notifications by type")
        void shouldGetByType() {
            when(inAppNotificationRepository.findByType(NotificationType.APPOINTMENT_REMINDER))
                    .thenReturn(List.of(testNotification));
            when(inAppNotificationMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(InAppNotificationResponseDto.builder().id(1L).build()));

            assertThat(inAppNotificationService.getNotificationsByType(NotificationType.APPOINTMENT_REMINDER)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Mark As Read")
    class MarkAsReadTests {

        @Test
        @DisplayName("should mark notification as read")
        void shouldMarkAsRead() {
            when(inAppNotificationRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testNotification));
            when(inAppNotificationMapper.toResponseDto(any(InAppNotification.class)))
                    .thenReturn(InAppNotificationResponseDto.builder().id(1L).build());
            when(inAppNotificationRepository.save(any(InAppNotification.class))).thenReturn(testNotification);

            inAppNotificationService.markAsRead(1L);

            verify(inAppNotificationRepository).save(argThat(n -> Boolean.TRUE.equals(n.getIsRead())));
        }

        @Test
        @DisplayName("should throw when already read")
        void shouldThrowWhenAlreadyRead() {
            testNotification.setIsRead(true);
            when(inAppNotificationRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testNotification));

            assertThatThrownBy(() -> inAppNotificationService.markAsRead(1L))
                    .isInstanceOf(AlreadyReadNotificationException.class);
        }
    }

    @Nested
    @DisplayName("Update Notification")
    class UpdateNotificationTests {

        @Test
        @DisplayName("should update notification")
        void shouldUpdateNotification() {
            InAppNotificationUpdateDto dto = InAppNotificationUpdateDto.builder()
                    .title("Updated Title")
                    .build();

            when(inAppNotificationRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testNotification));
            when(inAppNotificationMapper.toResponseDto(any(InAppNotification.class)))
                    .thenReturn(InAppNotificationResponseDto.builder().id(1L).build());
            when(inAppNotificationRepository.save(any(InAppNotification.class))).thenReturn(testNotification);

            InAppNotificationResponseDto result = inAppNotificationService.updateNotification(1L, dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(inAppNotificationMapper).updateEntity(dto, testNotification);
        }

        @Test
        @DisplayName("should throw when updating non-existent notification")
        void shouldThrowWhenUpdatingNonExistent() {
            when(inAppNotificationRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> inAppNotificationService.updateNotification(999L, new InAppNotificationUpdateDto()))
                    .isInstanceOf(InAppNotificationNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Notification")
    class DeleteNotificationTests {

        @Test
        @DisplayName("should soft delete notification")
        void shouldSoftDelete() {
            when(inAppNotificationRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testNotification));

            inAppNotificationService.deleteNotification(1L);

            verify(inAppNotificationRepository).save(argThat(InAppNotification::isDeleted));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count unread notifications by user")
        void shouldCountUnreadByUser() {
            when(inAppNotificationRepository.countByUserIdAndIsReadFalse(100L)).thenReturn(3L);

            assertThat(inAppNotificationService.countUnreadNotificationsByUser(100L)).isEqualTo(3L);
        }
    }
}
