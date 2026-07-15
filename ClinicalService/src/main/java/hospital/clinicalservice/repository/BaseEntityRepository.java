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

@NoRepositoryBean
public interface BaseEntityRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    @Query("select e from #{#entityName} e where e.id = :id and e.deleted = false")
    Optional<T> findNotDeletedById(@Param("id") ID id);

    @Query("select e from #{#entityName} e where e.id = :id")
    Optional<T> findByIdIncludingDeleted(@Param("id") ID id);

    @Query("select e from #{#entityName} e where e.deleted = false")
    List<T> findAllNotDeleted();

    @Modifying
    @Transactional
    @Query("update #{#entityName} e set e.deleted = true, e.deletedAt = :now where e.id = :id")
    void softDeleteById(@Param("id") ID id, @Param("now") LocalDateTime now);

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

    @Query("select count(e) from #{#entityName} e where e.deleted = false")
    long countNotDeleted();
}
