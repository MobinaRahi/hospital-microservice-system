package hospital.authservice.security.config;

import hospital.authservice.security.filter.JwtAuthenticationFilter;
import hospital.authservice.security.handler.CustomAccessDeniedHandler;
import hospital.authservice.security.handler.CustomAuthenticationEntryPoint;
import hospital.authservice.security.handler.CustomAuthenticationSuccessHandler;
import hospital.authservice.security.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpMethod;
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
    private final CustomAuthenticationSuccessHandler successHandler;
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
                .csrf(AbstractHttpConfigurer::disable) // غیرفعال کردن CSRF برای APIها (اختیاری)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 🔑 تغییر ۱: سشن‌ها را برای فرم لاگین فعال می‌کنیم
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        // ===== مسیرهای عمومی (بدون احراز هویت) =====
                        .requestMatchers(
                                "/", "/index", "/home", "/login", "/error",
                                "/css/**", "/js/**", "/images/**", "/static/**", "/webjars/**",
                                "/patient/book",
                                "/api/v1/doctor/active/by-department-id",
                                "/api/v1/appointments/doctor/available",
                                "/api/v1/appointments/patient",
                                "/api/v1/doctor/count/by-department-id",
                                "/patient/dashboard/",
                                "/patient/dashboard",
                                "/patient/patient_dashboard",
                                "/patient_dashboard",
                                "/api/v1/doctor/**",
                                "/patient/complete-profile",
                                "/doctors/**",
                                "/departments/**",
                                "/appointments/book",
                                "/api/v1/doctors/search",
                                "/api/v1/departments",
                                "/api/v1/doctors",
                                "/api/v1/doctor/by-department-id",
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()

                        // ===== APIهای خاص که باید عمومی باشند =====
                        .requestMatchers("/api/v1/patient/complete-registration").permitAll()
                        .requestMatchers("/api/v1/users/register-with-roles").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/patient").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/departments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/room").hasRole("ADMIN")

                        // ===== صفحات نیازمند لاگین =====
                        .requestMatchers(
                                "/dashboard",
                                "/patient/**",
                                "/doctor/**",
                                "/nurse/**",
                                "/admin/**"
                        ).authenticated()

                        // ===== APIهای نقش‌محور =====
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/doctor/**").hasAnyRole("DOCTOR", "ADMIN")
                        .requestMatchers("/api/v1/nurse/**").hasAnyRole("NURSE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/patient/**").hasAnyRole("PATIENT", "ADMIN", "DOCTOR", "NURSE")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/patient/**").hasAnyRole("PATIENT", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/patient/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // 🔑 تغییر ۲: فعال کردن فرم لاگین
                .formLogin(form -> form
                        .loginPage("/login")                   // صفحه لاگین سفارشی
                        .successHandler(successHandler)        // هندلر سفارشی برای هدایت بر اساس نقش
                        .failureUrl("/login?error=true")       // در صورت خطا
                        .permitAll()                           // اجازه دسترسی به این مسیرها بدون لاگین
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")                  // مسیر خروج
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )

                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}