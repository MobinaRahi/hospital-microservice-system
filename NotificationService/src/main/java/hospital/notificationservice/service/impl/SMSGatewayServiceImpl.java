package hospital.notificationservice.service.impl;

import hospital.notificationservice.dto.smsgateway.SMSGatewayCreateDto;
import hospital.notificationservice.dto.smsgateway.SMSGatewayResponseDto;
import hospital.notificationservice.exception.smsgateway.SMSGatewayNotFoundException;
import hospital.notificationservice.exception.smsgateway.SmsCancelException;
import hospital.notificationservice.mapper.SMSGatewayMapper;
import hospital.notificationservice.model.SMSGateway;
import hospital.notificationservice.model.enums.SmsStatus;
import hospital.notificationservice.repository.SMSGatewayRepository;
import hospital.notificationservice.service.SMSGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link SMSGatewayService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SMSGatewayServiceImpl implements SMSGatewayService {

    private final SMSGatewayRepository smsGatewayRepository;
    private final SMSGatewayMapper smsGatewayMapper;

    @Override
    public SMSGatewayResponseDto createSms(SMSGatewayCreateDto dto) {
        log.info("Creating SMS for: {}", dto.getTo());

        SMSGateway sms = smsGatewayMapper.toEntity(dto);
        SMSGateway saved = smsGatewayRepository.save(sms);
        log.info("SMS created with id: {}", saved.getId());

        return smsGatewayMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SMSGatewayResponseDto getSmsById(Long id) {
        log.debug("Fetching SMS by id: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        return smsGatewayMapper.toResponseDto(sms);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMSGatewayResponseDto> getAllSms() {
        log.debug("Fetching all SMS");

        List<SMSGateway> smsList = smsGatewayRepository.findAllNotDeleted();
        return smsGatewayMapper.toResponseDtoList(smsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMSGatewayResponseDto> getSmsByStatus(SmsStatus status) {
        log.debug("Fetching SMS by status: {}", status);

        List<SMSGateway> smsList = smsGatewayRepository.findByStatus(status);
        return smsGatewayMapper.toResponseDtoList(smsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMSGatewayResponseDto> getSmsByRecipient(String to) {
        log.debug("Fetching SMS by recipient: {}", to);

        List<SMSGateway> smsList = smsGatewayRepository.findByTo(to);
        return smsGatewayMapper.toResponseDtoList(smsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMSGatewayResponseDto> getPendingSms() {
        log.debug("Fetching pending SMS");

        List<SMSGateway> smsList = smsGatewayRepository.findPendingMessages();
        return smsGatewayMapper.toResponseDtoList(smsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMSGatewayResponseDto> getFailedSms() {
        log.debug("Fetching failed SMS");

        List<SMSGateway> smsList = smsGatewayRepository.findByStatusAndDeletedFalse(SmsStatus.FAILED);
        return smsGatewayMapper.toResponseDtoList(smsList);
    }

    @Override
    public SMSGatewayResponseDto markAsSent(Long id, String providerMessageId) {
        log.info("Marking SMS as sent: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        sms.markSent(providerMessageId);
        SMSGateway saved = smsGatewayRepository.save(sms);

        return smsGatewayMapper.toResponseDto(saved);
    }

    @Override
    public SMSGatewayResponseDto markAsDelivered(Long id) {
        log.info("Marking SMS as delivered: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        sms.markDelivered();
        SMSGateway saved = smsGatewayRepository.save(sms);

        return smsGatewayMapper.toResponseDto(saved);
    }

    @Override
    public SMSGatewayResponseDto markAsFailed(Long id, String errorMessage) {
        log.info("Marking SMS as failed: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        sms.markFailed(errorMessage);
        SMSGateway saved = smsGatewayRepository.save(sms);

        return smsGatewayMapper.toResponseDto(saved);
    }

    @Override
    public SMSGatewayResponseDto cancelSms(Long id) {
        log.info("Cancelling SMS: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        try {
            sms.cancel();
        } catch (IllegalStateException ex) {
            throw SmsCancelException.cannotCancel(sms.getStatus().name());
        }

        SMSGateway saved = smsGatewayRepository.save(sms);

        return smsGatewayMapper.toResponseDto(saved);
    }

    @Override
    public void deleteSms(Long id) {
        log.info("Soft-deleting SMS id: {}", id);

        SMSGateway sms = smsGatewayRepository.findNotDeletedById(id)
                .orElseThrow(() -> SMSGatewayNotFoundException.byId(id));

        sms.softDelete(null);
        smsGatewayRepository.save(sms);
    }

    @Override
    @Transactional(readOnly = true)
    public long countSmsByStatus(SmsStatus status) {
        return smsGatewayRepository.countByStatus(status);
    }
}
