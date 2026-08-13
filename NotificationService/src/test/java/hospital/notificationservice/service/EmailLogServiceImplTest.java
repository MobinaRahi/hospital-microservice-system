package hospital.notificationservice.service;

import hospital.notificationservice.dto.emaillog.EmailLogCreateDto;
import hospital.notificationservice.dto.emaillog.EmailLogResponseDto;
import hospital.notificationservice.exception.emaillog.EmailLogNotFoundException;
import hospital.notificationservice.mapper.EmailLogMapper;
import hospital.notificationservice.model.EmailLog;
import hospital.notificationservice.model.enums.EmailStatus;
import hospital.notificationservice.repository.EmailLogRepository;
import hospital.notificationservice.service.impl.EmailLogServiceImpl;
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
 * Unit tests for {@link EmailLogServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class EmailLogServiceImplTest {

    @Mock private EmailLogRepository emailLogRepository;
    @Mock private EmailLogMapper emailLogMapper;

    @InjectMocks
    private EmailLogServiceImpl emailLogService;

    private EmailLog testEmail;

    @BeforeEach
    void setUp() {
        testEmail = EmailLog.builder()
                .id(1L)
                .to("test@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .status(EmailStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Email")
    class CreateEmailTests {

        @Test
        @DisplayName("should create email successfully")
        void shouldCreateEmail() {
            EmailLogCreateDto dto = EmailLogCreateDto.builder()
                    .to("test@example.com")
                    .subject("Test Subject")
                    .body("Test Body")
                    .build();

            when(emailLogMapper.toEntity(any(EmailLogCreateDto.class))).thenReturn(testEmail);
            when(emailLogRepository.save(any(EmailLog.class))).thenReturn(testEmail);
            when(emailLogMapper.toResponseDto(any(EmailLog.class)))
                    .thenReturn(EmailLogResponseDto.builder().id(1L).build());

            EmailLogResponseDto result = emailLogService.createEmail(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(emailLogRepository).save(any(EmailLog.class));
        }
    }

    @Nested
    @DisplayName("Read Email")
    class ReadEmailTests {

        @Test
        @DisplayName("should get email by id")
        void shouldGetById() {
            EmailLogResponseDto expected = EmailLogResponseDto.builder().id(1L).build();

            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));
            when(emailLogMapper.toResponseDto(testEmail)).thenReturn(expected);

            EmailLogResponseDto result = emailLogService.getEmailById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(emailLogRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> emailLogService.getEmailById(999L))
                    .isInstanceOf(EmailLogNotFoundException.class);
        }

        @Test
        @DisplayName("should get emails by status")
        void shouldGetByStatus() {
            when(emailLogRepository.findByStatus(EmailStatus.PENDING))
                    .thenReturn(List.of(testEmail));
            when(emailLogMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(EmailLogResponseDto.builder().id(1L).build()));

            assertThat(emailLogService.getEmailsByStatus(EmailStatus.PENDING)).hasSize(1);
        }

        @Test
        @DisplayName("should get pending emails")
        void shouldGetPendingEmails() {
            when(emailLogRepository.findPendingEmails())
                    .thenReturn(List.of(testEmail));
            when(emailLogMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(EmailLogResponseDto.builder().id(1L).build()));

            assertThat(emailLogService.getPendingEmails()).hasSize(1);
        }

        @Test
        @DisplayName("should get failed emails")
        void shouldGetFailedEmails() {
            when(emailLogRepository.findFailedEmails())
                    .thenReturn(List.of(testEmail));
            when(emailLogMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(EmailLogResponseDto.builder().id(1L).build()));

            assertThat(emailLogService.getFailedEmails()).hasSize(1);
        }

        @Test
        @DisplayName("should get unopened emails")
        void shouldGetUnopenedEmails() {
            when(emailLogRepository.findUnopenedEmails())
                    .thenReturn(List.of(testEmail));
            when(emailLogMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(EmailLogResponseDto.builder().id(1L).build()));

            assertThat(emailLogService.getUnopenedEmails()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should mark email as sent")
        void shouldMarkAsSent() {
            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));
            when(emailLogMapper.toResponseDto(any(EmailLog.class)))
                    .thenReturn(EmailLogResponseDto.builder().id(1L).build());
            when(emailLogRepository.save(any(EmailLog.class))).thenReturn(testEmail);

            emailLogService.markAsSent(1L);

            verify(emailLogRepository).save(argThat(e -> e.getStatus() == EmailStatus.SENT));
        }

        @Test
        @DisplayName("should mark email as delivered")
        void shouldMarkAsDelivered() {
            testEmail.setStatus(EmailStatus.SENT);
            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));
            when(emailLogMapper.toResponseDto(any(EmailLog.class)))
                    .thenReturn(EmailLogResponseDto.builder().id(1L).build());
            when(emailLogRepository.save(any(EmailLog.class))).thenReturn(testEmail);

            emailLogService.markAsDelivered(1L);

            verify(emailLogRepository).save(argThat(e -> e.getStatus() == EmailStatus.DELIVERED));
        }

        @Test
        @DisplayName("should mark email as opened")
        void shouldMarkAsOpened() {
            testEmail.setStatus(EmailStatus.DELIVERED);
            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));
            when(emailLogMapper.toResponseDto(any(EmailLog.class)))
                    .thenReturn(EmailLogResponseDto.builder().id(1L).build());
            when(emailLogRepository.save(any(EmailLog.class))).thenReturn(testEmail);

            emailLogService.markAsOpened(1L);

            verify(emailLogRepository).save(argThat(e -> e.getStatus() == EmailStatus.OPENED));
        }

        @Test
        @DisplayName("should mark email as failed")
        void shouldMarkAsFailed() {
            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));
            when(emailLogMapper.toResponseDto(any(EmailLog.class)))
                    .thenReturn(EmailLogResponseDto.builder().id(1L).build());
            when(emailLogRepository.save(any(EmailLog.class))).thenReturn(testEmail);

            emailLogService.markAsFailed(1L, "SMTP error");

            verify(emailLogRepository).save(argThat(e -> e.getStatus() == EmailStatus.FAILED));
        }
    }

    @Nested
    @DisplayName("Delete Email")
    class DeleteEmailTests {

        @Test
        @DisplayName("should soft delete email")
        void shouldSoftDelete() {
            when(emailLogRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testEmail));

            emailLogService.deleteEmail(1L);

            verify(emailLogRepository).save(argThat(EmailLog::isDeleted));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count emails by status")
        void shouldCountByStatus() {
            when(emailLogRepository.countByStatus(EmailStatus.PENDING)).thenReturn(10L);

            assertThat(emailLogService.countEmailsByStatus(EmailStatus.PENDING)).isEqualTo(10L);
        }
    }
}
