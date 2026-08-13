package hospital.adminservice.service;

import hospital.adminservice.dto.report.ReportCreateDto;
import hospital.adminservice.dto.report.ReportResponseDto;
import hospital.adminservice.exception.report.InvalidReportStateException;
import hospital.adminservice.exception.report.ReportNotFoundException;
import hospital.adminservice.mapper.ReportMapper;
import hospital.adminservice.model.Report;
import hospital.adminservice.model.enums.ReportStatus;
import hospital.adminservice.model.enums.ReportType;
import hospital.adminservice.repository.ReportRepository;
import hospital.adminservice.service.impl.ReportServiceImpl;
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
 * Unit tests for {@link ReportServiceImpl}.
 *
 * @author MobinaRahi
 */
@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock private ReportRepository reportRepository;
    @Mock private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report testReport;

    @BeforeEach
    void setUp() {
        testReport = Report.builder()
                .id(1L)
                .name("Monthly Revenue Report")
                .type(ReportType.FINANCIAL_MONTHLY)
                .status(ReportStatus.PENDING)
                .build();
    }

    @Nested
    @DisplayName("Create Report")
    class CreateReportTests {

        @Test
        @DisplayName("should create report successfully")
        void shouldCreateReport() {
            ReportCreateDto dto = ReportCreateDto.builder()
                    .name("Monthly Revenue Report")
                    .type(ReportType.FINANCIAL_MONTHLY)
                    .build();

            when(reportMapper.toEntity(any(ReportCreateDto.class))).thenReturn(testReport);
            when(reportMapper.toResponseDto(any(Report.class)))
                    .thenReturn(ReportResponseDto.builder().id(1L).build());
            when(reportRepository.save(any(Report.class))).thenReturn(testReport);

            ReportResponseDto result = reportService.createReport(dto);

            assertThat(result.getId()).isEqualTo(1L);
            verify(reportRepository).save(any(Report.class));
        }
    }

    @Nested
    @DisplayName("Read Report")
    class ReadReportTests {

        @Test
        @DisplayName("should get report by id")
        void shouldGetById() {
            ReportResponseDto expected = ReportResponseDto.builder().id(1L).build();

            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));
            when(reportMapper.toResponseDto(testReport)).thenReturn(expected);

            ReportResponseDto result = reportService.getReportById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(reportRepository.findNotDeletedById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> reportService.getReportById(999L))
                    .isInstanceOf(ReportNotFoundException.class);
        }

        @Test
        @DisplayName("should get reports by type")
        void shouldGetByType() {
            when(reportRepository.findByType(ReportType.FINANCIAL_MONTHLY)).thenReturn(List.of(testReport));
            when(reportMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ReportResponseDto.builder().id(1L).build()));

            assertThat(reportService.getReportsByType(ReportType.FINANCIAL_MONTHLY)).hasSize(1);
        }

        @Test
        @DisplayName("should get reports by status")
        void shouldGetByStatus() {
            when(reportRepository.findByStatus(ReportStatus.PENDING)).thenReturn(List.of(testReport));
            when(reportMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ReportResponseDto.builder().id(1L).build()));

            assertThat(reportService.getReportsByStatus(ReportStatus.PENDING)).hasSize(1);
        }

        @Test
        @DisplayName("should get pending reports")
        void shouldGetPendingReports() {
            when(reportRepository.findByStatusOrderByGeneratedAtAsc(ReportStatus.PENDING))
                    .thenReturn(List.of(testReport));
            when(reportMapper.toResponseDtoList(anyList())).thenReturn(List.of(
                    ReportResponseDto.builder().id(1L).build()));

            assertThat(reportService.getPendingReports()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Status Management")
    class StatusManagementTests {

        @Test
        @DisplayName("should mark report as processing")
        void shouldMarkAsProcessing() {
            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));
            when(reportMapper.toResponseDto(any(Report.class)))
                    .thenReturn(ReportResponseDto.builder().build());
            when(reportRepository.save(any(Report.class))).thenReturn(testReport);

            reportService.markAsProcessing(1L);

            verify(reportRepository).save(argThat(r -> r.getStatus() == ReportStatus.PROCESSING));
        }

        @Test
        @DisplayName("should throw when marking non-pending as processing")
        void shouldThrowWhenMarkingNonPending() {
            testReport.setStatus(ReportStatus.COMPLETED);
            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));

            assertThatThrownBy(() -> reportService.markAsProcessing(1L))
                    .isInstanceOf(InvalidReportStateException.class);
        }

        @Test
        @DisplayName("should mark report as completed")
        void shouldMarkAsCompleted() {
            testReport.setStatus(ReportStatus.PROCESSING);
            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));
            when(reportMapper.toResponseDto(any(Report.class)))
                    .thenReturn(ReportResponseDto.builder().build());
            when(reportRepository.save(any(Report.class))).thenReturn(testReport);

            reportService.markAsCompleted(1L, "/reports/file.pdf");

            verify(reportRepository).save(argThat(r -> r.getStatus() == ReportStatus.COMPLETED));
        }

        @Test
        @DisplayName("should mark report as failed")
        void shouldMarkAsFailed() {
            testReport.setStatus(ReportStatus.PROCESSING);
            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));
            when(reportMapper.toResponseDto(any(Report.class)))
                    .thenReturn(ReportResponseDto.builder().build());
            when(reportRepository.save(any(Report.class))).thenReturn(testReport);

            reportService.markAsFailed(1L);

            verify(reportRepository).save(argThat(r -> r.getStatus() == ReportStatus.FAILED));
        }
    }

    @Nested
    @DisplayName("Delete Report")
    class DeleteReportTests {

        @Test
        @DisplayName("should soft delete report")
        void shouldSoftDelete() {
            when(reportRepository.findNotDeletedById(1L)).thenReturn(Optional.of(testReport));

            reportService.deleteReport(1L);

            verify(reportRepository).save(argThat(Report::isDeleted));
        }
    }

    @Nested
    @DisplayName("Statistics")
    class StatisticsTests {

        @Test
        @DisplayName("should count reports by type and status")
        void shouldCountByTypeAndStatus() {
            when(reportRepository.countByTypeAndStatus(ReportType.FINANCIAL_MONTHLY, ReportStatus.COMPLETED))
                    .thenReturn(5L);

            assertThat(reportService.countByTypeAndStatus(ReportType.FINANCIAL_MONTHLY, ReportStatus.COMPLETED))
                    .isEqualTo(5L);
        }
    }
}
