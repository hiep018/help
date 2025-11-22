package com.rescue.help.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // (Chúng ta không cần PasswordEncoder nữa vì không có đăng nhập)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF
            .authorizeHttpRequests(authz -> authz
                // Cho phép tất cả mọi request (/, /get-requests, /submit-request)
                // mà không cần đăng nhập.
                .anyRequest().permitAll() 
            );
        
        // Xóa cấu hình formLogin
        
        return http.build();
    }
}