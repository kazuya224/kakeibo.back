package com.example.kakeibo.controller;

import com.example.kakeibo.api.dto.KakeiboResponse;
import com.example.kakeibo.service.KakeiboService;
import com.example.kakeibo.api.dto.TransactionPostResponse;
import com.example.kakeibo.api.dto.TransactionRequest;
import com.example.kakeibo.api.dto.CategoryAddResponse;
import com.example.kakeibo.api.dto.CategoryAddRequest;
import com.example.kakeibo.api.dto.CategoryRequest;
import com.example.kakeibo.api.dto.CategoryResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.web.bind.annotation.RequestParam;

// @CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", // クッキー付きの通信を許可
//         allowedHeaders = "*", // すべてのヘッダーを許可
//         methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
//                 RequestMethod.OPTIONS })
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
    public ResponseEntity<TransactionPostResponse> createTransaction(
            @RequestBody TransactionRequest request,
            @CookieValue(name = "access_token", required = false) String userIdStr) {
        System.out.println("Cookieの中身" + userIdStr);

        // 1. Cookieがない場合のチェック
        if (userIdStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            System.out.println("リクエスト受け取り: " + request);

            // 2. 文字列からUUIDへの変換（形式が正しくないとここでエラーになるためtry-catch推奨）
            UUID userId = UUID.fromString(userIdStr);
            System.out.println("2. ユーザーID変換成功: " + userId);

            // 3. サービスの実行
            TransactionPostResponse response = kakeiboService.saveTransaction(request, userId);
            System.out.println("3. サービス保存成功: " + response);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("エラー発生詳細: ");
            e.printStackTrace();
            // UUIDの形式が不正な場合
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryAddResponse> createCategory(
            @RequestBody CategoryAddRequest request,
            @CookieValue(name = "access_token") String token // CookieからUUID文字列を取得
    ) {
        // 文字列のトークン（UUID）をUUID型に変換
        UUID userId = UUID.fromString(token);

        // サービス呼び出し
        CategoryAddResponse response = kakeiboService.addCategory(request, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public CategoryResponse getCategories(@CookieValue(name = "access_token") String userIdStr) {
        // 1. Cookieから取得した文字列をUUIDに変換
        UUID userId = UUID.fromString(userIdStr);

        // 2. インスタンス変数 kakeiboService を使用し、userIdを引数に渡す
        return kakeiboService.getCategoryList(userId);
    }

}
