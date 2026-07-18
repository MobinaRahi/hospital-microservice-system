package hospital.clinicalservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for searching ICD-10 and LOINC codes from JSON dictionaries.
 *
 * <p><strong>How it works:</strong></p>
 * <ol>
 *   <li>On startup (@PostConstruct), loads codes from JSON files</li>
 *   <li>Stores codes in memory for fast search</li>
 *   <li>Provides search methods for autocomplete in forms</li>
 * </ol>
 *
 * <p><strong>Data sources:</strong></p>
 * <ul>
 *   <li>ICD-10: src/main/resources/data/icd10.json (disease codes)</li>
 *   <li>LOINC: src/main/resources/data/loinc.json (lab test codes)</li>
 * </ul>
 *
 * @author Mobina
 */
@Slf4j
@Service
public class CodeSearchService {

    /** In-memory list of ICD-10 codes loaded from JSON */
    private List<IcdCode> icdCodes = new ArrayList<>();

    /** In-memory list of LOINC codes loaded from JSON */
    private List<LoincCode> loincCodes = new ArrayList<>();

    /**
     * Loads codes from JSON files on application startup.
     * Called automatically by Spring after bean creation.
     */
    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream icdStream = new ClassPathResource("data/icd10.json").getInputStream();
            icdCodes = mapper.readValue(icdStream, new TypeReference<List<IcdCode>>() {});

            InputStream loincStream = new ClassPathResource("data/loinc.json").getInputStream();
            loincCodes = mapper.readValue(loincStream, new TypeReference<List<LoincCode>>() {});

            log.info("Loaded {} ICD-10 codes and {} LOINC codes", icdCodes.size(), loincCodes.size());
        } catch (Exception e) {
            log.error("Failed to load code dictionaries", e);
        }
    }

    // ==================== ICD-10 Search ====================

    /**
     * Searches ICD-10 codes by query string.
     * Matches against: code, description, Persian name, category.
     * Returns max 20 results for autocomplete.
     *
     * @param query Search query (null = return all)
     * @return List of matching ICD-10 codes
     */
    public List<IcdCode> searchIcd(String query) {
        if (query == null || query.isBlank()) return icdCodes;
        String q = query.toLowerCase();
        return icdCodes.stream()
                .filter(c -> c.getCode().toLowerCase().contains(q)
                        || c.getDescription().toLowerCase().contains(q)
                        || c.getPersianName().contains(q)
                        || c.getCategory().toLowerCase().contains(q))
                .limit(20)
                .collect(Collectors.toList());
    }

    // ==================== LOINC Search ====================

    /**
     * Searches LOINC codes by query string.
     * Matches against: code, name, Persian name.
     * Returns max 20 results for autocomplete.
     *
     * @param query Search query (null = return all)
     * @return List of matching LOINC codes
     */
    public List<LoincCode> searchLoinc(String query) {
        if (query == null || query.isBlank()) return loincCodes;
        String q = query.toLowerCase();
        return loincCodes.stream()
                .filter(c -> c.getCode().toLowerCase().contains(q)
                        || c.getName().toLowerCase().contains(q)
                        || c.getPersianName().contains(q))
                .limit(20)
                .collect(Collectors.toList());
    }

    // ==================== Get by Code ====================

    /**
     * Gets ICD-10 code details by exact code.
     *
     * @param code ICD-10 code (e.g., "I10")
     * @return IcdCode if found, null otherwise
     */
    public IcdCode getIcdByCode(String code) {
        return icdCodes.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets LOINC code details by exact code.
     *
     * @param code LOINC code (e.g., "8480-6")
     * @return LoincCode if found, null otherwise
     */
    public LoincCode getLoincByCode(String code) {
        return loincCodes.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    // ==================== Inner Classes ====================

    /**
     * ICD-10 code representation.
     * Loaded from icd10.json file.
     */
    @Getter
    public static class IcdCode {
        /** ICD-10 code (e.g., "I10") */
        private String code;
        /** English description (e.g., "Essential hypertension") */
        private String description;
        /** Persian name (e.g., "فشار خون بالا") */
        private String persianName;
        /** Category (e.g., "CARDIOVASCULAR") */
        private String category;
    }

    /**
     * LOINC code representation.
     * Loaded from loinc.json file.
     */
    @Getter
    public static class LoincCode {
        /** LOINC code (e.g., "8480-6") */
        private String code;
        /** English name (e.g., "Systolic Blood Pressure") */
        private String name;
        /** Persian name (e.g., "فشار خون سیستولیک") */
        private String persianName;
        /** Unit of measurement (e.g., "mmHg") */
        private String unit;
        /** Normal range low value */
        private Double normalLow;
        /** Normal range high value */
        private Double normalHigh;
    }
}
