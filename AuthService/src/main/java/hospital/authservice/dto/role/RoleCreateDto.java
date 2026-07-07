package hospital.authservice.dto.role;

import hospital.authservice.model.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDto {

    @NotNull(message = "Role name is required")
    private RoleName name;

    @Size(max = 200, message = "Description max 200 characters")
    private String description;

    private List<Long> permissionIds = new ArrayList<>();
}