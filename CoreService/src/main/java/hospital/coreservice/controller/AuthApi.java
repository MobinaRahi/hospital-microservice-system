package hospital.coreservice.controller;

import hospital.coreservice.dto.request.LoginRequest;
import hospital.coreservice.dto.request.TokenRefreshRequest;
import hospital.coreservice.dto.response.ApiResponse;
import hospital.coreservice.dto.response.LoginResponse;
import hospital.coreservice.dto.response.TokenRefreshResponse;
import hospital.coreservice.exception.auth.InvalidTokenException;
import hospital.coreservice.security.model.SecurityUser;
import hospital.coreservice.security.provider.JwtTokenProvider;
import hospital.coreservice.security.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, refresh token and current user APIs")
public class AuthApi {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Value("${app.jwt.expiration:3600000}")
    private long jwtExpiration;

    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT access/refresh tokens")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        LoginResponse response = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpiration)
                .user(LoginResponse.UserInfo.builder()
                        .id(securityUser.getId())
                        .username(securityUser.getUsername())
                        .email(securityUser.getEmail())
                        .roles(roles)
                        .build())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Login successful", HttpStatus.OK.value()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT access token")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        TokenRefreshResponse response = TokenRefreshResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtExpiration)
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Token refreshed successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    public ResponseEntity<ApiResponse<SecurityUser>> me(@AuthenticationPrincipal SecurityUser currentUser) {
        return ResponseEntity.ok(ApiResponse.success(currentUser, "Current user loaded", HttpStatus.OK.value()));
    }
}