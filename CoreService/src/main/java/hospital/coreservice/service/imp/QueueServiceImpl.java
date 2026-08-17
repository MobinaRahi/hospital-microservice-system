package hospital.coreservice.service.imp;

import hospital.coreservice.dto.queue.QueueEntryResponseDto;
import hospital.coreservice.mapper.QueueEntryMapper;
import hospital.coreservice.model.Appointment;
import hospital.coreservice.model.QueueEntry;
import hospital.coreservice.repository.AppointmentRepository;
import hospital.coreservice.repository.QueueEntryRepository;
import hospital.coreservice.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementation of {@link QueueService}.
 *
 * @author MobinaRahi
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QueueServiceImpl implements QueueService {

    private final QueueEntryRepository queueEntryRepository;
    private final AppointmentRepository appointmentRepository;
    private final QueueEntryMapper queueEntryMapper;

    @Override
    public QueueEntryResponseDto addToQueue(Long appointmentId) {
        log.info("Adding appointment {} to queue", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment with id " + appointmentId + " not found"));

        throw new IllegalStateException("Can only add CHECKED_IN appointments to queue. Current status: " + appointment.getStatus());

    }

    @Override
    @Transactional(readOnly = true)
    public List<QueueEntryResponseDto> getTodayActiveQueue() {
        log.debug("Fetching today's active queue");

        List<QueueEntry> entries = queueEntryRepository.findTodayActiveQueue(LocalDate.now());
        return queueEntryMapper.toResponseDtoList(entries);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QueueEntryResponseDto> getDoctorQueue(Long doctorId) {
        log.debug("Fetching queue for doctor: {}", doctorId);

        List<QueueEntry> entries = queueEntryRepository.findDoctorQueue(doctorId, LocalDate.now());
        return queueEntryMapper.toResponseDtoList(entries);
    }

    @Override
    @Transactional(readOnly = true)
    public QueueEntryResponseDto getQueueEntryById(Long id) {
        log.debug("Fetching queue entry by id: {}", id);

        QueueEntry entry = queueEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Queue entry with id " + id + " not found"));

        return queueEntryMapper.toResponseDto(entry);
    }

    @Override
    public QueueEntryResponseDto callNext(Long doctorId) {
        log.info("Calling next patient for doctor: {}", doctorId);

        List<QueueEntry> queue = queueEntryRepository.findDoctorQueue(doctorId, LocalDate.now());

        if (queue.isEmpty()) {
            throw new IllegalStateException("No patients waiting in queue for doctor " + doctorId);
        }

        QueueEntry next = queue.get(0);
        next.markAsCalled();
        QueueEntry saved = queueEntryRepository.save(next);

        return queueEntryMapper.toResponseDto(saved);
    }

    @Override
    public QueueEntryResponseDto callEntry(Long id) {
        log.info("Calling queue entry id: {}", id);

        QueueEntry entry = queueEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Queue entry with id " + id + " not found"));

        entry.markAsCalled();
        QueueEntry saved = queueEntryRepository.save(entry);

        return queueEntryMapper.toResponseDto(saved);
    }

    @Override
    public QueueEntryResponseDto completeEntry(Long id) {
        log.info("Completing queue entry id: {}", id);

        QueueEntry entry = queueEntryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Queue entry with id " + id + " not found"));

        entry.markAsCompleted();
        QueueEntry saved = queueEntryRepository.save(entry);

        return queueEntryMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public long countWaiting(Long doctorId) {
        return queueEntryRepository.countByDoctorIdAndQueueDateAndStatus(doctorId, LocalDate.now(), "CHECKED_IN");
    }
}
