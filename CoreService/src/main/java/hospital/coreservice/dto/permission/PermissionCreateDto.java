package hospital.coreservice.dto.permission;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionCreateDto {

    @NotNull(message = "Permission name is required")
    private String name;

    @Size(max = 200, message = "Description max 200 characters")
    private String description;

    @NotNull(message = "resource is required")
    private String resource;

    @NotNull(message = "Action is required")
    private String action;
}