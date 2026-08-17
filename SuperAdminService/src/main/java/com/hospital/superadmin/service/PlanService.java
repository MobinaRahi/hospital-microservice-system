package com.hospital.superadmin.service;

import com.hospital.superadmin.dto.plan.PlanCreateDto;
import com.hospital.superadmin.dto.plan.PlanResponseDto;
import com.hospital.superadmin.dto.plan.PlanUpdateDto;
import com.hospital.superadmin.model.enums.PlanType;

import java.util.List;

/**
 * Service interface for Plan management.
 *
 * @author MobinaRahi
 */
public interface PlanService {

    PlanResponseDto createPlan(PlanCreateDto dto);

    PlanResponseDto getPlanById(Long id);

    PlanResponseDto getPlanByName(String name);

    List<PlanResponseDto> getAllPlans();

    List<PlanResponseDto> getActivePlans();

    List<PlanResponseDto> getPlansByType(PlanType planType);

    List<PlanResponseDto> searchPlansByName(String name);

    PlanResponseDto updatePlan(Long id, PlanUpdateDto dto);

    PlanResponseDto activatePlan(Long id);

    PlanResponseDto deactivatePlan(Long id);

    void deletePlan(Long id);

    boolean planNameExists(String name);

    long countByPlanType(PlanType planType);

    long countActivePlans();
}
