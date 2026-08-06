package hospital.inventoryservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity for all entities in InventoryService.
 * Provides common fields for multi-tenancy, auditing, soft delete, and optimistic locking.
 *
 * <p><strong>Fields:</strong></p>
 * <ul>
 *   <li>{@code tenantId} — Multi-tenant data isolation</li>
 *   <li>{@code deleted}, {@code deletedAt}, {@code deletedBy} — Soft delete support</li>
 *   <li>{@code version} — Optimistic locking (JPA @Version)</li>
 *   <li>{@code createdAt}, {@code updatedAt} — Automatic audit timestamps</li>
 *   <li>{@code createdBy}, {@code updatedBy} — Automatic audit user tracking</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    /**
     * Tenant ID for multi-tenant SaaS data isolation.
     * Every entity that extends BaseEntity will automatically be filtered by this field.
     * null = global/tenant-agnostic data (e.g., system-level config).
     */
    @Column(name = "tenant_id")
    private Long tenantId;

    /**
     * Whether this record has been soft-deleted.
     * Queries should filter by this field to exclude deleted records.
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
     * {@link jakarta.persistence.OptimisticLockException} if the version has changed.
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
     */
    public void restore() {
        this.deleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
    }
}
