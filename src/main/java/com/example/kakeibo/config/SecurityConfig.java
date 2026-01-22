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
                // 1. CORSを最初に適用
                .cors(Customizer.withDefaults())

                // 2. 本番環境（API）ではCSRFを無効化（これが403の主な原因です）
                .csrf(csrf -> csrf.disable())

                // 3. アクセス制限の順序を整理
                .authorizeHttpRequests(auth -> auth
                        // ログイン、新規登録、各種データ取得をすべて明示的に許可
                        .requestMatchers("/auth/**", "/transaction/**", "/transaction", "/kakeibo", "/kakeibo/**",
                                "/category", "/category/**")
                        .permitAll()
                        .anyRequest().authenticated())
                // 4. フォームログインなどが干渉しないように設定
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // フロントエンドのURLを正確に指定
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "https://kakeibo-front-rose.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // クッキーの送受信を許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}