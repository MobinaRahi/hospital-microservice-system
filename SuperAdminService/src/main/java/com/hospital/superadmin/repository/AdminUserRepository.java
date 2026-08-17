package com.hospital.superadmin.repository;

import com.hospital.superadmin.model.AdminUser;
import com.hospital.superadmin.model.enums.AdminStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for AdminUser entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface AdminUserRepository extends BaseEntityRepository<AdminUser, Long> {

    Optional<AdminUser> findByUserId(Long userId);

    Optional<AdminUser> findByEmail(String email);

    List<AdminUser> findByStatus(AdminStatus status);

    List<AdminUser> findByStatusAndDeletedFalse(AdminStatus status);

    boolean existsByUserId(Long userId);

    boolean existsByEmail(String email);

    long countByStatus(AdminStatus status);
}
