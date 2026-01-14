package com.example.kakeibo.api.dto;

import java.time.LocalDate;
import java.util.UUID;

// 収支登録リクエスト
public record TransactionRequest(
        UUID categoryId,
        LocalDate date,
        Integer amount,
        String type) {
}