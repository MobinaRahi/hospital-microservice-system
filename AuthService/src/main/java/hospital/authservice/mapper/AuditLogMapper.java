package hospital.authservice.mapper;

import hospital.authservice.dto.audit_log.AuditLogResponseDto;
import hospital.authservice.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuditLogMapper {

    @Mapping(target = "userId", source = "user.id")
    AuditLogResponseDto toResponseDto(AuditLog auditLog);
}