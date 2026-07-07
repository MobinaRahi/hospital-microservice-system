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

import java.util.List;
import java.util.Map;

@Slf4j
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
            Map<String, Object> profile = getUserProfile(userId);
            if (requiredRole != null && !requiredRole.isBlank() && !hasRole(profile, requiredRole)) {
                throw new IllegalArgumentException("User " + userId + " does not have required role: " + requiredRole);
            }
        } catch (RestClientException ex) {
            throw new IllegalArgumentException("User not found or AuthService unavailable for userId: " + userId, ex);
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
                if (id instanceof Number number) return number.longValue();
                if (id instanceof String text) return Long.parseLong(text);
            }
        } catch (Exception ex) {
            log.warn("Could not resolve user id by username '{}' from AuthService: {}", username, ex.getMessage());
        }
        return null;
    }

    private Map<String, Object> getUserProfile(Long userId) {
        String url = authBaseUrl + "/api/v1/internal/users/" + userId + "/profile";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        Object data = response.getBody() != null ? response.getBody().get("data") : null;
        if (data instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) map;
            return result;
        }
        throw new IllegalArgumentException("Invalid AuthService response for userId: " + userId);
    }

    private boolean hasRole(Map<String, Object> profile, String requiredRole) {
        Object roles = profile.get("roles");
        if (!(roles instanceof List<?> list)) return false;
        String normalized = requiredRole.startsWith("ROLE_") ? requiredRole.substring(5) : requiredRole;
        for (Object role : list) {
            if (role instanceof Map<?, ?> map) {
                Object name = map.get("name");
                if (normalized.equals(String.valueOf(name)) || ("ROLE_" + normalized).equals(String.valueOf(name))) {
                    return true;
                }
            } else if (normalized.equals(String.valueOf(role)) || ("ROLE_" + normalized).equals(String.valueOf(role))) {
                return true;
            }
        }
        return false;
    }
}
