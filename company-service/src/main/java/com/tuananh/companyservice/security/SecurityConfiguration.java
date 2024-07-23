package com.tuananh.companyservice.security;

import com.tuananh.companyservice.util.constant.PredefinedRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] PUBLIC_ENDPOINTS = {
//            "/auth/login", "/auth/introspect", "/auth/logout", "/auth/refresh",
//            "/users/registration",
//            "/auth/**",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html#/"
    };

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfiguration(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                .permitAll()

                .requestMatchers(HttpMethod.GET, "/company/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/company").hasAuthority(PredefinedRole.ROLE_USER)
                .requestMatchers(HttpMethod.PUT, "/company/*").hasAnyAuthority(PredefinedRole.ROLE_HR, PredefinedRole.ROLE_ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/company/*").hasAuthority(PredefinedRole.ROLE_ADMIN)
                .requestMatchers(HttpMethod.PUT, "/company/changeStatus/*").hasAuthority(PredefinedRole.ROLE_ADMIN)

//                .requestMatchers(HttpMethod.GET, "/user").hasAuthority("ROLE_ADMIN")
//                .requestMatchers(HttpMethod.DELETE, "/user/*").hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(customAuthenticationEntryPoint));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
