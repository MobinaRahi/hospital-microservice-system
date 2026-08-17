package com.hospital.superadmin.service.impl;

import com.hospital.superadmin.dto.plan.PlanCreateDto;
import com.hospital.superadmin.dto.plan.PlanResponseDto;
import com.hospital.superadmin.dto.plan.PlanUpdateDto;
import com.hospital.superadmin.mapper.PlanMapper;
import com.hospital.superadmin.model.Plan;
import com.hospital.superadmin.model.enums.PlanType;
import com.hospital.superadmin.repository.PlanRepository;
import com.hospital.superadmin.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PlanService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    public PlanResponseDto createPlan(PlanCreateDto dto) {
        log.info("Creating new plan: {}", dto.getName());

        if (planRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Plan with name '" + dto.getName() + "' already exists");
        }

        Plan plan = planMapper.toEntity(dto);
        Plan saved = planRepository.save(plan);
        log.info("Plan created with id: {}", saved.getId());

        return planMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponseDto getPlanById(Long id) {
        log.debug("Fetching plan by id: {}", id);

        Plan plan = planRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + id + " not found"));

        return planMapper.toResponseDto(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponseDto getPlanByName(String name) {
        log.debug("Fetching plan by name: {}", name);

        Plan plan = planRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Plan with name '" + name + "' not found"));

        return planMapper.toResponseDto(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponseDto> getAllPlans() {
        log.debug("Fetching all plans");

        List<Plan> plans = planRepository.findAllNotDeleted();
        return planMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponseDto> getActivePlans() {
        log.debug("Fetching active plans");

        List<Plan> plans = planRepository.findByIsActive(true);
        return planMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponseDto> getPlansByType(PlanType planType) {
        log.debug("Fetching plans by type: {}", planType);

        List<Plan> plans = planRepository.findByPlanType(planType);
        return planMapper.toResponseDtoList(plans);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponseDto> searchPlansByName(String name) {
        log.debug("Searching plans by name: {}", name);

        List<Plan> plans = planRepository.findByNameContainingIgnoreCase(name);
        return planMapper.toResponseDtoList(plans);
    }

    @Override
    public PlanResponseDto updatePlan(Long id, PlanUpdateDto dto) {
        log.info("Updating plan id: {}", id);

        Plan plan = planRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + id + " not found"));

        planMapper.updateEntity(dto, plan);
        Plan saved = planRepository.save(plan);

        return planMapper.toResponseDto(saved);
    }

    @Override
    public PlanResponseDto activatePlan(Long id) {
        log.info("Activating plan id: {}", id);

        Plan plan = planRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + id + " not found"));

        plan.activate();
        Plan saved = planRepository.save(plan);

        return planMapper.toResponseDto(saved);
    }

    @Override
    public PlanResponseDto deactivatePlan(Long id) {
        log.info("Deactivating plan id: {}", id);

        Plan plan = planRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + id + " not found"));

        plan.deactivate();
        Plan saved = planRepository.save(plan);

        return planMapper.toResponseDto(saved);
    }

    @Override
    public void deletePlan(Long id) {
        log.info("Soft-deleting plan id: {}", id);

        Plan plan = planRepository.findNotDeletedById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan with id " + id + " not found"));

        plan.softDelete(null);
        planRepository.save(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean planNameExists(String name) {
        return planRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByPlanType(PlanType planType) {
        return planRepository.countByPlanType(planType);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActivePlans() {
        return planRepository.countByIsActive(true);
    }
}
