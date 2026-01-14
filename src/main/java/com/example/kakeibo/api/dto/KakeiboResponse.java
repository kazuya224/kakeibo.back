package com.example.kakeibo.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record KakeiboResponse(
                List<CategoryDto> categories,
                List<TransactionDto> transactions) {
        public record CategoryDto(
                        UUID categoryId,
                        String name,
                        String type // '0' or '1'
        ) {
        }

        public record TransactionDto(
                        UUID transactionId,
                        UUID categoryId,
                        String categoryName,
                        LocalDate date,
                        String type, // '0' or '1'
                        Integer amount,
                        String memo) {
        }
}
