package hospital.coreservice.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class ApiResponse {
    private boolean success;
    private int status;
    private String message;
    private Object data;
}
