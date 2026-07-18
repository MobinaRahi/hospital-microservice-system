package hospital.coreservice.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * DTO for permission response data.
 *
 * @author Mobina
 */
public class PermissionResponseDto {

    private Long id;

    private String name;

    private String description;

    private String category;

    private boolean systemDefault;

    private boolean isActive;

    private int roleCount;

    private String action;
}
