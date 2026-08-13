package hospital.adminservice.service.impl;

import hospital.adminservice.dto.systemconfig.SystemConfigCreateDto;
import hospital.adminservice.dto.systemconfig.SystemConfigResponseDto;
import hospital.adminservice.dto.systemconfig.SystemConfigUpdateDto;
import hospital.adminservice.exception.systemconfig.ConfigNotEditableException;
import hospital.adminservice.exception.systemconfig.DuplicateConfigKeyException;
import hospital.adminservice.exception.systemconfig.SystemConfigNotFoundException;
import hospital.adminservice.mapper.SystemConfigMapper;
import hospital.adminservice.model.SystemConfiguration;
import hospital.adminservice.repository.SystemConfigRepository;
import hospital.adminservice.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;
    private final SystemConfigMapper systemConfigMapper;

    @Override
    public SystemConfigResponseDto createConfig(SystemConfigCreateDto dto) {
        log.info("Creating system config: {}", dto.getConfigKey());

        if (systemConfigRepository.existsByConfigKey(dto.getConfigKey())) {
            throw new DuplicateConfigKeyException(dto.getConfigKey());
        }

        SystemConfiguration config = systemConfigMapper.toEntity(dto);
        SystemConfiguration saved = systemConfigRepository.save(config);
        log.info("System config created with id: {}", saved.getId());

        return systemConfigMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemConfigResponseDto getConfigById(Long id) {
        log.debug("Fetching system config by id: {}", id);

        SystemConfiguration config = systemConfigRepository.findNotDeletedById(id)
                .orElseThrow(() -> SystemConfigNotFoundException.byId(id));

        return systemConfigMapper.toResponseDto(config);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemConfigResponseDto getConfigByKey(String configKey) {
        log.debug("Fetching system config by key: {}", configKey);

        SystemConfiguration config = systemConfigRepository.findByConfigKey(configKey)
                .orElseThrow(() -> SystemConfigNotFoundException.byKey(configKey));

        return systemConfigMapper.toResponseDto(config);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemConfigResponseDto> getConfigsByCategory(String category) {
        log.debug("Fetching system configs by category: {}", category);

        List<SystemConfiguration> configs = systemConfigRepository.findByCategory(category);
        return systemConfigMapper.toResponseDtoList(configs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemConfigResponseDto> getEditableConfigs() {
        log.debug("Fetching editable system configs");

        List<SystemConfiguration> configs = systemConfigRepository.findByIsEditableTrue();
        return systemConfigMapper.toResponseDtoList(configs);
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValue(String configKey) {
        return systemConfigRepository.findConfigValueByKey(configKey);
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValueOrDefault(String configKey, String defaultValue) {
        String value = systemConfigRepository.findConfigValueByKey(configKey);
        return value != null ? value : defaultValue;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getIntConfigValue(String configKey) {
        SystemConfiguration config = systemConfigRepository.findByConfigKey(configKey).orElse(null);
        return config != null ? config.getIntValue() : null;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean getBoolConfigValue(String configKey) {
        SystemConfiguration config = systemConfigRepository.findByConfigKey(configKey).orElse(null);
        return config != null ? config.getBoolValue() : null;
    }

    @Override
    @CacheEvict(value = "systemConfig", allEntries = true)
    public SystemConfigResponseDto updateConfig(Long id, SystemConfigUpdateDto dto) {
        log.info("Updating system config id: {}", id);

        SystemConfiguration config = systemConfigRepository.findNotDeletedById(id)
                .orElseThrow(() -> SystemConfigNotFoundException.byId(id));

        if (Boolean.FALSE.equals(config.getIsEditable())) {
            throw new ConfigNotEditableException(config.getConfigKey());
        }

        systemConfigMapper.updateEntity(dto, config);
        SystemConfiguration saved = systemConfigRepository.save(config);
        log.info("System config updated id: {}", saved.getId());

        return systemConfigMapper.toResponseDto(saved);
    }

    @Override
    @CacheEvict(value = "systemConfig", key = "#configKey")
    public void updateConfigValue(String configKey, String configValue) {
        log.info("Updating config value for key: {}", configKey);
        systemConfigRepository.updateConfigValue(configKey, configValue);
    }

    @Override
    @CacheEvict(value = "systemConfig", allEntries = true)
    public void deleteConfig(Long id) {
        log.info("Soft-deleting system config id: {}", id);

        SystemConfiguration config = systemConfigRepository.findNotDeletedById(id)
                .orElseThrow(() -> SystemConfigNotFoundException.byId(id));

        config.softDelete(null);
        systemConfigRepository.save(config);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean keyExists(String configKey) {
        return systemConfigRepository.existsByConfigKey(configKey);
    }
}
