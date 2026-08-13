package hospital.notificationservice.model;

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
 * Base entity for all entities in NotificationService.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li><b>Multi-tenancy:</b> All entities have a tenantId for data isolation</li>
 *   <li><b>Soft delete:</b> Entities are marked as deleted instead of being removed</li>
 *   <li><b>Audit fields:</b> createdAt, updatedAt, createdBy, updatedBy</li>
 *   <li><b>Optimistic locking:</b> Version field prevents concurrent modification conflicts</li>
 *   <li><b>Hibernate Filter:</b> Automatically filters queries by tenantId</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * public class SMSGateway extends BaseEntity {
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

    /**
     * Tenant ID for multi-tenant SaaS data isolation.
     * Every entity that extends BaseEntity will automatically be filtered by this field.
     */
    @Column(name = "tenant_id")
    @Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
    private Long tenantId;

    /**
     * Whether this record is soft-deleted.
     */
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    /**
     * When this record was soft-deleted.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * ID of the user who soft-deleted this record.
     */
    @Column(name = "deleted_by")
    private Long deletedBy;

    /**
     * Optimistic locking version.
     * JPA will automatically increment this on every update and throw
     * OptimisticLockException if the version has changed.
     */
    @Version
    @Column(nullable = false)
    private Long version = 0L;

    /**
     * When this record was created.
     * Automatically set by JPA auditing.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When this record was last updated.
     * Automatically set by JPA auditing.
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * ID of the user who created this record.
     * Automatically set by JPA auditing from SecurityContext.
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    /**
     * ID/username of the user who last updated this record.
     * Automatically set by JPA auditing from SecurityContext.
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Marks this record as soft-deleted.
     * Sets deleted=true, deletedAt=now, deletedBy=userId.
     *
     * @param userId the ID of the user performing the delete
     */
    public void softDelete(Long userId) {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }

    /**
     * Restores this record from soft-deleted state.
     * Clears deleted flag and related fields.
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
