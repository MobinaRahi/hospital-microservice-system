package hospital.tenantservice.service;

import hospital.tenantservice.dto.subscriptionhistory.SubscriptionHistoryResponseDto;
import hospital.tenantservice.exception.subscriptionhistory.SubscriptionHistoryNotFoundException;
import hospital.tenantservice.mapper.SubscriptionHistoryMapper;
import hospital.tenantservice.model.SubscriptionHistory;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import hospital.tenantservice.repository.SubscriptionHistoryRepository;
import hospital.tenantservice.service.impl.SubscriptionHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SubscriptionHistoryServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionHistoryServiceImplTest {

    @Mock private SubscriptionHistoryRepository subscriptionHistoryRepository;
    @Mock private SubscriptionHistoryMapper subscriptionHistoryMapper;

    @InjectMocks
    private SubscriptionHistoryServiceImpl subscriptionHistoryService;

    private SubscriptionHistory testHistory;

    @BeforeEach
    void setUp() {
        testHistory = SubscriptionHistory.builder()
                .id(1L)
                .tenantId(1L)
                .changeType(SubscriptionChangeType.CREATED)
                .previousPlan(null)
                .newPlan(PlanType.BASIC)
                .changeDate(LocalDate.now())
                .effectiveDate(LocalDate.now())
                .reason("Initial creation")
                .build();
    }

    @Nested
    @DisplayName("Read History")
    class ReadHistoryTests {

        @Test
        @DisplayName("should get history by tenant")
        void shouldGetHistoryByTenant() {
            when(subscriptionHistoryRepository.findByTenantIdOrderByChangeDateDesc(1L))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SubscriptionHistoryResponseDto.builder().id(1L).build()));

            assertThat(subscriptionHistoryService.getHistoryByTenant(1L)).hasSize(1);
        }

        @Test
        @DisplayName("should throw when no history found for tenant")
        void shouldThrowWhenNoHistory() {
            when(subscriptionHistoryRepository.findByTenantIdOrderByChangeDateDesc(999L))
                    .thenReturn(List.of());

            assertThatThrownBy(() -> subscriptionHistoryService.getHistoryByTenant(999L))
                    .isInstanceOf(SubscriptionHistoryNotFoundException.class);
        }

        @Test
        @DisplayName("should get most recent change")
        void shouldGetMostRecentChange() {
            when(subscriptionHistoryRepository.findMostRecentChange(1L))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDto(any(SubscriptionHistory.class)))
                    .thenReturn(SubscriptionHistoryResponseDto.builder().id(1L).build());

            SubscriptionHistoryResponseDto result = subscriptionHistoryService.getMostRecentChange(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when no recent change found")
        void shouldThrowWhenNoRecentChange() {
            when(subscriptionHistoryRepository.findMostRecentChange(999L))
                    .thenReturn(List.of());

            assertThatThrownBy(() -> subscriptionHistoryService.getMostRecentChange(999L))
                    .isInstanceOf(SubscriptionHistoryNotFoundException.class);
        }

        @Test
        @DisplayName("should get history by change type")
        void shouldGetByChangeType() {
            when(subscriptionHistoryRepository.findByChangeType(SubscriptionChangeType.UPGRADED))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SubscriptionHistoryResponseDto.builder().id(1L).build()));

            assertThat(subscriptionHistoryService.getHistoryByChangeType(SubscriptionChangeType.UPGRADED)).hasSize(1);
        }

        @Test
        @DisplayName("should get scheduled changes")
        void shouldGetScheduledChanges() {
            LocalDate start = LocalDate.now();
            LocalDate end = LocalDate.now().plusDays(30);

            when(subscriptionHistoryRepository.findByEffectiveDateBetween(start, end))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SubscriptionHistoryResponseDto.builder().id(1L).build()));

            assertThat(subscriptionHistoryService.getScheduledChanges(start, end)).hasSize(1);
        }

        @Test
        @DisplayName("should get pending scheduled changes")
        void shouldGetPendingScheduledChanges() {
            when(subscriptionHistoryRepository.findPendingScheduledChanges(any(LocalDate.class)))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SubscriptionHistoryResponseDto.builder().id(1L).build()));

            assertThat(subscriptionHistoryService.getPendingScheduledChanges()).hasSize(1);
        }

        @Test
        @DisplayName("should get changes by user")
        void shouldGetChangesByUser() {
            when(subscriptionHistoryRepository.findByChangedBy(100L))
                    .thenReturn(List.of(testHistory));
            when(subscriptionHistoryMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SubscriptionHistoryResponseDto.builder().id(1L).build()));

            assertThat(subscriptionHistoryService.getChangesByUser(100L)).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Count History")
    class CountHistoryTests {

        @Test
        @DisplayName("should count by change type")
        void shouldCountByChangeType() {
            when(subscriptionHistoryRepository.countByChangeType(SubscriptionChangeType.UPGRADED)).thenReturn(5L);

            assertThat(subscriptionHistoryService.countByChangeType(SubscriptionChangeType.UPGRADED)).isEqualTo(5L);
        }

        @Test
        @DisplayName("should count by tenant")
        void shouldCountByTenant() {
            when(subscriptionHistoryRepository.countByTenantId(1L)).thenReturn(10L);

            assertThat(subscriptionHistoryService.countByTenant(1L)).isEqualTo(10L);
        }
    }
}
