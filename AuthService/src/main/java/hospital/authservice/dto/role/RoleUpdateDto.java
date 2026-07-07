package hospital.authservice.dto.role;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleUpdateDto {

    @Size(max = 200, message = "Description max 200 characters")
    private String description;

    private List<Long> permissionIds;
}