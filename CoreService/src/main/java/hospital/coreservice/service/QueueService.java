package hospital.coreservice.service;

import hospital.coreservice.dto.queue.QueueEntryResponseDto;

import java.util.List;

/**
 * Service interface for Queue management.
 *
 * @author MobinaRahi
 */
public interface QueueService {

    QueueEntryResponseDto addToQueue(Long appointmentId);

    List<QueueEntryResponseDto> getTodayActiveQueue();

    List<QueueEntryResponseDto> getDoctorQueue(Long doctorId);

    QueueEntryResponseDto getQueueEntryById(Long id);

    QueueEntryResponseDto callNext(Long doctorId);

    QueueEntryResponseDto callEntry(Long id);

    QueueEntryResponseDto completeEntry(Long id);

    long countWaiting(Long doctorId);
}
