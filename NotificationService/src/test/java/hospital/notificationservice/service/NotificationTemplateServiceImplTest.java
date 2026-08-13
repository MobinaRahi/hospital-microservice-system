package hospital.notificationservice.service;

import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateCreateDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateResponseDto;
import hospital.notificationservice.dto.notificationtemplate.NotificationTemplateUpdateDto;
import hospital.notificationservice.exception.notificationtemplate.DuplicateTemplateNameException;
import hospital.notificationservice.exception.notificationtemplate.NotificationTemplateNotFoundException;
import hospital.notificationservice.mapper.NotificationTemplateMapper;
import hospital.notificationservice.model.NotificationTemplate;
import hospital.notificationservice.model.enums.TemplateType;
import hospital.notificationservice.repository.NotificationTemplateRepository;
import hospital.notificationservice.service.impl.NotificationTemplateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationTemplateServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class NotificationTemplateServiceImplTest {

    @Mock private NotificationTemplateRepository notificationTemplateRepository;
    @Mock private NotificationTemplateMapper notificationTemplateMapper;

    @InjectMocks
    private NotificationTemplateServiceImpl notificationTemplateService;

    private NotificationTemplate testTemplate;

    @BeforeEach
    void setUp() {
        testTemplate = NotificationTemplate.builder()
                .id(1L)
                .name("Test Template")
                .type(TemplateType.SMS)
                .content("Hello {{name}}, your appointment is on {{date}}")
                .variables("name,date")
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Create Template")
    class CreateTemplateTests {

        @Test
        @DisplayName("should create template successfully")
        void shouldCreateTemplate() {
            NotificationTemplateCreateDto dto = NotificationTemplateCreateDto.builder()
                    .name("Test Template")
                    .type(TemplateType.SMS)
                    .content("Hello {{name}}")
                    .build();

            when(notificationTemplateRepository.existsByName("Test Template")).thenReturn(false);
            when(notificationTemplateMapper.toEntity(any(NotificationTemplateCreateDto.class))).thenReturn(testTemplate);
            when(notificationTemplateRepository.save(any(NotificationTemplate.class))).thenReturn(testTemplate);
            when(notificationTemplateMapper.toResponseDto(any(NotificationTemplate.class)))
                    .thenReturn(NotificationTemplateResponseDto.builder().id(1L).build());

            NotificationTemplateResponseDto result = notificationTemplateService.createTemplate(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(notificationTemplateRepository).save(any(NotificationTemplate.class));
        }

        @Test
        @DisplayName("should throw when template name already exists")
        void shouldThrowWhenNameExists() {
            NotificationTemplateCreateDto dto = NotificationTemplateCreateDto.builder()
                    .name("Test Template")
                    .type(TemplateType.SMS)
                    .content("Hello {{name}}")
                    .build();

            when(notificationTemplateRepository.existsByName("Test Template")).thenReturn(true);

            assertThatThrownBy(() -> notificationTemplateService.createTemplate(dto))
                    .isInstanceOf(DuplicateTemplateNameException.class);
        }
    }

    @Nested
    @DisplayName("Read Template")
    class ReadTemplateTests {

        @Test
        @DisplayName("should get template by id")
        void shouldGetById() {
            NotificationTemplateResponseDto expected = NotificationTemplateResponseDto.builder().id(1L).build();

            when(notificationTemplateRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTemplate));
            when(notificationTemplateMapper.toResponseDto(testTemplate)).thenReturn(expected);

            NotificationTemplateResponseDto result = notificationTemplateService.getTemplateById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found by id")
        void shouldThrowWhenNotFoundById() {
            when(notificationTemplateRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> notificationTemplateService.getTemplateById(999L))
                    .isInstanceOf(NotificationTemplateNotFoundException.class);
        }

        @Test
        @DisplayName("should get template by name")
        void shouldGetByName() {
            when(notificationTemplateRepository.findByName("Test Template")).thenReturn(Optional.of(testTemplate));
            when(notificationTemplateMapper.toResponseDto(testTemplate))
                    .thenReturn(NotificationTemplateResponseDto.builder().id(1L).name("Test Template").build());

            NotificationTemplateResponseDto result = notificationTemplateService.getTemplateByName("Test Template");

            assertThat(result.getName()).isEqualTo("Test Template");
        }

        @Test
        @DisplayName("should throw when not found by name")
        void shouldThrowWhenNotFoundByName() {
            when(notificationTemplateRepository.findByName("Unknown")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> notificationTemplateService.getTemplateByName("Unknown"))
                    .isInstanceOf(NotificationTemplateNotFoundException.class);
        }

        @Test
        @DisplayName("should get all templates")
        void shouldGetAll() {
            when(notificationTemplateRepository.findAllNotDeleted()).thenReturn(List.of(testTemplate));
            when(notificationTemplateMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(NotificationTemplateResponseDto.builder().id(1L).build()));

            assertThat(notificationTemplateService.getAllTemplates()).hasSize(1);
        }

        @Test
        @DisplayName("should get templates by type")
        void shouldGetByType() {
            when(notificationTemplateRepository.findByType(TemplateType.SMS)).thenReturn(List.of(testTemplate));
            when(notificationTemplateMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(NotificationTemplateResponseDto.builder().id(1L).build()));

            assertThat(notificationTemplateService.getTemplatesByType(TemplateType.SMS)).hasSize(1);
        }

        @Test
        @DisplayName("should get active templates")
        void shouldGetActive() {
            when(notificationTemplateRepository.findByIsActive(true)).thenReturn(List.of(testTemplate));
            when(notificationTemplateMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(NotificationTemplateResponseDto.builder().id(1L).build()));

            assertThat(notificationTemplateService.getActiveTemplates()).hasSize(1);
        }

        @Test
        @DisplayName("should search templates by name")
        void shouldSearchByName() {
            when(notificationTemplateRepository.findByNameContainingIgnoreCase("test"))
                    .thenReturn(List.of(testTemplate));
            when(notificationTemplateMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(NotificationTemplateResponseDto.builder().id(1L).build()));

            assertThat(notificationTemplateService.searchTemplatesByName("test")).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Update Template")
    class UpdateTemplateTests {

        @Test
        @DisplayName("should update template")
        void shouldUpdateTemplate() {
            NotificationTemplateUpdateDto dto = NotificationTemplateUpdateDto.builder()
                    .name("Updated Name")
                    .build();

            when(notificationTemplateRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTemplate));
            when(notificationTemplateRepository.save(any(NotificationTemplate.class))).thenReturn(testTemplate);
            when(notificationTemplateMapper.toResponseDto(any(NotificationTemplate.class)))
                    .thenReturn(NotificationTemplateResponseDto.builder().id(1L).build());

            NotificationTemplateResponseDto result = notificationTemplateService.updateTemplate(1L, dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(notificationTemplateMapper).updateEntity(dto, testTemplate);
        }

        @Test
        @DisplayName("should throw when updating non-existent template")
        void shouldThrowWhenUpdatingNonExistent() {
            when(notificationTemplateRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> notificationTemplateService.updateTemplate(999L, new NotificationTemplateUpdateDto()))
                    .isInstanceOf(NotificationTemplateNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Activate/Deactivate Template")
    class ActivateDeactivateTests {

        @Test
        @DisplayName("should activate template")
        void shouldActivateTemplate() {
            testTemplate.setIsActive(false);
            when(notificationTemplateRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTemplate));
            when(notificationTemplateMapper.toResponseDto(any(NotificationTemplate.class)))
                    .thenReturn(NotificationTemplateResponseDto.builder().id(1L).build());
            when(notificationTemplateRepository.save(any(NotificationTemplate.class))).thenReturn(testTemplate);

            notificationTemplateService.activateTemplate(1L);

            verify(notificationTemplateRepository).save(argThat(t -> Boolean.TRUE.equals(t.getIsActive())));
        }

        @Test
        @DisplayName("should deactivate template")
        void shouldDeactivateTemplate() {
            when(notificationTemplateRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTemplate));
            when(notificationTemplateMapper.toResponseDto(any(NotificationTemplate.class)))
                    .thenReturn(NotificationTemplateResponseDto.builder().id(1L).build());
            when(notificationTemplateRepository.save(any(NotificationTemplate.class))).thenReturn(testTemplate);

            notificationTemplateService.deactivateTemplate(1L);

            verify(notificationTemplateRepository).save(argThat(t -> Boolean.FALSE.equals(t.getIsActive())));
        }

        @Test
        @DisplayName("should throw when activating non-existent template")
        void shouldThrowWhenActivatingNonExistent() {
            when(notificationTemplateRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> notificationTemplateService.activateTemplate(999L))
                    .isInstanceOf(NotificationTemplateNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Template")
    class DeleteTemplateTests {

        @Test
        @DisplayName("should soft delete template")
        void shouldSoftDelete() {
            when(notificationTemplateRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testTemplate));

            notificationTemplateService.deleteTemplate(1L);

            verify(notificationTemplateRepository).save(argThat(NotificationTemplate::isDeleted));
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("should check template name existence")
        void shouldCheckNameExistence() {
            when(notificationTemplateRepository.existsByName("Test Template")).thenReturn(true);

            assertThat(notificationTemplateService.templateNameExists("Test Template")).isTrue();
        }
    }
}
