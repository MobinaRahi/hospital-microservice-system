package hospital.coreservice.dto.role;

import hospital.coreservice.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Slim DTO for role list views.
 *
 * @author Mobina
 */
public class RoleSlimResponseDto {

    private Long id;

    private RoleName name;

    private String description;
}