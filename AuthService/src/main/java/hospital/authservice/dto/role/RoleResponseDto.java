package hospital.authservice.dto.role;

import hospital.authservice.dto.permission.PermissionSlimResponseDto;
import hospital.authservice.model.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleResponseDto {

    private Long id;

    private RoleName name;

    private String description;

    private boolean isActive;

    private List<PermissionSlimResponseDto> permissions = new ArrayList<>();

    private int userCount;

}