package com.example.kakeibo.api.dto;

import java.time.LocalDate;
import java.util.UUID;

// 収支登録レスポンス
public record TransactionPostResponse(
        String responseStatus,
        UUID transactionId,
        UUID categoryId,
        LocalDate date,
        Integer amount) {
}