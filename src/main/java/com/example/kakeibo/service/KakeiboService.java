package com.example.kakeibo.service;

import com.example.kakeibo.api.dto.KakeiboResponse;
import com.example.kakeibo.api.dto.TransactionPostResponse;
import com.example.kakeibo.api.dto.TransactionRequest;
import com.example.kakeibo.api.dto.CategoryAddRequest;
import com.example.kakeibo.api.dto.CategoryAddResponse;
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
                // 論理削除されていないものだけを取得
                List<Category> categories = categoryRepository.findByUserIdAndDelFlgOrderByTypeAscNameAsc(userId, "0");

                Map<UUID, String> categoryNameMap = categories.stream()
                                .collect(Collectors.toMap(Category::getCategoryId, Category::getName));

                List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(userId);

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
        public CategoryAddResponse addCategory(CategoryAddRequest request, UUID userId) {
                Category category = new Category();
                category.setUserId(userId);
                category.setName(request.getCategory_name());
                category.setType(request.getType_flg());
                category.setDelFlg("0"); // 明示的にセット

                Category saved = categoryRepository.save(category);

                return new CategoryAddResponse(
                                saved.getCategoryId().toString(),
                                saved.getName(),
                                saved.getType());
        }

        public CategoryResponse getCategoryList(UUID userId) {
                // 論理削除されていないものだけを取得
                List<Category> categories = categoryRepository.findByUserIdAndDelFlgOrderByTypeAscNameAsc(userId, "0");

                List<CategoryResponse.CategoryInfo> categoryInfos = categories.stream()
                                .map(c -> CategoryResponse.CategoryInfo.builder()
                                                .categoryId(c.getCategoryId().toString())
                                                .name(c.getName())
                                                .type(c.getType())
                                                .build())
                                .toList();

                return new CategoryResponse(categoryInfos);
        }

        /**
         * カテゴリ更新
         */
        @Transactional
        public CategoryAddResponse updateCategory(UUID categoryId, CategoryAddRequest request, UUID userId) {
                Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("Category not found"));

                if (!category.getUserId().equals(userId)) {
                        throw new RuntimeException("権限がありません");
                }

                category.setName(request.getCategory_name());
                category.setType(request.getType_flg());

                Category updated = categoryRepository.save(category);

                return new CategoryAddResponse(
                                updated.getCategoryId().toString(),
                                updated.getName(),
                                updated.getType());
        }

        /**
         * カテゴリ削除
         */
        @Transactional
        public void deleteCategory(UUID categoryId, UUID userId) {
                Category category = categoryRepository.findById(categoryId)
                                .orElseThrow(() -> new RuntimeException("Category not found"));

                if (!category.getUserId().equals(userId)) {
                        throw new RuntimeException("権限がありません");
                }

                // 物理削除ではなく、フラグを "1" (削除済み) にして保存
                category.setDelFlg("1");
                categoryRepository.save(category);
        }
}
