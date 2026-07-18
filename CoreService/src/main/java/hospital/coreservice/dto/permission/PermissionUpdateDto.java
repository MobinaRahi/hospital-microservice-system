package hospital.coreservice.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for updating a permission.
 *
 * @author Mobina
 */
public class PermissionUpdateDto {

    private String name;

    private String description;

    private String category;

    private String action;
}
