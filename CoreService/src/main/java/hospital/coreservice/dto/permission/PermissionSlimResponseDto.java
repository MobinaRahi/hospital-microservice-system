package hospital.coreservice.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Slim DTO for permission list views.
 *
 * @author Mobina
 */
public class PermissionSlimResponseDto {

    private Long id;

    private String name;

    private String category;

    private String action;
}