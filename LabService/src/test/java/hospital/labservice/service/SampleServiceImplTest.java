package hospital.labservice.service;

import hospital.labservice.dto.sample.SampleCreateDto;
import hospital.labservice.dto.sample.SampleResponseDto;
import hospital.labservice.exception.labrequest.LabRequestNotFoundException;
import hospital.labservice.exception.sample.DuplicateSampleNumberException;
import hospital.labservice.exception.sample.SampleNotFoundException;
import hospital.labservice.mapper.SampleMapper;
import hospital.labservice.model.LabRequest;
import hospital.labservice.model.Sample;
import hospital.labservice.model.enums.SampleQuality;
import hospital.labservice.model.enums.SampleType;
import hospital.labservice.repository.LabRequestRepository;
import hospital.labservice.repository.SampleRepository;
import hospital.labservice.service.impl.SampleServiceImpl;
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
 * Unit tests for {@link SampleServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class SampleServiceImplTest {

    @Mock private SampleRepository sampleRepository;
    @Mock private LabRequestRepository labRequestRepository;
    @Mock private SampleMapper sampleMapper;

    @InjectMocks
    private SampleServiceImpl sampleService;

    private Sample testSample;
    private LabRequest testRequest;

    @BeforeEach
    void setUp() {
        testRequest = LabRequest.builder().id(1L).requestNumber("LAB-001").build();
        testSample = Sample.builder()
                .id(1L)
                .sampleNumber("SMP-001")
                .sampleType(SampleType.BLOOD)
                .collectionDate(LocalDateTime.now())
                .quality(SampleQuality.GOOD)
                .labRequest(testRequest)
                .build();
    }

    @Nested
    @DisplayName("Create Sample")
    class CreateSampleTests {

        @Test
        @DisplayName("should create sample successfully")
        void shouldCreateSample() {
            SampleCreateDto dto = SampleCreateDto.builder()
                    .labRequestId(1L)
                    .sampleNumber("SMP-001")
                    .sampleType(SampleType.BLOOD)
                    .collectionDate(LocalDateTime.now())
                    .build();

            when(sampleRepository.existsBySampleNumber("SMP-001")).thenReturn(false);
            when(sampleMapper.toEntity(any())).thenReturn(testSample);
            when(labRequestRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testRequest));
            when(sampleRepository.save(any())).thenReturn(testSample);
            when(sampleMapper.toResponseDto(any()))
                    .thenReturn(SampleResponseDto.builder().id(1L).build());

            var result = sampleService.createSample(dto);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when sample number exists")
        void shouldThrowWhenNumberExists() {
            SampleCreateDto dto = SampleCreateDto.builder().sampleNumber("SMP-001").build();

            when(sampleRepository.existsBySampleNumber("SMP-001")).thenReturn(true);

            assertThatThrownBy(() -> sampleService.createSample(dto))
                    .isInstanceOf(DuplicateSampleNumberException.class);
        }

        @Test
        @DisplayName("should throw when lab request not found")
        void shouldThrowWhenRequestNotFound() {
            SampleCreateDto dto = SampleCreateDto.builder()
                    .labRequestId(999L)
                    .sampleNumber("SMP-002")
                    .build();

            when(sampleRepository.existsBySampleNumber("SMP-002")).thenReturn(false);
            when(sampleMapper.toEntity(any())).thenReturn(Sample.builder().build());
            when(labRequestRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sampleService.createSample(dto))
                    .isInstanceOf(LabRequestNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Receive Sample")
    class ReceiveSampleTests {

        @Test
        @DisplayName("should receive sample at lab")
        void shouldReceiveSample() {
            when(sampleRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSample));
            when(sampleRepository.save(any())).thenReturn(testSample);
            when(sampleMapper.toResponseDto(any()))
                    .thenReturn(SampleResponseDto.builder().id(1L).build());

            sampleService.receiveSample(1L, 50L);

            verify(sampleRepository).save(argThat(s -> s.getReceivedBy() == 50L && s.getReceivedAtLab() != null));
        }

        @Test
        @DisplayName("should throw when sample not found for receive")
        void shouldThrowWhenNotFound() {
            when(sampleRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sampleService.receiveSample(999L, 50L))
                    .isInstanceOf(SampleNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Quality Update")
    class QualityTests {

        @Test
        @DisplayName("should update sample quality")
        void shouldUpdateQuality() {
            when(sampleRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSample));
            when(sampleRepository.save(any())).thenReturn(testSample);
            when(sampleMapper.toResponseDto(any()))
                    .thenReturn(SampleResponseDto.builder().id(1L).build());

            sampleService.updateSampleQuality(1L, SampleQuality.HEMOLYZED);

            verify(sampleRepository).save(argThat(s -> s.getQuality() == SampleQuality.HEMOLYZED));
        }
    }

    @Nested
    @DisplayName("Read Samples")
    class ReadSampleTests {

        @Test
        @DisplayName("should get unreceived samples")
        void shouldGetUnreceived() {
            when(sampleRepository.findUnreceivedSamples()).thenReturn(List.of(testSample));
            when(sampleMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SampleResponseDto.builder().id(1L).build()));

            assertThat(sampleService.getUnreceivedSamples()).hasSize(1);
        }

        @Test
        @DisplayName("should get samples with quality issues")
        void shouldGetQualityIssues() {
            when(sampleRepository.findSamplesWithQualityIssues()).thenReturn(List.of(testSample));
            when(sampleMapper.toResponseDtoList(anyList()))
                    .thenReturn(List.of(SampleResponseDto.builder().id(1L).build()));

            assertThat(sampleService.getSamplesWithQualityIssues()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Delete Sample")
    class DeleteSampleTests {

        @Test
        @DisplayName("should soft delete sample")
        void shouldSoftDelete() {
            when(sampleRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testSample));

            sampleService.deleteSample(1L);

            verify(sampleRepository).save(argThat(Sample::isDeleted));
        }
    }
}
