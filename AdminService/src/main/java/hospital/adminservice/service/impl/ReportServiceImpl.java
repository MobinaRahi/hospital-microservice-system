package hospital.adminservice.service.impl;

import hospital.adminservice.dto.report.ReportCreateDto;
import hospital.adminservice.dto.report.ReportResponseDto;
import hospital.adminservice.exception.report.InvalidReportStateException;
import hospital.adminservice.exception.report.ReportNotFoundException;
import hospital.adminservice.mapper.ReportMapper;
import hospital.adminservice.model.Report;
import hospital.adminservice.model.enums.ReportStatus;
import hospital.adminservice.model.enums.ReportType;
import hospital.adminservice.repository.ReportRepository;
import hospital.adminservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    @Override
    public ReportResponseDto createReport(ReportCreateDto dto) {
        log.info("Creating report: {} of type {}", dto.getName(), dto.getType());

        Report report = reportMapper.toEntity(dto);
        report.setGeneratedAt(LocalDateTime.now());
        Report saved = reportRepository.save(report);
        log.info("Report created with id: {}", saved.getId());

        return reportMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponseDto getReportById(Long id) {
        log.debug("Fetching report by id: {}", id);

        Report report = reportRepository.findNotDeletedById(id)
                .orElseThrow(() -> ReportNotFoundException.byId(id));

        return reportMapper.toResponseDto(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByType(ReportType type) {
        log.debug("Fetching reports by type: {}", type);

        List<Report> reports = reportRepository.findByType(type);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByStatus(ReportStatus status) {
        log.debug("Fetching reports by status: {}", status);

        List<Report> reports = reportRepository.findByStatus(status);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByUser(Long generatedBy) {
        log.debug("Fetching reports by user: {}", generatedBy);

        List<Report> reports = reportRepository.findByGeneratedBy(generatedBy);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching reports from {} to {}", startDate, endDate);

        List<Report> reports = reportRepository.findByGeneratedAtBetween(startDate, endDate);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getPendingReports() {
        log.debug("Fetching pending reports");

        List<Report> reports = reportRepository.findByStatusOrderByGeneratedAtAsc(ReportStatus.PENDING);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponseDto> getExpiredReports(LocalDateTime beforeDate) {
        log.debug("Fetching expired reports before {}", beforeDate);

        List<Report> reports = reportRepository.findExpiredReportsBefore(beforeDate);
        return reportMapper.toResponseDtoList(reports);
    }

    @Override
    public ReportResponseDto markAsProcessing(Long id) {
        log.info("Marking report {} as processing", id);

        Report report = reportRepository.findNotDeletedById(id)
                .orElseThrow(() -> ReportNotFoundException.byId(id));

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new InvalidReportStateException(id, report.getStatus().name(), "marked as processing");
        }

        report.markAsProcessing();
        Report saved = reportRepository.save(report);
        log.info("Report {} marked as processing", id);

        return reportMapper.toResponseDto(saved);
    }

    @Override
    public ReportResponseDto markAsCompleted(Long id, String fileUrl) {
        log.info("Marking report {} as completed", id);

        Report report = reportRepository.findNotDeletedById(id)
                .orElseThrow(() -> ReportNotFoundException.byId(id));

        if (!report.isProcessing()) {
            throw new InvalidReportStateException(id, report.getStatus().name(), "marked as completed");
        }

        report.markAsCompleted(fileUrl);
        Report saved = reportRepository.save(report);
        log.info("Report {} marked as completed with file: {}", id, fileUrl);

        return reportMapper.toResponseDto(saved);
    }

    @Override
    public ReportResponseDto markAsFailed(Long id) {
        log.info("Marking report {} as failed", id);

        Report report = reportRepository.findNotDeletedById(id)
                .orElseThrow(() -> ReportNotFoundException.byId(id));

        if (!report.isProcessing()) {
            throw new InvalidReportStateException(id, report.getStatus().name(), "marked as failed");
        }

        report.markAsFailed();
        Report saved = reportRepository.save(report);
        log.info("Report {} marked as failed", id);

        return reportMapper.toResponseDto(saved);
    }

    @Override
    public void deleteReport(Long id) {
        log.info("Soft-deleting report id: {}", id);

        Report report = reportRepository.findNotDeletedById(id)
                .orElseThrow(() -> ReportNotFoundException.byId(id));

        report.softDelete(null);
        reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTypeAndStatus(ReportType type, ReportStatus status) {
        return reportRepository.countByTypeAndStatus(type, status);
    }
}
