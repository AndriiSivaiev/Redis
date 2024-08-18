package com.spring_book.redis.repository;

import com.spring_book.redis.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Поиск категории по имени
    Optional<Category> findByCategoryName(String categoryName);

    // Проверка существования категории по имени
    boolean existsByCategoryName(String categoryName);
}
