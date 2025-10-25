package ru.practicum.ewm.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories ORDER BY id LIMIT :size OFFSET :offset", nativeQuery = true)
    List<Category> findByOffset(Integer offset, Integer size);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :categoryId")
    int deleteCategory(Long categoryId);
}
