package hospital.coreservice.dto.role;

import hospital.coreservice.dto.permission.PermissionSlimResponseDto;
import hospital.coreservice.model.enums.RoleName;
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