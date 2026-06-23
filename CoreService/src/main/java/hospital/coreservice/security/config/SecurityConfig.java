package hospital.coreservice.security.config;

import hospital.coreservice.security.filter.JwtAuthenticationFilter;
import hospital.coreservice.security.handler.CustomAccessDeniedHandler;
import hospital.coreservice.security.handler.CustomAuthenticationEntryPoint;
import hospital.coreservice.security.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final LogoutHandler logoutHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ==================== PUBLIC (همه می‌تونن) ====================
                        .requestMatchers(
                                // صفحات عمومی
                                "/", "/index", "/home", "/login", "/error", "/favicon.ico",
                                "/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**",
                                "/patient/book",
                                "/api/v1/doctor/active/by-department-id",   // برای گرفتن پزشکان بر اساس بخش
                                "/api/v1/appointments/doctor/available",    // برای گرفتن ساعت‌های خالی
                                "/api/v1/appointments/patient",         // برای ثبت نوبت توسط بیمار
                                // جستجو و مشاهده عمومی
                                "/doctors/**",
                                "/departments/**",
                                "/appointments/book",           // جستجوی نوبت
                                "/api/v1/doctors/search",       // API جستجوی پزشک
                                "/api/v1/departments",          // لیست بخش‌ها
                                "/api/v1/doctors",         // لیست پزشکان
                                "/api/v1/doctor/by-department-id"
                        ).permitAll()

                        // ==================== احراز هویت ====================
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // ==================== Swagger ====================
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ==================== H2 Console ====================
                        .requestMatchers("/h2-console/**").permitAll()

                        // ==================== صفحات نیازمند لاگین ====================
                        .requestMatchers(
                                "/dashboard",
                                "/patient/**",
                                "/doctor/**",
                                "/nurse/**",
                                "/admin/**"
                        ).authenticated()

                        // ==================== APIهای نقش‌محور ====================
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/v1/nurse/**").hasAnyRole("NURSE", "ADMIN")
                        .requestMatchers("/api/v1/patient/**").hasAnyRole("PATIENT", "ADMIN")

                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((req, res, auth) -> {
                            res.setStatus(200);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"success\":true,\"message\":\"Logged out successfully\"}");
                        })
                )

                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
