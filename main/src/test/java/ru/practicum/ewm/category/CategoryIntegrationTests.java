package ru.practicum.ewm.category;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.mapper.CategoryMapperImpl;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.common.exception.CategoryNotEmptyException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Import({CategoryService.class, CategoryMapperImpl.class})
public class CategoryIntegrationTests {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createCategory() {
        CategoryRequest newCategoryRequest = Instancio.create(CategoryRequest.class);

        categoryService.createCategory(newCategoryRequest);

        Optional<Category> newCategory = categoryRepository.findById(26L);

        assertTrue(newCategory.isPresent());
        assertEquals(newCategoryRequest.getName(), newCategory.get().getName());
    }

    @Test
    void deleteCategory() {
        categoryService.deleteCategory(1L);

        Optional<Category> category = categoryRepository.findById(1L);

        assertTrue(category.isEmpty());
    }

    @Test
    void updateCategory() {
        CategoryRequest updCategoryRequest = Instancio.create(CategoryRequest.class);

        categoryService.updateCategory(1L, updCategoryRequest);

        Optional<Category> category = categoryRepository.findById(1L);

        assertTrue(category.isPresent());
        assertEquals(updCategoryRequest.getName(), category.get().getName());
    }

    @Test
    void deleteNotEmptyCategory() {
        assertThrows(CategoryNotEmptyException.class, () -> categoryService.deleteCategory(5L));
    }

    @Test
    void getCategoriesFrom() {
        List<CategoryDto> categoriest = categoryService.getCategories(10, 10);

        assertEquals(10, categoriest.size());
        assertEquals(11L, categoriest.getFirst().getId());
    }
}
