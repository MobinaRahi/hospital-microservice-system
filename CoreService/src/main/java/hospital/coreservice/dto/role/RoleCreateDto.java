package hospital.coreservice.dto.role;

import hospital.coreservice.model.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleCreateDto {

    @NotNull(message = "Role name is required")
    private RoleName name;

    @Size(max = 200, message = "Description max 200 characters")
    private String description;

    private List<Long> permissionIds = new ArrayList<>();
}