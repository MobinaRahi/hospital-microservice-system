package com.hospital.superadmin.repository;

import com.hospital.superadmin.model.Plan;
import com.hospital.superadmin.model.enums.PlanType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Plan entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface PlanRepository extends BaseEntityRepository<Plan, Long> {

    Optional<Plan> findByName(String name);

    List<Plan> findByPlanType(PlanType planType);

    List<Plan> findByIsActive(Boolean isActive);

    List<Plan> findByPlanTypeAndIsActive(PlanType planType, Boolean isActive);

    @Query("SELECT p FROM Plan p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.deleted = false")
    List<Plan> findByNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByName(String name);

    long countByPlanType(PlanType planType);

    long countByIsActive(Boolean isActive);
}
