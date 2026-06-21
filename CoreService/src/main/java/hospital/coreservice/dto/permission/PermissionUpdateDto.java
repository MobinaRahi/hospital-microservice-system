package hospital.coreservice.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionUpdateDto {

    String name;

    String description;

    String category;
}
