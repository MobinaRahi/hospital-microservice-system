package hospital.authservice.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSlimResponseDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
}
