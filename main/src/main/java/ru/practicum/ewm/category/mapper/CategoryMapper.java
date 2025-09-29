package ru.practicum.ewm.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryRequest request);

    Category toCategory(CategoryDto dto);

    Category updateCategory(CategoryRequest request, @MappingTarget Category category);
}
