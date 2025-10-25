package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.common.exception.CategoryNotEmptyException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        return categoryRepository.findByOffset(from, size).stream().map(categoryMapper::toCategoryDto).toList();
    }

    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryDto)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found"));
    }

    public Category getCategoryModel(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found"));
    }

    @Transactional
    public CategoryDto createCategory(CategoryRequest request) {
        Category category = categoryRepository.save(categoryMapper.toCategory(request));

        return categoryMapper.toCategoryDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!eventRepository.findEventsByCategoryId(id).isEmpty()) {
            throw new CategoryNotEmptyException("The category is not empty");
        }
        int rows = categoryRepository.deleteCategory(id);

        if (rows == 0) {
            throw new NotFoundException("Category with id=" + id + " was not found");
        }
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest request) {
        Category newCategory = categoryRepository.findById(id)
                .map(category -> categoryMapper.updateCategory(request, category))
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found"));

        newCategory = categoryRepository.save(newCategory);

        return categoryMapper.toCategoryDto(newCategory);
    }
}
