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
 * سرویس جستجوی کدهای استاندارد (ICD-10 و LOINC)
 * داده‌ها از فایل‌های JSON خوانده می‌شوند
 */
@Slf4j
@Service
public class CodeSearchService {

    private List<IcdCode> icdCodes = new ArrayList<>();
    private List<LoincCode> loincCodes = new ArrayList<>();

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

    /**
     * جستجوی ICD-10 بر اساس کد یا نام
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

    /**
     * جستجوی LOINC بر اساس کد یا نام
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

    /**
     * دریافت ICD بر اساس کد
     */
    public IcdCode getIcdByCode(String code) {
        return icdCodes.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    /**
     * دریافت LOINC بر اساس کد
     */
    public LoincCode getLoincByCode(String code) {
        return loincCodes.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    // ==================== Inner Classes ====================

    @Getter
    public static class IcdCode {
        private String code;
        private String description;
        private String persianName;
        private String category;
    }

    @Getter
    public static class LoincCode {
        private String code;
        private String name;
        private String persianName;
        private String unit;
        private Double normalLow;
        private Double normalHigh;
    }
}
