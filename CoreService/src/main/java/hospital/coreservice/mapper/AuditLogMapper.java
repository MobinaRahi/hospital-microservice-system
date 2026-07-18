package hospital.coreservice.mapper;

import hospital.coreservice.dto.audit_log.AuditLogResponseDto;
import hospital.coreservice.model.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
/**
 * MapStruct mapper for AuditLog entity ↔ DTO conversion.
 *
 * @author Mobina
 */
public interface AuditLogMapper {

    @Mapping(target = "userId", source = "user.id")
    AuditLogResponseDto toResponseDto(AuditLog auditLog);
}