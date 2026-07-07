package hospital.authservice.dto.role;

import hospital.authservice.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleSlimResponseDto {

    private Long id;

    private RoleName name;

    private String description;
}