package hospital.adminservice.service;

import hospital.adminservice.dto.systemconfig.SystemConfigCreateDto;
import hospital.adminservice.dto.systemconfig.SystemConfigResponseDto;
import hospital.adminservice.exception.systemconfig.ConfigNotEditableException;
import hospital.adminservice.exception.systemconfig.DuplicateConfigKeyException;
import hospital.adminservice.exception.systemconfig.SystemConfigNotFoundException;
import hospital.adminservice.mapper.SystemConfigMapper;
import hospital.adminservice.model.SystemConfiguration;
import hospital.adminservice.repository.SystemConfigRepository;
import hospital.adminservice.service.impl.SystemConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SystemConfigServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class SystemConfigServiceImplTest {

    @Mock private SystemConfigRepository systemConfigRepository;
    @Mock private SystemConfigMapper systemConfigMapper;

    @InjectMocks
    private SystemConfigServiceImpl systemConfigService;

    private SystemConfiguration testConfig;

    @BeforeEach
    void setUp() {
        testConfig = SystemConfiguration.builder()
                .id(1L)
                .configKey("MAX_APPOINTMENTS_PER_DAY")
                .configValue("50")
                .category("APPOINTMENT")
                .dataType("INTEGER")
                .isEditable(true)
                .build();
    }

    @Nested
    @DisplayName("Create Config")
    class CreateConfigTests {

        @Test
        @DisplayName("should create config successfully")
        void shouldCreateConfig() {
            SystemConfigCreateDto dto = SystemConfigCreateDto.builder()
                    .configKey("MAX_APPOINTMENTS_PER_DAY")
                    .configValue("50")
                    .category("APPOINTMENT")
                    .dataType("INTEGER")
                    .build();

            when(systemConfigRepository.existsByConfigKey("MAX_APPOINTMENTS_PER_DAY")).thenReturn(false);
            when(systemConfigMapper.toEntity(any(SystemConfigCreateDto.class))).thenReturn(testConfig);
            when(systemConfigMapper.toResponseDto(any(SystemConfiguration.class)))
                    .thenReturn(SystemConfigResponseDto.builder().id(1L).build());
            when(systemConfigRepository.save(any(SystemConfiguration.class))).thenReturn(testConfig);

            SystemConfigResponseDto result = systemConfigService.createConfig(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(systemConfigRepository).save(any(SystemConfiguration.class));
        }

        @Test
        @DisplayName("should throw when key exists")
        void shouldThrowWhenKeyExists() {
            SystemConfigCreateDto dto = SystemConfigCreateDto.builder()
                    .configKey("MAX_APPOINTMENTS_PER_DAY").build();

            when(systemConfigRepository.existsByConfigKey("MAX_APPOINTMENTS_PER_DAY")).thenReturn(true);

            assertThatThrownBy(() -> systemConfigService.createConfig(dto))
                    .isInstanceOf(DuplicateConfigKeyException.class);
        }
    }

    @Nested
    @DisplayName("Read Config")
    class ReadConfigTests {

        @Test
        @DisplayName("should get config by id")
        void shouldGetById() {
            SystemConfigResponseDto expected = SystemConfigResponseDto.builder().id(1L).build();

            when(systemConfigRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testConfig));
            when(systemConfigMapper.toResponseDto(testConfig)).thenReturn(expected);

            SystemConfigResponseDto result = systemConfigService.getConfigById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(systemConfigRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> systemConfigService.getConfigById(999L))
                    .isInstanceOf(SystemConfigNotFoundException.class);
        }

        @Test
        @DisplayName("should get config by key")
        void shouldGetByKey() {
            when(systemConfigRepository.findByConfigKey("MAX_APPOINTMENTS_PER_DAY"))
                    .thenReturn(Optional.of(testConfig));
            when(systemConfigMapper.toResponseDto(any(SystemConfiguration.class)))
                    .thenReturn(SystemConfigResponseDto.builder().build());

            SystemConfigResponseDto result = systemConfigService.getConfigByKey("MAX_APPOINTMENTS_PER_DAY");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should get configs by category")
        void shouldGetByCategory() {
            when(systemConfigRepository.findByCategory("APPOINTMENT")).thenReturn(List.of(testConfig));
            when(systemConfigMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    SystemConfigResponseDto.builder().id(1L).build()));

            assertThat(systemConfigService.getConfigsByCategory("APPOINTMENT")).hasSize(1);
        }

        @Test
        @DisplayName("should get int config value")
        void shouldGetIntConfigValue() {
            when(systemConfigRepository.findByConfigKey("MAX_APPOINTMENTS_PER_DAY"))
                    .thenReturn(Optional.of(testConfig));

            Integer value = systemConfigService.getIntConfigValue("MAX_APPOINTMENTS_PER_DAY");

            assertThat(value).isEqualTo(50);
        }

        @Test
        @DisplayName("should get bool config value")
        void shouldGetBoolConfigValue() {
            testConfig.setConfigValue("true");
            when(systemConfigRepository.findByConfigKey("ENABLE_NOTIFICATION"))
                    .thenReturn(Optional.of(testConfig));

            Boolean value = systemConfigService.getBoolConfigValue("ENABLE_NOTIFICATION");

            assertThat(value).isTrue();
        }
    }

    @Nested
    @DisplayName("Update Config")
    class UpdateConfigTests {

        @Test
        @DisplayName("should throw when config not editable")
        void shouldThrowWhenNotEditable() {
            testConfig.setIsEditable(false);
            when(systemConfigRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testConfig));

            assertThatThrownBy(() -> systemConfigService.updateConfig(1L,
                    new hospital.adminservice.dto.systemconfig.SystemConfigUpdateDto()))
                    .isInstanceOf(ConfigNotEditableException.class);
        }
    }

    @Nested
    @DisplayName("Delete Config")
    class DeleteConfigTests {

        @Test
        @DisplayName("should soft delete config")
        void shouldSoftDelete() {
            when(systemConfigRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testConfig));

            systemConfigService.deleteConfig(1L);

            verify(systemConfigRepository).save(argThat(SystemConfiguration::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check key existence")
        void shouldCheckKeyExistence() {
            when(systemConfigRepository.existsByConfigKey("MAX_APPOINTMENTS_PER_DAY")).thenReturn(true);

            assertThat(systemConfigService.keyExists("MAX_APPOINTMENTS_PER_DAY")).isTrue();
        }
    }
}
