package hospital.clinicalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for ClinicalService Spring Boot application.
 *
 * <p><strong>What this service does:</strong></p>
 * <ul>
 *   <li>Manages patient encounters (visits)</li>
 *   <li>Handles diagnoses with ICD-10 codes</li>
 *   <li>Tracks vital signs with LOINC codes</li>
 *   <li>Manages prescriptions and medications</li>
 *   <li>Stores nursing notes and triage assessments</li>
 *   <li>Tracks patient allergies</li>
 * </ul>
 *
 * <p><strong>Port:</strong> 8083</p>
 * <p><strong>Database:</strong> Oracle (clinical schema)</p>
 *
 * @author Mobina
 */
@SpringBootApplication
@EnableJpaAuditing
public class ClinicalServiceApplication {

    /**
     * Starts the ClinicalService application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ClinicalServiceApplication.class, args);
    }
}
