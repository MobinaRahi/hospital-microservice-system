package hospital.notificationservice.service.impl;

import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateCreateDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateResponseDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateUpdateDto;
import hospital.notificationservice.exception.notificationtemplate.DuplicateTemplateNameException;
import hospital.notificationservice.exception.notificationtemplate.InactiveTemplateException;
import hospital.notificationservice.exception.notificationtemplate.NotificationTemplateNotFoundException;
import hospital.notificationservice.mapper.NotificationTemplateMapper;
import hospital.notificationservice.model.NotificationTemplate;
import hospital.notificationservice.model.enums.TemplateType;
import hospital.notificationservice.repository.NotificationTemplateRepository;
import hospital.notificationservice.service.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link NotificationTemplateService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationTemplateMapper notificationTemplateMapper;

    @Override
    public NotificationTemplateResponseDto createTemplate(NotificationTemplateCreateDto dto) {
        log.info("Creating notification template: {}", dto.getName());

        if (notificationTemplateRepository.existsByName(dto.getName())) {
            throw new DuplicateTemplateNameException(dto.getName());
        }

        NotificationTemplate template = notificationTemplateMapper.toEntity(dto);
        NotificationTemplate saved = notificationTemplateRepository.save(template);
        log.info("Notification template created with id: {}", saved.getId());

        return notificationTemplateMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponseDto getTemplateById(Long id) {
        log.debug("Fetching notification template by id: {}", id);

        NotificationTemplate template = notificationTemplateRepository.findNotDeletedById(id)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byId(id));

        return notificationTemplateMapper.toResponseDto(template);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponseDto getTemplateByName(String name) {
        log.debug("Fetching notification template by name: {}", name);

        NotificationTemplate template = notificationTemplateRepository.findByName(name)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byName(name));

        return notificationTemplateMapper.toResponseDto(template);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponseDto> getAllTemplates() {
        log.debug("Fetching all notification templates");

        List<NotificationTemplate> templates = notificationTemplateRepository.findAllNotDeleted();
        return notificationTemplateMapper.toResponseDtoList(templates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponseDto> getTemplatesByType(TemplateType type) {
        log.debug("Fetching notification templates by type: {}", type);

        List<NotificationTemplate> templates = notificationTemplateRepository.findByType(type);
        return notificationTemplateMapper.toResponseDtoList(templates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponseDto> getActiveTemplates() {
        log.debug("Fetching active notification templates");

        List<NotificationTemplate> templates = notificationTemplateRepository.findByIsActive(true);
        return notificationTemplateMapper.toResponseDtoList(templates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponseDto> getActiveTemplatesByType(TemplateType type) {
        log.debug("Fetching active notification templates by type: {}", type);

        List<NotificationTemplate> templates = notificationTemplateRepository.findByTypeAndIsActive(type, true);
        return notificationTemplateMapper.toResponseDtoList(templates);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponseDto> searchTemplatesByName(String name) {
        log.debug("Searching notification templates by name: {}", name);

        List<NotificationTemplate> templates = notificationTemplateRepository.findByNameContainingIgnoreCase(name);
        return notificationTemplateMapper.toResponseDtoList(templates);
    }

    @Override
    public NotificationTemplateResponseDto updateTemplate(Long id, NotificationTemplateUpdateDto dto) {
        log.info("Updating notification template id: {}", id);

        NotificationTemplate template = notificationTemplateRepository.findNotDeletedById(id)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byId(id));

        notificationTemplateMapper.updateEntity(dto, template);
        NotificationTemplate saved = notificationTemplateRepository.save(template);

        return notificationTemplateMapper.toResponseDto(saved);
    }

    @Override
    public NotificationTemplateResponseDto activateTemplate(Long id) {
        log.info("Activating notification template id: {}", id);

        NotificationTemplate template = notificationTemplateRepository.findNotDeletedById(id)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byId(id));

        template.activate();
        NotificationTemplate saved = notificationTemplateRepository.save(template);

        return notificationTemplateMapper.toResponseDto(saved);
    }

    @Override
    public NotificationTemplateResponseDto deactivateTemplate(Long id) {
        log.info("Deactivating notification template id: {}", id);

        NotificationTemplate template = notificationTemplateRepository.findNotDeletedById(id)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byId(id));

        template.deactivate();
        NotificationTemplate saved = notificationTemplateRepository.save(template);

        return notificationTemplateMapper.toResponseDto(saved);
    }

    @Override
    public void deleteTemplate(Long id) {
        log.info("Soft-deleting notification template id: {}", id);

        NotificationTemplate template = notificationTemplateRepository.findNotDeletedById(id)
                .orElseThrow(() -> NotificationTemplateNotFoundException.byId(id));

        template.softDelete(null);
        notificationTemplateRepository.save(template);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean templateNameExists(String name) {
        return notificationTemplateRepository.existsByName(name);
    }
}
