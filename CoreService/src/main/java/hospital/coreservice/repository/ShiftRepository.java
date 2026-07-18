package hospital.coreservice.repository;

import hospital.coreservice.model.Shift;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Shift entity.
 * Provides query methods by name, night shift, and active status.
 *
 * @author Mobina
 */
@Repository
public interface ShiftRepository extends BaseEntityRepository<Shift,Long> {

    Optional<Shift> findByName(String name);

    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = true")
    List<Shift> findAllNightShifts();

    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = true AND s.isActive=true ")
    List<Shift> findAllActiveNightShifts();

    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = false")
    List<Shift> findAllDayShifts();

    @Query("SELECT s FROM shiftEntity s WHERE s.nightShift = false AND s.isActive=true ")
    List<Shift> findAllActiveDayShifts();

    @Query("SELECT s FROM shiftEntity s WHERE s.hasExtraPay = true")
    List<Shift> findAllShiftsWithExtraPay();

    @Query("SELECT s FROM shiftEntity s WHERE s.hasExtraPay = true AND s.isActive=true ")
    List<Shift> findAllActiveShiftsWithExtraPay();

    boolean existsByName(String name);

    @Query("SELECT d FROM shiftEntity d WHERE d.id = :id AND d.isActive = true")
    Optional<Shift> findActiveById(@Param("id") Long id);

    @Query("SELECT d FROM shiftEntity d WHERE d.isActive = true")
    List<Shift> findAllActive();

    @Query("SELECT d FROM shiftEntity d WHERE d.isActive = false")
    List<Shift> findAllInactive();

    @Modifying
    @Query("UPDATE shiftEntity d SET d.isActive = false WHERE d.id = :id")
    void deactivate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE shiftEntity d SET d.isActive = true WHERE d.id = :id")
    void activate(@Param("id") Long id);

    @Query("select count(s) from shiftEntity s where s.isActive=true ")
    Long countActive();

    @Query("select count(s) from shiftEntity s where s.isActive=false ")
    Long countInactive();
}