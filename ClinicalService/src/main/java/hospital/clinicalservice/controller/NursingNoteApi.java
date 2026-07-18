package hospital.clinicalservice.controller;

import hospital.clinicalservice.dto.nursingnote.NursingNoteCreateDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteResponseDto;
import hospital.clinicalservice.dto.nursingnote.NursingNoteUpdateDto;
import hospital.clinicalservice.dto.response.ApiResponse;
import hospital.clinicalservice.model.enums.NoteType;
import hospital.clinicalservice.model.enums.Shift;
import hospital.clinicalservice.service.NursingNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for NursingNote management.
 * Manages nursing notes and shift reports.
 *
 * @author Mobina
 */
@RestController
@RequestMapping("/api/v1/nursing-notes")
@RequiredArgsConstructor
@Tag(name = "Nursing Note Management", description = "Nursing Note CRUD and management APIs")
public class NursingNoteApi {

    private final NursingNoteService nursingNoteService;

    @PostMapping
    @Operation(summary = "Create a new nursing note")
    public ResponseEntity<ApiResponse<NursingNoteResponseDto>> createNursingNote(@Valid @RequestBody NursingNoteCreateDto createDto) {
        NursingNoteResponseDto created = nursingNoteService.createNursingNote(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Nursing note created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a nursing note by ID")
    public ResponseEntity<ApiResponse<NursingNoteResponseDto>> updateNursingNote(@PathVariable Long id, @Valid @RequestBody NursingNoteUpdateDto updateDto) {
        NursingNoteResponseDto updated = nursingNoteService.updateNursingNote(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Nursing note updated successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a nursing note by ID")
    public ResponseEntity<ApiResponse<Void>> deleteNursingNote(@PathVariable Long id) {
        nursingNoteService.deleteNursingNote(id);
        return ResponseEntity.ok(ApiResponse.success("Nursing note deleted successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get nursing note by ID")
    public ResponseEntity<ApiResponse<NursingNoteResponseDto>> getNursingNoteById(@PathVariable Long id) {
        NursingNoteResponseDto note = nursingNoteService.getNursingNoteById(id);
        return ResponseEntity.ok(ApiResponse.success(note, "Nursing note retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get nursing notes by patient ID")
    public ResponseEntity<ApiResponse<List<NursingNoteResponseDto>>> getNursingNotesByPatientId(@PathVariable Long patientId) {
        List<NursingNoteResponseDto> notes = nursingNoteService.getNursingNotesByPatientId(patientId);
        return ResponseEntity.ok(ApiResponse.success(notes, "Nursing notes retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/nurse/{nurseId}")
    @Operation(summary = "Get nursing notes by nurse ID")
    public ResponseEntity<ApiResponse<List<NursingNoteResponseDto>>> getNursingNotesByNurseId(@PathVariable Long nurseId) {
        List<NursingNoteResponseDto> notes = nursingNoteService.getNursingNotesByNurseId(nurseId);
        return ResponseEntity.ok(ApiResponse.success(notes, "Nursing notes retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/type/{noteType}")
    @Operation(summary = "Get nursing notes by type")
    public ResponseEntity<ApiResponse<List<NursingNoteResponseDto>>> getNursingNotesByNoteType(@PathVariable NoteType noteType) {
        List<NursingNoteResponseDto> notes = nursingNoteService.getNursingNotesByNoteType(noteType);
        return ResponseEntity.ok(ApiResponse.success(notes, "Nursing notes retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/shift/{shift}")
    @Operation(summary = "Get nursing notes by shift")
    public ResponseEntity<ApiResponse<List<NursingNoteResponseDto>>> getNursingNotesByShift(@PathVariable Shift shift) {
        List<NursingNoteResponseDto> notes = nursingNoteService.getNursingNotesByShift(shift);
        return ResponseEntity.ok(ApiResponse.success(notes, "Nursing notes retrieved successfully", HttpStatus.OK.value()));
    }
}
