package hospital.billingservice.repository;

import hospital.billingservice.model.PatientInsurance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for PatientInsurance entity.
 *
 * @author MobinaRahi
 */
@Repository
public interface PatientInsuranceRepository extends BaseEntityRepository<PatientInsurance, Long> {

    /**
     * Finds all insurance records for a patient.
     *
     * @param patientId the patient ID
     * @return list of patient insurance records
     */
    List<PatientInsurance> findByPatientId(Long patientId);

    /**
     * Finds the primary insurance for a patient.
     *
     * @param patientId the patient ID
     * @return primary insurance record if found
     */
    Optional<PatientInsurance> findByPatientIdAndIsPrimaryTrue(Long patientId);

    /**
     * Finds a patient insurance record by policy number.
     *
     * @param policyNumber the policy number
     * @return patient insurance record if found
     */
    Optional<PatientInsurance> findByPolicyNumber(String policyNumber);

    /**
     * Checks if a policy number already exists.
     *
     * @param policyNumber the policy number to check
     * @return true if exists
     */
    boolean existsByPolicyNumber(String policyNumber);

    /**
     * Finds all active (valid) insurance records for a patient.
     * A record is valid if expiryDate is null or in the future.
     *
     * @param patientId the patient ID
     * @return list of valid insurance records
     */
    List<PatientInsurance> findByPatientIdAndExpiryDateIsNull(Long patientId);
}
