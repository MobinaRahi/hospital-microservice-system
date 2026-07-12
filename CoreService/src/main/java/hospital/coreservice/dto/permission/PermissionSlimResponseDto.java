package hospital.coreservice.dto.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionSlimResponseDto {

    private Long id;

    private String name;

    private String category;

    private String action;
}