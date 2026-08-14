package hospital.tenantservice.service;

import hospital.tenantservice.dto.tenant.*;
import hospital.tenantservice.exception.tenant.*;
import hospital.tenantservice.mapper.TenantMapper;
import hospital.tenantservice.model.Tenant;
import hospital.tenantservice.model.SubscriptionHistory;
import hospital.tenantservice.model.enums.PlanType;
import hospital.tenantservice.model.enums.SubscriptionChangeType;
import hospital.tenantservice.model.enums.TenantStatus;
import hospital.tenantservice.repository.SubscriptionHistoryRepository;
import hospital.tenantservice.repository.TenantRepository;
import hospital.tenantservice.service.impl.TenantServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TenantServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {

    @Mock private TenantRepository tenantRepository;
    @Mock private SubscriptionHistoryRepository subscriptionHistoryRepository;
    @Mock private TenantMapper tenantMapper;

    @InjectMocks
    private TenantServiceImpl tenantService;

    private Tenant testTenant;

    @BeforeEach
    void setUp() {
        testTenant = Tenant.builder()
                .id(1L)
                .name("Test Hospital")
                .subdomain("test-hospital")
                .adminEmail("admin@test.com")
                .plan(PlanType.BASIC)
                .status(TenantStatus.PENDING)
                .isActive(false)
                .maxUsers(20)
                .maxPatients(500)
                .startDate(LocalDate.now())
                .build();
    }

    @Nested
    @DisplayName("Create Tenant")
    class CreateTenantTests {

        @Test
        @DisplayName("should create tenant successfully")
        void shouldCreateTenant() {
            TenantCreateDto dto = TenantCreateDto.builder()
                    .name("Test Hospital")
                    .subdomain("test-hospital")
                    .adminEmail("admin@test.com")
                    .plan(PlanType.BASIC)
                    .startDate(LocalDate.now())
                    .build();

            when(tenantRepository.existsBySubdomain("test-hospital")).thenReturn(false);
            when(tenantRepository.existsByName("Test Hospital")).thenReturn(false);
            when(tenantMapper.toEntity(any(TenantCreateDto.class))).thenReturn(testTenant);
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            TenantResponseDto result = tenantService.createTenant(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(tenantRepository).save(any(Tenant.class));
        }

        @Test
        @DisplayName("should throw when subdomain already exists")
        void shouldThrowWhenSubdomainExists() {
            TenantCreateDto dto = TenantCreateDto.builder()
                    .name("Test Hospital")
                    .subdomain("test-hospital")
                    .adminEmail("admin@test.com")
                    .plan(PlanType.BASIC)
                    .startDate(LocalDate.now())
                    .build();

            when(tenantRepository.existsBySubdomain("test-hospital")).thenReturn(true);

            assertThatThrownBy(() -> tenantService.createTenant(dto))
                    .isInstanceOf(DuplicateSubdomainException.class);
        }

        @Test
        @DisplayName("should throw when name already exists")
        void shouldThrowWhenNameExists() {
            TenantCreateDto dto = TenantCreateDto.builder()
                    .name("Test Hospital")
                    .subdomain("test-hospital")
                    .adminEmail("admin@test.com")
                    .plan(PlanType.BASIC)
                    .startDate(LocalDate.now())
                    .build();

            when(tenantRepository.existsBySubdomain("test-hospital")).thenReturn(false);
            when(tenantRepository.existsByName("Test Hospital")).thenReturn(true);

            assertThatThrownBy(() -> tenantService.createTenant(dto))
                    .isInstanceOf(DuplicateTenantNameException.class);
        }
    }

    @Nested
    @DisplayName("Read Tenant")
    class ReadTenantTests {

        @Test
        @DisplayName("should get tenant by id")
        void shouldGetById() {
            TenantResponseDto expected = TenantResponseDto.builder().id(1L).build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantMapper.toResponseDto(testTenant)).thenReturn(expected);

            TenantResponseDto result = tenantService.getTenantById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found by id")
        void shouldThrowWhenNotFoundById() {
            when(tenantRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tenantService.getTenantById(999L))
                    .isInstanceOf(TenantNotFoundException.class);
        }

        @Test
        @DisplayName("should get tenant by subdomain")
        void shouldGetBySubdomain() {
            when(tenantRepository.findBySubdomain("test-hospital")).thenReturn(Optional.of(testTenant));
            when(tenantMapper.toResponseDto(testTenant))
                    .thenReturn(TenantResponseDto.builder().id(1L).subdomain("test-hospital").build());

            TenantResponseDto result = tenantService.getTenantBySubdomain("test-hospital");

            assertThat(result.getSubdomain()).isEqualTo("test-hospital");
        }

        @Test
        @DisplayName("should get all tenants")
        void shouldGetAll() {
            when(tenantRepository.findAllNotDeleted()).thenReturn(List.of(testTenant));
            when(tenantMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantResponseDto.builder().id(1L).build()));

            assertThat(tenantService.getAllTenants()).hasSize(1);
        }

        @Test
        @DisplayName("should get tenants by status")
        void shouldGetByStatus() {
            when(tenantRepository.findByStatus(TenantStatus.ACTIVE)).thenReturn(List.of(testTenant));
            when(tenantMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantResponseDto.builder().id(1L).build()));

            assertThat(tenantService.getTenantsByStatus(TenantStatus.ACTIVE)).hasSize(1);
        }

        @Test
        @DisplayName("should get active tenants")
        void shouldGetActive() {
            when(tenantRepository.findActiveTenants()).thenReturn(List.of(testTenant));
            when(tenantMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(TenantResponseDto.builder().id(1L).build()));

            assertThat(tenantService.getActiveTenants()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Transitions")
    class StatusTransitionTests {

        @Test
        @DisplayName("should activate tenant")
        void shouldActivateTenant() {
            TenantStatusUpdateDto dto = TenantStatusUpdateDto.builder()
                    .reason("Payment confirmed")
                    .build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            tenantService.activateTenant(1L, dto);

            verify(tenantRepository).save(argThat(t -> t.getStatus() == TenantStatus.ACTIVE));
        }

        @Test
        @DisplayName("should throw when activating INACTIVE tenant")
        void shouldThrowWhenActivatingInactive() {
            testTenant.setStatus(TenantStatus.INACTIVE);
            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));

            assertThatThrownBy(() -> tenantService.activateTenant(1L, null))
                    .isInstanceOf(TenantActivationException.class);
        }

        @Test
        @DisplayName("should deactivate tenant")
        void shouldDeactivateTenant() {
            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            tenantService.deactivateTenant(1L, null);

            verify(tenantRepository).save(argThat(t -> t.getStatus() == TenantStatus.INACTIVE));
        }

        @Test
        @DisplayName("should suspend tenant")
        void shouldSuspendTenant() {
            testTenant.setStatus(TenantStatus.ACTIVE);
            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            tenantService.suspendTenant(1L, null);

            verify(tenantRepository).save(argThat(t -> t.getStatus() == TenantStatus.SUSPENDED));
        }

        @Test
        @DisplayName("should throw when suspending non-ACTIVE tenant")
        void shouldThrowWhenSuspendingNonActive() {
            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));

            assertThatThrownBy(() -> tenantService.suspendTenant(1L, null))
                    .isInstanceOf(TenantSuspensionException.class);
        }
    }

    @Nested
    @DisplayName("Plan Changes")
    class PlanChangeTests {

        @Test
        @DisplayName("should upgrade tenant plan")
        void shouldUpgradePlan() {
            TenantPlanChangeDto dto = TenantPlanChangeDto.builder()
                    .newPlan(PlanType.PROFESSIONAL)
                    .reason("Growing hospital")
                    .build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            tenantService.upgradeTenant(1L, dto);

            verify(tenantRepository).save(any(Tenant.class));
        }

        @Test
        @DisplayName("should downgrade tenant plan")
        void shouldDowngradePlan() {
            TenantPlanChangeDto dto = TenantPlanChangeDto.builder()
                    .newPlan(PlanType.FREE)
                    .reason("Cost reduction")
                    .build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantRepository.save(any(Tenant.class))).thenReturn(testTenant);
            when(tenantMapper.toResponseDto(any(Tenant.class)))
                    .thenReturn(TenantResponseDto.builder().id(1L).build());

            tenantService.downgradeTenant(1L, dto);

            verify(tenantRepository).save(any(Tenant.class));
        }

        @Test
        @DisplayName("should throw when downgrading exceeds limits")
        void shouldThrowWhenDowngradeExceedsLimits() {
            testTenant.setCurrentUsers(50); // exceeds FREE limit of 5
            TenantPlanChangeDto dto = TenantPlanChangeDto.builder()
                    .newPlan(PlanType.FREE)
                    .reason("Cost reduction")
                    .build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));

            assertThatThrownBy(() -> tenantService.downgradeTenant(1L, dto))
                    .isInstanceOf(TenantPlanChangeException.class);
        }
    }

    @Nested
    @DisplayName("Usage Statistics")
    class UsageTests {

        @Test
        @DisplayName("should get tenant usage")
        void shouldGetUsage() {
            TenantUsageResponseDto usage = TenantUsageResponseDto.builder()
                    .tenantId(1L)
                    .currentUsers(10)
                    .maxUsers(20)
                    .userUsagePercent(50.0)
                    .build();

            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));
            when(tenantMapper.toUsageResponseDto(any(Tenant.class))).thenReturn(usage);

            TenantUsageResponseDto result = tenantService.getTenantUsage(1L);

            assertThat(result.getUserUsagePercent()).isEqualTo(50.0);
        }
    }

    @Nested
    @DisplayName("Delete Tenant")
    class DeleteTenantTests {

        @Test
        @DisplayName("should soft delete tenant")
        void shouldSoftDelete() {
            when(tenantRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTenant));

            tenantService.deleteTenant(1L);

            verify(tenantRepository).save(argThat(Tenant::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check subdomain existence")
        void shouldCheckSubdomain() {
            when(tenantRepository.existsBySubdomain("test-hospital")).thenReturn(true);

            assertThat(tenantService.subdomainExists("test-hospital")).isTrue();
        }

        @Test
        @DisplayName("should count tenants by status")
        void shouldCountByStatus() {
            when(tenantRepository.countByStatus(TenantStatus.ACTIVE)).thenReturn(5L);

            assertThat(tenantService.countTenantsByStatus(TenantStatus.ACTIVE)).isEqualTo(5L);
        }

        @Test
        @DisplayName("should count tenants by plan")
        void shouldCountByPlan() {
            when(tenantRepository.countByPlan(PlanType.BASIC)).thenReturn(10L);

            assertThat(tenantService.countTenantsByPlan(PlanType.BASIC)).isEqualTo(10L);
        }
    }
}
