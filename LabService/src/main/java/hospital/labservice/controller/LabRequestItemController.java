package hospital.labservice.controller;

import hospital.labservice.dto.labrequestitem.LabRequestItemCreateDto;
import hospital.labservice.dto.labrequestitem.LabRequestItemResponseDto;
import hospital.labservice.model.enums.RequestItemStatus;
import hospital.labservice.service.LabRequestItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for LabRequestItem management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/lab/request-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lab Request Items", description = "Individual test items within lab requests")
public class LabRequestItemController {

    private final LabRequestItemService labRequestItemService;

    @PostMapping("/request/{requestId}")
    @Operation(summary = "Add a test item to a lab request")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item created"),
            @ApiResponse(responseCode = "404", description = "Request or test not found")
    })
    public ResponseEntity<LabRequestItemResponseDto> createItem(
            @PathVariable Long requestId,
            @Valid @RequestBody LabRequestItemCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labRequestItemService.createItem(requestId, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a lab request item by ID")
    public ResponseEntity<LabRequestItemResponseDto> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestItemService.getItemById(id));
    }

    @GetMapping("/request/{requestId}")
    @Operation(summary = "Get all items for a lab request")
    public ResponseEntity<List<LabRequestItemResponseDto>> getItemsByRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(labRequestItemService.getItemsByRequest(requestId));
    }

    @GetMapping
    @Operation(summary = "Get items by processing status")
    public ResponseEntity<List<LabRequestItemResponseDto>> getItemsByStatus(
            @Parameter(description = "Item status filter") @RequestParam RequestItemStatus status) {
        return ResponseEntity.ok(labRequestItemService.getItemsByStatus(status));
    }

    @PutMapping("/{id}/process")
    @Operation(summary = "Start processing a test item")
    public ResponseEntity<LabRequestItemResponseDto> startProcessing(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestItemService.startProcessing(id));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete a test item")
    public ResponseEntity<LabRequestItemResponseDto> completeItem(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestItemService.completeItem(id));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a test item")
    public ResponseEntity<LabRequestItemResponseDto> cancelItem(@PathVariable Long id) {
        return ResponseEntity.ok(labRequestItemService.cancelItem(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a test item")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        labRequestItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
