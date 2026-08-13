package hospital.notificationservice.service.impl;

import hospital.notificationservice.dto.emaillog.EmailLogCreateDto;
import hospital.notificationservice.dto.emaillog.EmailLogResponseDto;
import hospital.notificationservice.exception.emaillog.EmailLogNotFoundException;
import hospital.notificationservice.mapper.EmailLogMapper;
import hospital.notificationservice.model.EmailLog;
import hospital.notificationservice.model.enums.EmailStatus;
import hospital.notificationservice.repository.EmailLogRepository;
import hospital.notificationservice.service.EmailLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link EmailLogService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailLogServiceImpl implements EmailLogService {

    private final EmailLogRepository emailLogRepository;
    private final EmailLogMapper emailLogMapper;

    @Override
    public EmailLogResponseDto createEmail(EmailLogCreateDto dto) {
        log.info("Creating email for: {}", dto.getTo());

        EmailLog email = emailLogMapper.toEntity(dto);
        EmailLog saved = emailLogRepository.save(email);
        log.info("Email created with id: {}", saved.getId());

        return emailLogMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailLogResponseDto getEmailById(Long id) {
        log.debug("Fetching email by id: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        return emailLogMapper.toResponseDto(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getAllEmails() {
        log.debug("Fetching all emails");

        List<EmailLog> emails = emailLogRepository.findAllNotDeleted();
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getEmailsByStatus(EmailStatus status) {
        log.debug("Fetching emails by status: {}", status);

        List<EmailLog> emails = emailLogRepository.findByStatus(status);
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getEmailsByRecipient(String to) {
        log.debug("Fetching emails by recipient: {}", to);

        List<EmailLog> emails = emailLogRepository.findByTo(to);
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getPendingEmails() {
        log.debug("Fetching pending emails");

        List<EmailLog> emails = emailLogRepository.findPendingEmails();
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getFailedEmails() {
        log.debug("Fetching failed emails");

        List<EmailLog> emails = emailLogRepository.findFailedEmails();
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmailLogResponseDto> getUnopenedEmails() {
        log.debug("Fetching unopened emails");

        List<EmailLog> emails = emailLogRepository.findUnopenedEmails();
        return emailLogMapper.toResponseDtoList(emails);
    }

    @Override
    public EmailLogResponseDto markAsSent(Long id) {
        log.info("Marking email as sent: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        email.markSent();
        EmailLog saved = emailLogRepository.save(email);

        return emailLogMapper.toResponseDto(saved);
    }

    @Override
    public EmailLogResponseDto markAsDelivered(Long id) {
        log.info("Marking email as delivered: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        email.markDelivered();
        EmailLog saved = emailLogRepository.save(email);

        return emailLogMapper.toResponseDto(saved);
    }

    @Override
    public EmailLogResponseDto markAsOpened(Long id) {
        log.info("Marking email as opened: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        email.markOpened();
        EmailLog saved = emailLogRepository.save(email);

        return emailLogMapper.toResponseDto(saved);
    }

    @Override
    public EmailLogResponseDto markAsFailed(Long id, String errorMessage) {
        log.info("Marking email as failed: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        email.markFailed(errorMessage);
        EmailLog saved = emailLogRepository.save(email);

        return emailLogMapper.toResponseDto(saved);
    }

    @Override
    public void deleteEmail(Long id) {
        log.info("Soft-deleting email id: {}", id);

        EmailLog email = emailLogRepository.findNotDeletedById(id)
                .orElseThrow(() -> EmailLogNotFoundException.byId(id));

        email.softDelete(null);
        emailLogRepository.save(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long countEmailsByStatus(EmailStatus status) {
        return emailLogRepository.countByStatus(status);
    }
}
