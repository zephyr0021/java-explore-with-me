package ru.practicum.ewm.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.category.controller.AdminCategoryController;
import ru.practicum.ewm.category.controller.CategoryController;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryRequest;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CategoryController.class, AdminCategoryController.class})
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private final String categoryUrl = "/categories";

    private final String adminCategoryUrl = "/admin/categories";

    @Test
    public void getCategories() throws Exception {
        List<CategoryDto> categories = Instancio.createList(CategoryDto.class);
        when(categoryService.getCategories(anyInt(), anyInt())).thenReturn(categories);

        mockMvc.perform(get(categoryUrl))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categories)));
        Mockito.verify(categoryService, Mockito.times(1)).getCategories(anyInt(), anyInt());
    }

    @Test
    public void getCategory() throws Exception {
        CategoryDto category = Instancio.create(CategoryDto.class);
        when(categoryService.getCategory(category.getId())).thenReturn(category);

        mockMvc.perform(get(categoryUrl + "/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
        Mockito.verify(categoryService, Mockito.times(1)).getCategory(category.getId());
    }

    @Test
    public void createCategory() throws Exception {
        CategoryRequest categoryRequest = Instancio.create(CategoryRequest.class);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName(categoryRequest.getName());

        when(categoryService.createCategory(categoryRequest)).thenReturn(categoryDto);

        mockMvc.perform(post(adminCategoryUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));

        Mockito.verify(categoryService, Mockito.times(1)).createCategory(categoryRequest);
    }

    @Test
    public void deleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete(adminCategoryUrl + "/" + 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(1L);
    }

    @Test
    public void updateCategory() throws Exception {
        CategoryRequest categoryRequest = Instancio.create(CategoryRequest.class);
        CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(1L);
        categoryDto.setName(categoryRequest.getName());
        when(categoryService.updateCategory(1L, categoryRequest)).thenReturn(categoryDto);

        mockMvc.perform(patch(adminCategoryUrl + "/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));


    }
}
