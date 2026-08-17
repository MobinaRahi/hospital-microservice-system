package hospital.coreservice.repository;

import hospital.coreservice.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for QueueEntry entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    List<QueueEntry> findByDoctorId(Long doctorId);

    List<QueueEntry> findByDoctorIdAndQueueDate(Long doctorId, LocalDate queueDate);

    @Query("SELECT q FROM QueueEntry q WHERE q.queueDate = :date AND q.status = 'CHECKED_IN' AND q.deleted = false ORDER BY q.priority, q.queuePosition")
    List<QueueEntry> findTodayActiveQueue(@Param("date") LocalDate date);

    @Query("SELECT q FROM QueueEntry q WHERE q.doctorId = :doctorId AND q.queueDate = :date AND q.status = 'CHECKED_IN' AND q.deleted = false ORDER BY q.priority, q.queuePosition")
    List<QueueEntry> findDoctorQueue(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    @Query("SELECT MAX(q.queuePosition) FROM QueueEntry q WHERE q.doctorId = :doctorId AND q.queueDate = :date AND q.deleted = false")
    Integer findMaxQueuePosition(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    long countByDoctorIdAndQueueDateAndStatus(Long doctorId, LocalDate queueDate, String status);
}
