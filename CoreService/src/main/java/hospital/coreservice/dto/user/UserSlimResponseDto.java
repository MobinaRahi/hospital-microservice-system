package hospital.coreservice.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Slim DTO for user list views.
 *
 * @author Mobina
 */
public class UserSlimResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
}
