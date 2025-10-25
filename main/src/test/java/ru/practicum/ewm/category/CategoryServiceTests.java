package ru.practicum.ewm.category;


import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getCategories() {
        List<Category> categories = Instancio.createList(Category.class);

        when(categoryRepository.findByOffset(anyInt(), anyInt())).thenReturn(categories);

        List<CategoryDto> result = categoryService.getCategories(5, 10);

        verify(categoryRepository, Mockito.times(1)).findByOffset(anyInt(), anyInt());
        assertEquals(categories.size(), result.size());
    }

    @Test
    void getCategory() {
        Category category = Instancio.create(Category.class);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);
        CategoryDto result = categoryService.getCategory(category.getId());

        verify(categoryRepository, Mockito.times(1)).findById(anyLong());
        assertEquals(category.getId(), result.getId());
    }
}
