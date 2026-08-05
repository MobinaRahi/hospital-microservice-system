package hospital.authservice.service.impl;

import hospital.authservice.model.enums.PlanType;
import hospital.authservice.model.enums.TenantStatus;
import hospital.authservice.model.tenant.Tenant;
import hospital.authservice.repository.TenantRepository;
import hospital.authservice.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link TenantService}.
 *
 * <p><strong>Trial Defaults:</strong></p>
 * <ul>
 *   <li>Plan: TRIAL</li>
 *   <li>Duration: 14 days</li>
 *   <li>Max patients: 100/month</li>
 *   <li>Enabled modules: core, auth, clinical</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantServiceImpl implements TenantService {

    private static final int TRIAL_DAYS = 14;
    private static final int TRIAL_MAX_PATIENTS = 100;
    private static final String TRIAL_MODULES = "[\"core\",\"auth\",\"clinical\"]";

    private final TenantRepository tenantRepository;

    // ════════════════════════════════════════════════════════════════════
    // Registration
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public Tenant registerTenant(String name, String subdomain, String adminEmail, String phone, String address) {
        log.info("Registering new tenant: {} (subdomain: {})", name, subdomain);

        // Validate subdomain uniqueness
        if (tenantRepository.existsBySubdomain(subdomain)) {
            throw new IllegalArgumentException("Subdomain '" + subdomain + "' is already taken.");
        }

        LocalDate today = LocalDate.now();

        Tenant tenant = Tenant.builder()
                .name(name)
                .subdomain(subdomain.toLowerCase().trim())
                .adminEmail(adminEmail)
                .phone(phone)
                .address(address)
                .planType(PlanType.TRIAL)
                .status(TenantStatus.TRIAL)
                .subscriptionStart(today)
                .subscriptionEnd(today.plusDays(TRIAL_DAYS))
                .maxPatientsPerMonth(TRIAL_MAX_PATIENTS)
                .enabledModules(TRIAL_MODULES)
                .build();

        Tenant saved = tenantRepository.save(tenant);
        log.info("Tenant registered with id: {}, trial until: {}", saved.getId(), saved.getSubscriptionEnd());

        return saved;
    }

    // ════════════════════════════════════════════════════════════════════
    // Read
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Tenant getTenantBySubdomain(String subdomain) {
        return tenantRepository.findBySubdomainAndDeletedFalse(subdomain)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with subdomain: " + subdomain));
    }

    @Override
    @Transactional(readOnly = true)
    public Tenant getTenantByAdminEmail(String email) {
        return tenantRepository.findByAdminEmailAndDeletedFalse(email)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tenant> getAllTenants(Pageable pageable) {
        return tenantRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tenant> getTenantsByStatus(TenantStatus status) {
        return tenantRepository.findByStatusAndDeletedFalse(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tenant> getTenantsByPlanType(PlanType planType) {
        return tenantRepository.findByPlanTypeAndDeletedFalse(planType);
    }

    // ════════════════════════════════════════════════════════════════════
    // Update
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public Tenant updateTenant(Long id, String name, String phone, String address) {
        log.info("Updating tenant id: {}", id);
        Tenant tenant = getTenantById(id);

        if (name != null) tenant.setName(name);
        if (phone != null) tenant.setPhone(phone);
        if (address != null) tenant.setAddress(address);

        return tenantRepository.save(tenant);
    }

    // ════════════════════════════════════════════════════════════════════
    // Plan Management
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public Tenant changePlan(Long tenantId, PlanType newPlan) {
        log.info("Changing plan for tenant {} to {}", tenantId, newPlan);
        Tenant tenant = getTenantById(tenantId);

        tenant.setPlanType(newPlan);

        // Update limits based on plan
        switch (newPlan) {
            case BASIC -> {
                tenant.setMaxPatientsPerMonth(50);
                tenant.setEnabledModules("[\"core\",\"auth\",\"clinical\"]");
                tenant.setStatus(TenantStatus.ACTIVE);
            }
            case PROFESSIONAL -> {
                tenant.setMaxPatientsPerMonth(null); // unlimited
                tenant.setEnabledModules("[\"core\",\"auth\",\"clinical\",\"inventory\",\"billing\",\"admin\",\"notification\",\"lab\"]");
                tenant.setStatus(TenantStatus.ACTIVE);
            }
            case ENTERPRISE -> {
                tenant.setMaxPatientsPerMonth(null); // unlimited
                tenant.setEnabledModules("[\"core\",\"auth\",\"clinical\",\"inventory\",\"billing\",\"admin\",\"notification\",\"lab\",\"custom\"]");
                tenant.setStatus(TenantStatus.ACTIVE);
            }
            default -> {
                // TRIAL keeps its own defaults
            }
        }

        // Extend subscription by 30 days from now
        tenant.setSubscriptionStart(LocalDate.now());
        tenant.setSubscriptionEnd(LocalDate.now().plusDays(30));

        return tenantRepository.save(tenant);
    }

    // ════════════════════════════════════════════════════════════════════
    // Status Management
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional
    public Tenant suspendTenant(Long tenantId) {
        log.warn("Suspending tenant id: {}", tenantId);
        Tenant tenant = getTenantById(tenantId);
        tenant.setStatus(TenantStatus.SUSPENDED);
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public Tenant activateTenant(Long tenantId) {
        log.info("Activating tenant id: {}", tenantId);
        Tenant tenant = getTenantById(tenantId);
        tenant.setStatus(TenantStatus.ACTIVE);
        return tenantRepository.save(tenant);
    }

    @Override
    @Transactional
    public void deleteTenant(Long tenantId) {
        log.warn("Soft-deleting tenant id: {}", tenantId);
        Tenant tenant = getTenantById(tenantId);
        tenant.setDeleted(true);
        tenantRepository.save(tenant);
    }

    // ════════════════════════════════════════════════════════════════════
    // Utility
    // ════════════════════════════════════════════════════════════════════

    @Override
    @Transactional(readOnly = true)
    public boolean isSubdomainAvailable(String subdomain) {
        return !tenantRepository.existsBySubdomain(subdomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tenant> getTenantsWithExpiringSubscription() {
        LocalDate cutoff = LocalDate.now().plusDays(7);
        return tenantRepository.findTenantsWithExpiringSubscription(cutoff);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveTenants() {
        return tenantRepository.countByStatusAndDeletedFalse(TenantStatus.ACTIVE);
    }
}
