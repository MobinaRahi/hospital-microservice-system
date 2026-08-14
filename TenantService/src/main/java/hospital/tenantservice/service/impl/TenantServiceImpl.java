package hospital.tenantservice.service.impl;

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
import hospital.tenantservice.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link TenantService}.
 *
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each tenant must have a unique subdomain</li>
 *   <li>New tenants start with PENDING status</li>
 *   <li>Plan limits are enforced on user/patient/appointment creation</li>
 *   <li>Subscriptions can be upgraded, downgraded, suspended, or cancelled</li>
 *   <li>All plan changes are recorded in SubscriptionHistory</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final TenantMapper tenantMapper;

    @Override
    public TenantResponseDto createTenant(TenantCreateDto dto) {
        log.info("Creating new tenant: {}", dto.getName());

        // Check unique subdomain
        if (tenantRepository.existsBySubdomain(dto.getSubdomain())) {
            throw new DuplicateSubdomainException(dto.getSubdomain());
        }

        // Check unique name
        if (tenantRepository.existsByName(dto.getName())) {
            throw new DuplicateTenantNameException(dto.getName());
        }

        // Map DTO to entity
        Tenant tenant = tenantMapper.toEntity(dto);

        // Set initial status
        tenant.setStatus(TenantStatus.PENDING);
        tenant.setIsActive(false);

        // Set plan limits
        applyPlanLimits(tenant, dto.getPlan());

        // Save
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant created with id: {} and subdomain: {}", saved.getId(), saved.getSubdomain());

        // Record in subscription history
        recordSubscriptionChange(saved, null, dto.getPlan(), SubscriptionChangeType.CREATED, "Initial creation");

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantResponseDto getTenantById(Long id) {
        log.debug("Fetching tenant by id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        return tenantMapper.toResponseDto(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public TenantResponseDto getTenantBySubdomain(String subdomain) {
        log.debug("Fetching tenant by subdomain: {}", subdomain);

        Tenant tenant = tenantRepository.findBySubdomain(subdomain)
                .orElseThrow(() -> TenantNotFoundException.bySubdomain(subdomain));

        return tenantMapper.toResponseDto(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> getAllTenants() {
        log.debug("Fetching all tenants");

        List<Tenant> tenants = tenantRepository.findAllNotDeleted();
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> getTenantsByStatus(TenantStatus status) {
        log.debug("Fetching tenants by status: {}", status);

        List<Tenant> tenants = tenantRepository.findByStatus(status);
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> getTenantsByPlan(PlanType plan) {
        log.debug("Fetching tenants by plan: {}", plan);

        List<Tenant> tenants = tenantRepository.findByPlan(plan);
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> getActiveTenants() {
        log.debug("Fetching active tenants");

        List<Tenant> tenants = tenantRepository.findActiveTenants();
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> searchTenantsByName(String name) {
        log.debug("Searching tenants by name: {}", name);

        List<Tenant> tenants = tenantRepository.findByNameContainingIgnoreCase(name);
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TenantResponseDto> getTenantsExpiringWithin(int days) {
        log.debug("Fetching tenants expiring within {} days", days);

        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(days);

        List<Tenant> tenants = tenantRepository.findTenantsExpiringSoon(today, expiryDate);
        return tenantMapper.toResponseDtoList(tenants);
    }

    @Override
    public TenantResponseDto updateTenant(Long id, TenantUpdateDto dto) {
        log.info("Updating tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        tenantMapper.updateEntity(dto, tenant);
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant updated id: {}", saved.getId());

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public TenantResponseDto activateTenant(Long id, TenantStatusUpdateDto dto) {
        log.info("Activating tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        if (tenant.getStatus() == TenantStatus.INACTIVE) {
            throw new TenantActivationException("Cannot activate an INACTIVE tenant");
        }

        tenant.activate();
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant activated id: {}", saved.getId());

        recordSubscriptionChange(saved, tenant.getPlan(), tenant.getPlan(), SubscriptionChangeType.ACTIVATED, dto != null ? dto.getReason() : "Activated");

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public TenantResponseDto deactivateTenant(Long id, TenantStatusUpdateDto dto) {
        log.info("Deactivating tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        tenant.deactivate();
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant deactivated id: {}", saved.getId());

        recordSubscriptionChange(saved, tenant.getPlan(), tenant.getPlan(), SubscriptionChangeType.CANCELLED, dto != null ? dto.getReason() : "Deactivated");

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public TenantResponseDto suspendTenant(Long id, TenantStatusUpdateDto dto) {
        log.info("Suspending tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        if (tenant.getStatus() != TenantStatus.ACTIVE) {
            throw new TenantSuspensionException("Cannot suspend tenant in status: " + tenant.getStatus());
        }

        tenant.suspend();
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant suspended id: {}", saved.getId());

        recordSubscriptionChange(saved, tenant.getPlan(), tenant.getPlan(), SubscriptionChangeType.SUSPENDED, dto != null ? dto.getReason() : "Suspended");

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public TenantResponseDto upgradeTenant(Long id, TenantPlanChangeDto dto) {
        log.info("Upgrading tenant id: {} to plan: {}", id, dto.getNewPlan());

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        PlanType previousPlan = tenant.getPlan();
        tenant.upgradePlan(dto.getNewPlan());
        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant upgraded id: {} from {} to {}", saved.getId(), previousPlan, dto.getNewPlan());

        recordSubscriptionChange(saved, previousPlan, dto.getNewPlan(), SubscriptionChangeType.UPGRADED, dto.getReason());

        return tenantMapper.toResponseDto(saved);
    }

    @Override
    public TenantResponseDto downgradeTenant(Long id, TenantPlanChangeDto dto) {
        log.info("Downgrading tenant id: {} to plan: {}", id, dto.getNewPlan());

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        try {
            PlanType previousPlan = tenant.getPlan();
            tenant.downgradePlan(dto.getNewPlan());
            Tenant saved = tenantRepository.save(tenant);
            log.info("Tenant downgraded id: {} from {} to {}", saved.getId(), previousPlan, dto.getNewPlan());

            recordSubscriptionChange(saved, previousPlan, dto.getNewPlan(), SubscriptionChangeType.DOWNGRADED, dto.getReason());

            return tenantMapper.toResponseDto(saved);
        } catch (IllegalStateException ex) {
            throw TenantPlanChangeException.cannotDowngrade(ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TenantUsageResponseDto getTenantUsage(Long id) {
        log.debug("Fetching usage for tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        TenantUsageResponseDto usage = tenantMapper.toUsageResponseDto(tenant);

        // Calculate usage percentages
        if (tenant.getMaxUsers() > 0) {
            usage.setUserUsagePercent((tenant.getCurrentUsers() * 100.0) / tenant.getMaxUsers());
        }
        if (tenant.getMaxPatients() > 0) {
            usage.setPatientUsagePercent((tenant.getCurrentPatients() * 100.0) / tenant.getMaxPatients());
        }
        if (tenant.getMaxAppointmentsPerMonth() > 0) {
            usage.setAppointmentUsagePercent((tenant.getCurrentMonthAppointments() * 100.0) / tenant.getMaxAppointmentsPerMonth());
        }
        if (tenant.getStorageLimitMB() > 0) {
            // Storage usage would come from a storage service
            usage.setStorageUsedMB(0);
            usage.setStorageUsagePercent(0.0);
        }

        return usage;
    }

    @Override
    public void deleteTenant(Long id) {
        log.info("Soft-deleting tenant id: {}", id);

        Tenant tenant = tenantRepository.findNotDeletedById(id)
                .orElseThrow(() -> TenantNotFoundException.byId(id));

        tenant.softDelete(null);
        tenantRepository.save(tenant);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean subdomainExists(String subdomain) {
        return tenantRepository.existsBySubdomain(subdomain);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTenantsByStatus(TenantStatus status) {
        return tenantRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTenantsByPlan(PlanType plan) {
        return tenantRepository.countByPlan(plan);
    }

    // ══════════════════════════════════════════════════════════════════
    // Private Helper Methods
    // ══════════════════════════════════════════════════════════════════

    private void applyPlanLimits(Tenant tenant, PlanType plan) {
        // Plan limits are applied by the entity's upgradePlan method
        // For new tenants, we set the initial limits
        tenant.setPlan(plan);
        switch (plan) {
            case FREE:
                tenant.setMaxUsers(5);
                tenant.setMaxPatients(50);
                tenant.setMaxAppointmentsPerMonth(100);
                tenant.setStorageLimitMB(1024);
                tenant.setSupportLevel("email");
                break;
            case BASIC:
                tenant.setMaxUsers(20);
                tenant.setMaxPatients(500);
                tenant.setMaxAppointmentsPerMonth(1000);
                tenant.setStorageLimitMB(10240);
                tenant.setSupportLevel("email");
                break;
            case PROFESSIONAL:
                tenant.setMaxUsers(100);
                tenant.setMaxPatients(5000);
                tenant.setMaxAppointmentsPerMonth(10000);
                tenant.setStorageLimitMB(102400);
                tenant.setSupportLevel("priority");
                break;
            case ENTERPRISE:
                tenant.setMaxUsers(-1);
                tenant.setMaxPatients(-1);
                tenant.setMaxAppointmentsPerMonth(-1);
                tenant.setStorageLimitMB(-1);
                tenant.setSupportLevel("24/7");
                break;
        }
    }

    private void recordSubscriptionChange(Tenant tenant, PlanType previousPlan, PlanType newPlan, SubscriptionChangeType changeType, String reason) {
        SubscriptionHistory history = SubscriptionHistory.builder()
                .tenantId(tenant.getId())
                .changeType(changeType)
                .previousPlan(previousPlan)
                .newPlan(newPlan)
                .changeDate(LocalDate.now())
                .effectiveDate(LocalDate.now())
                .reason(reason)
                .build();

        subscriptionHistoryRepository.save(history);
    }
}
