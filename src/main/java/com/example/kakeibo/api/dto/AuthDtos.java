package com.example.kakeibo.api.dto;

import java.util.UUID;

public class AuthDtos {
    public record SignupRequest(String email, String password, String name) {
    }

    public record LoginRequest(String email, String password) {
    }

    public record LoginResponse(String response_status, String access_token, UUID user_id) {
    }

    public record SimpleResponse(String response_status) {
    }
}