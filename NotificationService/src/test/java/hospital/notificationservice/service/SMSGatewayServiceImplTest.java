package hospital.notificationservice.service;

import hospital.notificationservice.dto.smsgateway.SMSGatewayCreateDto;
import hospital.notificationservice.dto.smsgateway.SMSGatewayResponseDto;
import hospital.notificationservice.exception.smsgateway.SMSGatewayNotFoundException;
import hospital.notificationservice.exception.smsgateway.SmsCancelException;
import hospital.notificationservice.mapper.SMSGatewayMapper;
import hospital.notificationservice.model.SMSGateway;
import hospital.notificationservice.model.enums.SmsStatus;
import hospital.notificationservice.repository.SMSGatewayRepository;
import hospital.notificationservice.service.impl.SMSGatewayServiceImpl;
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
 * Unit tests for {@link SMSGatewayServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class SMSGatewayServiceImplTest {

    @Mock private SMSGatewayRepository smsGatewayRepository;
    @Mock private SMSGatewayMapper smsGatewayMapper;

    @InjectMocks
    private SMSGatewayServiceImpl smsGatewayService;

    private SMSGateway testSms;

    @BeforeEach
    void setUp() {
        testSms = SMSGateway.builder()
                .id(1L)
                .to("+989123456789")
                .message("Test message")
                .status(SmsStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create SMS")
    class CreateSmsTests {

        @Test
        @DisplayName("should create SMS successfully")
        void shouldCreateSms() {
            SMSGatewayCreateDto dto = SMSGatewayCreateDto.builder()
                    .to("+989123456789")
                    .message("Test message")
                    .build();

            when(smsGatewayMapper.toEntity(any(SMSGatewayCreateDto.class))).thenReturn(testSms);
            when(smsGatewayRepository.save(any(SMSGateway.class))).thenReturn(testSms);
            when(smsGatewayMapper.toResponseDto(any(SMSGateway.class)))
                    .thenReturn(SMSGatewayResponseDto.builder().id(1L).build());

            SMSGatewayResponseDto result = smsGatewayService.createSms(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(smsGatewayRepository).save(any(SMSGateway.class));
        }
    }

    @Nested
    @DisplayName("Read SMS")
    class ReadSmsTests {

        @Test
        @DisplayName("should get SMS by id")
        void shouldGetById() {
            SMSGatewayResponseDto expected = SMSGatewayResponseDto.builder().id(1L).build();

            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));
            when(smsGatewayMapper.toResponseDto(testSms)).thenReturn(expected);

            SMSGatewayResponseDto result = smsGatewayService.getSmsById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(smsGatewayRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> smsGatewayService.getSmsById(999L))
                    .isInstanceOf(SMSGatewayNotFoundException.class);
        }

        @Test
        @DisplayName("should get SMS by status")
        void shouldGetByStatus() {
            when(smsGatewayRepository.findByStatus(SmsStatus.PENDING))
                    .thenReturn(List.of(testSms));
            when(smsGatewayMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SMSGatewayResponseDto.builder().id(1L).build()));

            assertThat(smsGatewayService.getSmsByStatus(SmsStatus.PENDING)).hasSize(1);
        }

        @Test
        @DisplayName("should get pending SMS")
        void shouldGetPendingSms() {
            when(smsGatewayRepository.findPendingMessages())
                    .thenReturn(List.of(testSms));
            when(smsGatewayMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SMSGatewayResponseDto.builder().id(1L).build()));

            assertThat(smsGatewayService.getPendingSms()).hasSize(1);
        }

        @Test
        @DisplayName("should get failed SMS")
        void shouldGetFailedSms() {
            when(smsGatewayRepository.findByStatusAndDeletedFalse(SmsStatus.FAILED))
                    .thenReturn(List.of(testSms));
            when(smsGatewayMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SMSGatewayResponseDto.builder().id(1L).build()));

            assertThat(smsGatewayService.getFailedSms()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should mark SMS as sent")
        void shouldMarkAsSent() {
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));
            when(smsGatewayMapper.toResponseDto(any(SMSGateway.class)))
                    .thenReturn(SMSGatewayResponseDto.builder().id(1L).build());
            when(smsGatewayRepository.save(any(SMSGateway.class))).thenReturn(testSms);

            smsGatewayService.markAsSent(1L, "PROV-123");

            verify(smsGatewayRepository).save(argThat(s -> s.getStatus() == SmsStatus.SENT));
        }

        @Test
        @DisplayName("should mark SMS as delivered")
        void shouldMarkAsDelivered() {
            testSms.setStatus(SmsStatus.SENT);
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));
            when(smsGatewayMapper.toResponseDto(any(SMSGateway.class)))
                    .thenReturn(SMSGatewayResponseDto.builder().id(1L).build());
            when(smsGatewayRepository.save(any(SMSGateway.class))).thenReturn(testSms);

            smsGatewayService.markAsDelivered(1L);

            verify(smsGatewayRepository).save(argThat(s -> s.getStatus() == SmsStatus.DELIVERED));
        }

        @Test
        @DisplayName("should mark SMS as failed")
        void shouldMarkAsFailed() {
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));
            when(smsGatewayMapper.toResponseDto(any(SMSGateway.class)))
                    .thenReturn(SMSGatewayResponseDto.builder().id(1L).build());
            when(smsGatewayRepository.save(any(SMSGateway.class))).thenReturn(testSms);

            smsGatewayService.markAsFailed(1L, "Gateway timeout");

            verify(smsGatewayRepository).save(argThat(s -> s.getStatus() == SmsStatus.FAILED));
        }

        @Test
        @DisplayName("should cancel pending SMS")
        void shouldCancelSms() {
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));
            when(smsGatewayMapper.toResponseDto(any(SMSGateway.class)))
                    .thenReturn(SMSGatewayResponseDto.builder().id(1L).build());
            when(smsGatewayRepository.save(any(SMSGateway.class))).thenReturn(testSms);

            smsGatewayService.cancelSms(1L);

            verify(smsGatewayRepository).save(argThat(s -> s.getStatus() == SmsStatus.CANCELLED));
        }

        @Test
        @DisplayName("should throw when cancelling non-pending SMS")
        void shouldThrowWhenCancellingNonPending() {
            testSms.setStatus(SmsStatus.SENT);
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));

            assertThatThrownBy(() -> smsGatewayService.cancelSms(1L))
                    .isInstanceOf(SmsCancelException.class);
        }
    }

    @Nested
    @DisplayName("Delete SMS")
    class DeleteSmsTests {

        @Test
        @DisplayName("should soft delete SMS")
        void shouldSoftDelete() {
            when(smsGatewayRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSms));

            smsGatewayService.deleteSms(1L);

            verify(smsGatewayRepository).save(argThat(SMSGateway::isDeleted));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count SMS by status")
        void shouldCountByStatus() {
            when(smsGatewayRepository.countByStatus(SmsStatus.PENDING)).thenReturn(5L);

            assertThat(smsGatewayService.countSmsByStatus(SmsStatus.PENDING)).isEqualTo(5L);
        }
    }
}
