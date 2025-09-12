package ru.practicum.ewm.category;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.mapper.CategoryMapperImpl;
import ru.practicum.ewm.category.model.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryMapperTests {
    private final CategoryMapper mapper = new CategoryMapperImpl();

    @Test
    void toCategoryDto() {
        Category category = Instancio.create(Category.class);
        CategoryDto categoryDto = mapper.toCategoryDto(category);

        assertEquals(category.getId(), categoryDto.getId());
        assertEquals(category.getName(), categoryDto.getName());

    }

    @Test
    void toCategory() {
        CategoryRequest request = Instancio.create(CategoryRequest.class);
        Category category = mapper.toCategory(request);

        assertEquals(request.getName(), category.getName());
    }

    @Test
    void updateCategory() {
        CategoryRequest request = Instancio.create(CategoryRequest.class);
        Category category = Instancio.create(Category.class);
        Category newCategory = mapper.updateCategory(request, category);

        assertEquals(request.getName(), newCategory.getName());
    }
}
