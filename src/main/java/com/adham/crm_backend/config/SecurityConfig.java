package com.adham.crm_backend.config;
import com.adham.crm_backend.security.JwtAccessDeniedHandler;
import com.adham.crm_backend.security.JwtAuthenticationEntryPoint;
import com.adham.crm_backend.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

         http
                 .csrf(AbstractHttpConfigurer::disable)

                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                 .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                         .accessDeniedHandler(jwtAccessDeniedHandler))

                 .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/api/v1/auth/login","/api/v1/auth/refresh","/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html")
                        .permitAll().anyRequest().authenticated())
                 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
