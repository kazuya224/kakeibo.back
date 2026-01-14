package com.example.kakeibo.service;

import com.example.kakeibo.domain.User;
import com.example.kakeibo.repository.UserRepository;
import com.example.kakeibo.api.dto.AuthDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UUID signup(String email, String rawPassword, String name) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("EMAIL_ALREADY_USED");
        }

        User u = new User();
        u.setUserId(UUID.randomUUID()); // DB側 default gen_random_uuid を使うならnullでもOKだが、まずは確実に入れる
        u.setEmail(email);
        u.setName(name);
        u.setPasswordHash(encoder.encode(rawPassword));

        userRepository.save(u);
        return u.getUserId();
    }

    public UUID login(String email, String rawPassword) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

        if (!encoder.matches(rawPassword, u.getPasswordHash())) {
            System.out.println("パスワード" + u.getPasswordHash());
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }
        return u.getUserId();
    }

    public String authenticate(AuthDtos.LoginRequest loginRequest) {
        // 1. メールアドレスでユーザーを探す
        User u = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

        // 2. パスワードが一致するかチェック
        if (!encoder.matches(loginRequest.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("INVALID_CREDENTIALS");
        }

        // 3. 認証成功！本来はここでJWT（トークン）を生成して返します。
        // 今回は簡易的にユーザーIDやUUIDを文字列にしてトークン代わりにします。
        // ※本格的なJWT実装をする場合は、ここでライブラリを使って生成します。
        return u.getUserId().toString();
    }

    public void logout() {
        // JWTなしのMVPではサーバー側でやることは基本なし（クライアントがtoken破棄）
    }
}
