package hospital.coreservice.controller;

import hospital.coreservice.dto.queue.QueueEntryResponseDto;
import hospital.coreservice.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Queue management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/core/queue")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Queue", description = "Waiting queue management for today's appointments")
public class QueueApi {

    private final QueueService queueService;

    @PostMapping("/{appointmentId}")
    @Operation(summary = "Add an appointment to the waiting queue")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Added to queue"),
            @ApiResponse(responseCode = "400", description = "Appointment not checked in"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    public ResponseEntity<QueueEntryResponseDto> addToQueue(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(queueService.addToQueue(appointmentId));
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's active queue (all doctors)")
    public ResponseEntity<List<QueueEntryResponseDto>> getTodayActiveQueue() {
        return ResponseEntity.ok(queueService.getTodayActiveQueue());
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get queue for a specific doctor (today)")
    public ResponseEntity<List<QueueEntryResponseDto>> getDoctorQueue(@PathVariable Long doctorId) {
        return ResponseEntity.ok(queueService.getDoctorQueue(doctorId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get queue entry by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Queue entry found"),
            @ApiResponse(responseCode = "404", description = "Queue entry not found")
    })
    public ResponseEntity<QueueEntryResponseDto> getQueueEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.getQueueEntryById(id));
    }

    @PostMapping("/doctor/{doctorId}/call-next")
    @Operation(summary = "Call the next patient in queue for a doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Next patient called"),
            @ApiResponse(responseCode = "400", description = "Queue is empty")
    })
    public ResponseEntity<QueueEntryResponseDto> callNext(@PathVariable Long doctorId) {
        return ResponseEntity.ok(queueService.callNext(doctorId));
    }

    @PutMapping("/{id}/call")
    @Operation(summary = "Call a specific queue entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient called"),
            @ApiResponse(responseCode = "404", description = "Queue entry not found")
    })
    public ResponseEntity<QueueEntryResponseDto> callEntry(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.callEntry(id));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Mark a queue entry as completed")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entry completed"),
            @ApiResponse(responseCode = "404", description = "Queue entry not found")
    })
    public ResponseEntity<QueueEntryResponseDto> completeEntry(@PathVariable Long id) {
        return ResponseEntity.ok(queueService.completeEntry(id));
    }

    @GetMapping("/doctor/{doctorId}/waiting-count")
    @Operation(summary = "Count patients waiting for a doctor")
    public ResponseEntity<Long> countWaiting(@PathVariable Long doctorId) {
        return ResponseEntity.ok(queueService.countWaiting(doctorId));
    }
}
