package hospital.adminservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity for all entities in AdminService.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Multi-tenancy: All entities have a tenantId for data isolation</li>
 *   <li>Soft delete: Entities are marked as deleted instead of being removed</li>
 *   <li>Audit fields: createdAt, updatedAt, createdBy, updatedBy</li>
 *   <li>Optimistic locking: Version field prevents concurrent modification conflicts</li>
 *   <li>Hibernate Filter: Automatically filters queries by tenantId</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * public class Hospital extends BaseEntity {
 *     // entity-specific fields
 * }
 * </pre>
 *
 * @author MobinaRahi
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@SQLRestriction("deleted = false")
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "tenantId", type = Long.class))
public abstract class BaseEntity {

    @Column(name = "tenant_id")
    @Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
    private Long tenantId;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    private Long deletedBy;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    public void softDelete(Long userId) {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
