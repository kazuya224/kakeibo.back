package com.example.kakeibo.service;

import com.example.kakeibo.api.dto.KakeiboResponse;
import com.example.kakeibo.api.dto.TransactionPostResponse;
import com.example.kakeibo.api.dto.TransactionRequest;
import com.example.kakeibo.api.dto.CategoryRequest;
import com.example.kakeibo.api.dto.CategoryResponse;
import com.example.kakeibo.domain.Category;
import com.example.kakeibo.domain.Transaction;
import com.example.kakeibo.repository.CategoryRepository;
import com.example.kakeibo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KakeiboService {

        private final CategoryRepository categoryRepository;
        private final TransactionRepository transactionRepository;

        public KakeiboResponse get(UUID userId) {

                // 1) カテゴリ取得
                List<Category> categories = categoryRepository.findByUserIdOrderByTypeAscNameAsc(userId);

                // categoryId -> categoryName の辞書（transactionsに名前を付けるため）
                Map<UUID, String> categoryNameMap = categories.stream()
                                .collect(Collectors.toMap(Category::getCategoryId, Category::getName));

                // 2) 取引取得
                List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(userId);

                // 3) DTO変換
                List<KakeiboResponse.CategoryDto> categoryDtos = categories.stream()
                                .map(c -> new KakeiboResponse.CategoryDto(c.getCategoryId(), c.getName(), c.getType()))
                                .toList();

                List<KakeiboResponse.TransactionDto> transactionDtos = transactions.stream()
                                .map(t -> new KakeiboResponse.TransactionDto(
                                                t.getTransactionId(),
                                                t.getCategoryId(),
                                                categoryNameMap.getOrDefault(t.getCategoryId(), "(不明カテゴリ)"),
                                                t.getDate(),
                                                t.getType(),
                                                t.getAmount(),
                                                t.getMemo()))
                                .toList();

                return new KakeiboResponse(categoryDtos, transactionDtos);
        }

        @Transactional
        public TransactionPostResponse saveTransaction(TransactionRequest request, UUID userId) {

                // ドメインモデルのインスタンス化
                Transaction transaction = new Transaction(
                                UUID.randomUUID(),
                                userId, // 認証情報（Cookie）から来たIDを使用
                                request.categoryId(),
                                request.date(),
                                request.type(),
                                request.amount(),
                                "" // メモ
                );

                // DBへ保存
                Transaction saved = transactionRepository.save(transaction);

                return new TransactionPostResponse(
                                "success",
                                saved.getTransactionId(),
                                saved.getCategoryId(),
                                saved.getDate(),
                                saved.getAmount());
        }

        @Transactional
        public CategoryResponse addCategory(CategoryRequest request, UUID userId) {
                // エンティティの構築
                Category category = new Category();
                category.setUserId(userId); // 認証情報から受け取ったIDをセット
                category.setName(request.getCategory_name());
                category.setType(request.getType_flg());

                // 保存（PrePersistでUUID生成と日付が入る想定）
                Category saved = categoryRepository.save(category);

                // レスポンスDTOに変換
                return new CategoryResponse(
                                saved.getCategoryId().toString(),
                                saved.getName(),
                                saved.getType());
        }
}
