package hospital.clinicalservice.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity for all clinical service entities.
 * Provides common fields: soft delete, versioning, auditing timestamps.
 *
 * @author Mobina
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BaseEntity {

    /** Soft delete flag */
    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    /** Timestamp of soft delete */
    private LocalDateTime deletedAt;

    /** User ID who performed the soft delete */
    private Long deletedBy;

    /** Version counter for optimistic locking */
    @Version
    @Column(nullable = false)
    @Builder.Default
    private Long version = 0L;

    /** Record creation timestamp (auto-set by JPA) */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Last modification timestamp (auto-set by JPA) */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** User ID who created the record */
    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    /** User ID who last modified the record */
    @LastModifiedBy
    private Long updatedBy;
}
