package com.example.kakeibo.controller;

import com.example.kakeibo.api.dto.AuthDtos.*;
import com.example.kakeibo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", // クッキー付きの通信を許可
        allowedHeaders = "*", // すべてのヘッダーを許可
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
                RequestMethod.OPTIONS })

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SimpleResponse> signup(@RequestBody SignupRequest req) {
        authService.signup(req.email(), req.password(), req.name());
        return ResponseEntity.ok(new SimpleResponse("OK"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        // 1. 認証ロジック (省略)
        String token = authService.authenticate(loginRequest);

        // 2. クッキーを作成
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true) // JavaScriptからアクセス不可（XSS対策）
                .secure(false) // 開発環境(http)なのでfalse。本番(https)はtrue
                .path("/") // 全パスで有効
                .maxAge(24 * 60 * 60) // 有効期限（例：1日）
                .sameSite("Lax") // CSRF対策の基本
                .build();

        // 3. レスポンスヘッダーにセット
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().body("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponse> logout() {
        authService.logout();
        return ResponseEntity.ok(new SimpleResponse("OK"));
    }
}
