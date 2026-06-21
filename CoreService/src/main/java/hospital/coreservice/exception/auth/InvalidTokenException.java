package hospital.coreservice.exception.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a refresh token is invalid, expired, or malformed.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        super("Invalid or expired token");
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(Throwable cause) {
        super("Invalid or expired token", cause);
    }

    // ========== Static factory methods for common use cases ==========

    public static InvalidTokenException expired() {
        return new InvalidTokenException("Token has expired. Please login again.");
    }

    public static InvalidTokenException malformed() {
        return new InvalidTokenException("Token is malformed or invalid.");
    }

    public static InvalidTokenException unsupported() {
        return new InvalidTokenException("Token type is not supported.");
    }

    public static InvalidTokenException missing() {
        return new InvalidTokenException("Token is missing in the request.");
    }

    public static InvalidTokenException invalidSignature() {
        return new InvalidTokenException("Token signature is invalid.");
    }
}