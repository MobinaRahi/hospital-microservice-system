package hospital.coreservice.dto.permission;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionCreateDto {

    @NotNull(message = "Permission name is required")
    private String name;

    @Size(max = 200, message = "Description max 200 characters")
    private String description;

    @NotNull(message = "Category is required")
    private String category;
}