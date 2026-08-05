package hospital.authservice.repository;

import hospital.authservice.model.enums.TenantStatus;
import hospital.authservice.model.tenant.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Tenant} entities.
 * Provides methods for tenant lookup, search, and subscription management.
 *
 * @author MobinaRahi
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    /**
     * Finds a tenant by its unique subdomain.
     */
    Optional<Tenant> findBySubdomainAndDeletedFalse(String subdomain);

    /**
     * Finds a tenant by admin email.
     */
    Optional<Tenant> findByAdminEmailAndDeletedFalse(String adminEmail);

    /**
     * Checks if a subdomain is already taken.
     */
    boolean existsBySubdomain(String subdomain);

    /**
     * Finds all active tenants.
     */
    List<Tenant> findByStatusAndDeletedFalse(TenantStatus status);

    /**
     * Finds all tenants with a specific plan type.
     */
    List<Tenant> findByPlanTypeAndDeletedFalse(hospital.authservice.model.enums.PlanType planType);

    /**
     * Paginated search of all non-deleted tenants.
     */
    Page<Tenant> findAllByDeletedFalse(Pageable pageable);

    /**
     * Finds tenants whose subscription is expiring soon.
     */
    @Query("SELECT t FROM tenantEntity t WHERE t.subscriptionEnd <= :cutoff AND t.status = 'ACTIVE' AND t.deleted = false")
    List<Tenant> findTenantsWithExpiringSubscription(java.time.LocalDate cutoff);

    /**
     * Counts total active tenants.
     */
    long countByStatusAndDeletedFalse(TenantStatus status);
}
