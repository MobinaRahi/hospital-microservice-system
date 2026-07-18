package hospital.coreservice.repository;

import hospital.coreservice.model.Role;
import hospital.coreservice.model.enums.RoleName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * Repository for Role entity.
 * Provides query methods by name.
 *
 * @author Mobina
 */
@Repository
public interface RoleRepository extends BaseEntityRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);

    boolean existsByName(RoleName name);

    @Query("SELECT r FROM roleEntity r LEFT JOIN FETCH r.permissions WHERE r.id = :id AND r.deleted = false")
    Optional<Role> findNotDeletedByIdWithPermissions(@Param("id") Long id);

    @Query("SELECT r FROM roleEntity r LEFT JOIN FETCH r.permissions WHERE r.name = :name AND r.deleted = false")
    Optional<Role> findNotDeletedByNameWithPermissions(@Param("name") RoleName name);

    Set<Role> findByNameIn(Set<RoleName> names);

    @Query("""
        SELECT r FROM roleEntity r
        JOIN r.permissions p
        WHERE p.name = :permissionName AND r.deleted = false
    """)
    Set<Role> findAllNotDeletedRolesByPermission(@Param("permissionName") String permissionName);

    // ========== User-based ==========
    @Query("""
        SELECT DISTINCT r FROM roleEntity r
        JOIN r.users u
        WHERE u.id = :userId AND r.deleted = false
    """)
    Set<Role> findAllNotDeletedRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) FROM userEntity u JOIN u.roles r WHERE r.id = :roleId AND u.deleted = false")
    long countNotDeletedUsersByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT r FROM roleEntity r WHERE r.name = 'ROLE_USER' AND r.deleted = false")
    Optional<Role> findDefaultUserRole();

    @Query("select r from roleEntity r where r.name='name'and r.deleted=false ")
    Set<Role>findByNameAndDeletedFalse(@Param("name") RoleName name);
}