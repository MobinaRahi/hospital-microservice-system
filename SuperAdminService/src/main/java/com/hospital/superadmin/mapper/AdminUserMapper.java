package com.hospital.superadmin.mapper;

import com.hospital.superadmin.dto.adminuser.AdminUserCreateDto;
import com.hospital.superadmin.dto.adminuser.AdminUserResponseDto;
import com.hospital.superadmin.dto.adminuser.AdminUserUpdateDto;
import com.hospital.superadmin.model.AdminUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for AdminUser entity and DTOs.
 *
 * @author MobinaRahi
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    AdminUser toEntity(AdminUserCreateDto dto);

    AdminUserResponseDto toResponseDto(AdminUser entity);

    List<AdminUserResponseDto> toResponseDtoList(List<AdminUser> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(AdminUserUpdateDto dto, @MappingTarget AdminUser entity);
}
