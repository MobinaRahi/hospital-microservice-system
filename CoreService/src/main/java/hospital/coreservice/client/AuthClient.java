package hospital.coreservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
/**
 * Client for communicating with AuthService API.
 *
 * @author Mobina
 */
@Component
@RequiredArgsConstructor
public class AuthClient {

    private final RestTemplate restTemplate;

    @Value("${services.auth.base-url:http://localhost:8081}")
    private String authBaseUrl;

    public void validateUserHasRole(Long userId, String requiredRole) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        try {
            String url = authBaseUrl + "/api/v1/internal/users/" + userId + "/has-role?role=" + requiredRole;

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            Object data = response.getBody() != null ? response.getBody().get("data") : null;

            if (!Boolean.TRUE.equals(data)) {
                throw new IllegalArgumentException(
                        "User " + userId + " does not have required role: " + requiredRole
                );
            }

        } catch (RestClientException ex) {
            throw new IllegalArgumentException(
                    "User not found or AuthService unavailable for userId: " + userId,
                    ex
            );
        }
    }

    public Long getUserIdByUsername(String username) {
        try {
            String url = authBaseUrl + "/api/v1/internal/users/by-username?username=" + username;

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            Object data = response.getBody() != null ? response.getBody().get("data") : null;

            if (data instanceof Map<?, ?> map) {
                Object id = map.get("id");

                if (id instanceof Number number) {
                    return number.longValue();
                }

                if (id instanceof String text) {
                    return Long.parseLong(text);
                }
            }

        } catch (Exception ex) {
            log.warn("Could not resolve user id by username '{}' from AuthService: {}", username, ex.getMessage());
        }

        return null;
    }
}