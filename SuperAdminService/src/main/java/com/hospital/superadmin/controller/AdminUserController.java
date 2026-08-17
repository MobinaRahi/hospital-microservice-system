package com.hospital.superadmin.controller;

import com.hospital.superadmin.dto.adminuser.AdminUserCreateDto;
import com.hospital.superadmin.dto.adminuser.AdminUserResponseDto;
import com.hospital.superadmin.dto.adminuser.AdminUserUpdateDto;
import com.hospital.superadmin.model.enums.AdminStatus;
import com.hospital.superadmin.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
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
 * REST Controller for AdminUser management.
 *
 * @author MobinaRahi
 */
@RestController
@RequestMapping("/api/v1/super-admin/admins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Users", description = "Super admin user management")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @Operation(summary = "Create a new admin user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin created"),
            @ApiResponse(responseCode = "409", description = "Email or userId already exists"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<AdminUserResponseDto> createAdmin(@Valid @RequestBody AdminUserCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserService.createAdmin(dto));
    }

    @GetMapping
    @Operation(summary = "Get all admins with optional status filter")
    public ResponseEntity<List<AdminUserResponseDto>> getAllAdmins(
            @RequestParam(required = false) AdminStatus status) {

        if (status != null) {
            return ResponseEntity.ok(adminUserService.getAdminsByStatus(status));
        }
        return ResponseEntity.ok(adminUserService.getAllAdmins());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admin by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin found"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.getAdminById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get admin by userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin found"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> getAdminByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.getAdminByUserId(userId));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get admin by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin found"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> getAdminByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminUserService.getAdminByEmail(email));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin updated"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUserUpdateDto dto) {
        return ResponseEntity.ok(adminUserService.updateAdmin(id, dto));
    }

    @PutMapping("/{id}/suspend")
    @Operation(summary = "Suspend an admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin suspended"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> suspendAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.suspendAdmin(id));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate an admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin activated"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> activateAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.activateAdmin(id));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an admin")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Admin deactivated"),
            @ApiResponse(responseCode = "404", description = "Admin not found")
    })
    public ResponseEntity<AdminUserResponseDto> deactivateAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminUserService.deactivateAdmin(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete an admin")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminUserService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Get admin count statistics")
    public ResponseEntity<Long> countAdmins(
            @RequestParam(required = false) AdminStatus status) {
        if (status != null) {
            return ResponseEntity.ok(adminUserService.countByStatus(status));
        }
        return ResponseEntity.ok((long) adminUserService.getAllAdmins().size());
    }

    @GetMapping("/check-email")
    @Operation(summary = "Check if an email is available")
    public ResponseEntity<Boolean> checkEmail(
            @RequestParam String email) {
        return ResponseEntity.ok(!adminUserService.emailExists(email));
    }

    @GetMapping("/check-user")
    @Operation(summary = "Check if a userId is available")
    public ResponseEntity<Boolean> checkUser(
            @RequestParam Long userId) {
        return ResponseEntity.ok(!adminUserService.userIdExists(userId));
    }
}
