package hospital.billingservice.controller;

import hospital.billingservice.dto.response.ApiResponse;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogCreateDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogResponseDto;
import hospital.billingservice.dto.servicecatalog.ServiceCatalogUpdateDto;
import hospital.billingservice.model.enums.ServiceCategory;
import hospital.billingservice.service.ServiceCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for ServiceCatalog.
 *
 * <p><strong>Access Control:</strong></p>
 * <ul>
 *   <li>Admin: Full CRUD access</li>
 *   <li>Accountant/Doctor: Read access</li>
 * </ul>
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/billing/service-catalog")
@RequiredArgsConstructor
@Tag(name = "Service Catalog Management", description = "Service catalog CRUD and search APIs")
public class ServiceCatalogApi {

    private final ServiceCatalogService serviceCatalogService;

    @PostMapping
    @Operation(summary = "Create a new service catalog entry")
    public ResponseEntity<ApiResponse<ServiceCatalogResponseDto>> createService(@Valid @RequestBody ServiceCatalogCreateDto createDto) {
        ServiceCatalogResponseDto created = serviceCatalogService.createService(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Service created successfully", HttpStatus.CREATED.value()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a service by ID")
    public ResponseEntity<ApiResponse<ServiceCatalogResponseDto>> updateService(@PathVariable Long id, @Valid @RequestBody ServiceCatalogUpdateDto updateDto) {
        ServiceCatalogResponseDto updated = serviceCatalogService.updateService(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Service updated successfully", HttpStatus.OK.value()));
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle service active status")
    public ResponseEntity<ApiResponse<ServiceCatalogResponseDto>> toggleActive(@PathVariable Long id) {
        ServiceCatalogResponseDto toggled = serviceCatalogService.toggleActive(id);
        return ResponseEntity.ok(ApiResponse.success(toggled, "Service status toggled successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get service by ID")
    public ResponseEntity<ApiResponse<ServiceCatalogResponseDto>> getServiceById(@PathVariable Long id) {
        ServiceCatalogResponseDto service = serviceCatalogService.getServiceById(id);
        return ResponseEntity.ok(ApiResponse.success(service, "Service retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get service by code")
    public ResponseEntity<ApiResponse<ServiceCatalogResponseDto>> getServiceByCode(@PathVariable String code) {
        ServiceCatalogResponseDto service = serviceCatalogService.getServiceByCode(code);
        return ResponseEntity.ok(ApiResponse.success(service, "Service retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active services")
    public ResponseEntity<ApiResponse<List<ServiceCatalogResponseDto>>> getAllActiveServices() {
        List<ServiceCatalogResponseDto> services = serviceCatalogService.getAllActiveServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Active services retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping
    @Operation(summary = "Get all services")
    public ResponseEntity<ApiResponse<List<ServiceCatalogResponseDto>>> getAllServices() {
        List<ServiceCatalogResponseDto> services = serviceCatalogService.getAllServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Services retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get services by category")
    public ResponseEntity<ApiResponse<List<ServiceCatalogResponseDto>>> getServicesByCategory(@PathVariable ServiceCategory category) {
        List<ServiceCatalogResponseDto> services = serviceCatalogService.getServicesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(services, "Services retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/category/{category}/active")
    @Operation(summary = "Get active services by category")
    public ResponseEntity<ApiResponse<List<ServiceCatalogResponseDto>>> getActiveServicesByCategory(@PathVariable ServiceCategory category) {
        List<ServiceCatalogResponseDto> services = serviceCatalogService.getActiveServicesByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(services, "Active services retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search services by name")
    public ResponseEntity<ApiResponse<List<ServiceCatalogResponseDto>>> searchServices(@RequestParam String name) {
        List<ServiceCatalogResponseDto> services = serviceCatalogService.searchByName(name);
        return ResponseEntity.ok(ApiResponse.success(services, "Services retrieved successfully", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a service")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable Long id) {
        serviceCatalogService.deleteService(id);
        return ResponseEntity.ok(ApiResponse.success("Service deleted successfully", HttpStatus.OK.value()));
    }
}
