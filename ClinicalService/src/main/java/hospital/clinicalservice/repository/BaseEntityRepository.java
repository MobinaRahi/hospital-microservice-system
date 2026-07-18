package hospital.clinicalservice.repository;

import hospital.clinicalservice.model.BaseEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Base repository interface with soft delete, versioning, and conflict handling.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Soft delete: Records are marked as deleted, not physically removed</li>
 *   <li>Optimistic locking: Version field prevents concurrent modification conflicts</li>
 *   <li>Conflict handling: Graceful error messages for concurrent updates</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 * public interface PatientRepository extends BaseEntityRepository&lt;Patient, Long&gt; {
 *     // Custom query methods here
 * }
 * </pre>
 *
 * @param <T> Entity type (must extend BaseEntity)
 * @param <ID> Primary key type (usually Long)
 *
 * @author Mobina
 */
@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    // ==================== Soft Delete Queries ====================

    /**
     * Finds an entity by ID, excluding soft-deleted records.
     * This is the standard "get by ID" method for most use cases.
     *
     * @param id Entity ID
     * @return Optional containing the entity if found and not deleted
     */
    @Query("select e from #{#entityName} e where e.id = :id and e.deleted = false")
    Optional<T> findNotDeletedById(@Param("id") ID id);

    /**
     * Finds an entity by ID, including soft-deleted records.
     * Used for: admin views, audit trails, data recovery.
     *
     * @param id Entity ID
     * @return Optional containing the entity if found (even if deleted)
     */
    @Query("select e from #{#entityName} e where e.id = :id")
    Optional<T> findByIdIncludingDeleted(@Param("id") ID id);

    /**
     * Finds all entities that are NOT soft-deleted.
     * This is the standard "get all" method for most use cases.
     */
    @Query("select e from #{#entityName} e where e.deleted = false")
    List<T> findAllNotDeleted();

    // ==================== Soft Delete Operations ====================

    /**
     * Soft-deletes an entity by setting deleted=true and deletedAt=now.
     * The record remains in the database but is filtered out of normal queries.
     *
     * @param id Entity ID to soft-delete
     * @param now Current timestamp
     * @return Number of rows affected (1 if successful, 0 if not found)
     */
    @Modifying
    @Transactional
    @Query("update #{#entityName} e set e.deleted = true, e.deletedAt = :now where e.id = :id")
    int softDeleteById(@Param("id") ID id, @Param("now") LocalDateTime now);

    /**
     * Convenience method for soft-deleting with automatic timestamp.
     *
     * @param id Entity ID to soft-delete
     * @return true if successfully deleted, false if not found
     */
    default boolean softDelete(ID id) {
        int result = softDeleteById(id, LocalDateTime.now());
        return result > 0;
    }

    // ==================== Update with Conflict Handling ====================

    /**
     * Updates an entity with optimistic locking and conflict handling.
     * If another user modified the record concurrently, throws ConcurrentModificationException.
     *
     * @param id Entity ID to update
     * @param updateLogic Consumer that applies updates to the entity
     * @return The updated entity
     * @throws EntityNotFoundException if entity not found
     * @throws ConcurrentModificationException if another user modified the record
     */
    default T updateWithConflictMessage(ID id, Consumer<T> updateLogic) {
        try {
            T entity = findNotDeletedById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
            updateLogic.accept(entity);
            return save(entity);
        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException(
                    "This record is being modified by another user. Please refresh and try again."
            );
        }
    }

    // ==================== Count Queries ====================

    /**
     * Counts all non-deleted entities.
     * Used for: dashboard statistics.
     */
    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long countNotDeleted();
}
