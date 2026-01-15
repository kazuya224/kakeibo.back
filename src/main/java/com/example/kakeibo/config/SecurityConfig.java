package com.example.kakeibo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORSを有効にする（下のcorsConfigurationSourceを使う）
                .cors(Customizer.withDefaults())

                // 2. CSRFを無効にする（ログイン等のPOSTを許可するために必須）
                .csrf(csrf -> csrf.disable())

                // 3. セッション管理（クッキーを使う場合はデフォルトでOK）

                // 4. アクセス制限
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/transaction/**", "/kakeibo/**", "/transaction").permitAll() // /auth/login
                                                                                                                   // や
                        // /auth/signup
                        // を完全に許可
                        .anyRequest().authenticated() // それ以外は認証が必要
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // フロントエンドのURLを正確に指定
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // クッキーの送受信を許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}