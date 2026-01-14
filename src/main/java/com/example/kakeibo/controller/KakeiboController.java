package com.example.kakeibo.controller;

import com.example.kakeibo.api.dto.KakeiboResponse;
import com.example.kakeibo.service.KakeiboService;
import com.example.kakeibo.api.dto.TransactionPostResponse;
import com.example.kakeibo.api.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", // クッキー付きの通信を許可
        allowedHeaders = "*", // すべてのヘッダーを許可
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
                RequestMethod.OPTIONS })
@RestController
@RequiredArgsConstructor
public class KakeiboController {

    private final KakeiboService kakeiboService;

    @GetMapping("/kakeibo")
    public KakeiboResponse get(@CookieValue(name = "access_token") String userIdStr) {
        // Cookieから取得した文字列をUUIDに変換
        UUID userId = UUID.fromString(userIdStr);
        return kakeiboService.get(userId);
    }

    @PostMapping("/transaction")
    public TransactionPostResponse createTransaction(@RequestBody TransactionRequest request,
            @CookieValue(name = "access_token") String userIdStr) {
        System.out.println("リクエスト" + request);
        UUID userId = UUID.fromString(userIdStr);
        return kakeiboService.saveTransaction(request, userId);
    }
}
