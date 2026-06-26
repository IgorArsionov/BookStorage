package com.bookstorage.service;

import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.CategoryMapper;
import com.bookstorage.model.Category;
import com.bookstorage.repository.CategoryRepository;
import com.bookstorage.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Get category by id")
    void getCategory_WithValidId1_ReturnsCategory() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantastic");
        category.setDescription("Fantastic category");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                categoryId,
                category.getName(),
                category.getDescription()
        );

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        Assertions.assertNotNull(categoryService.getById(categoryId));
    }

    @Test
    @DisplayName("Catching the non-existent entity exception")
    void getCategory_WithNonExistId_ReturnEntityNotFound() {
        Long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId))
                .thenThrow(new EntityNotFoundException("Can't find category with id: " + categoryId));
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );

        Assertions.assertEquals(
                "Can't find category with id: " + categoryId,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Update category by id 1")
    void updateCategory_WithValidId1_ReturnCategory() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantastic");
        category.setDescription("Fantastic category");

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy category");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(
                categoryId,
                requestDto.getName(),
                requestDto.getDescription()
        );

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        Assertions.assertNotNull(categoryService.update(categoryId, requestDto));
    }

    @Test
    @DisplayName("Create new category")
    void createCategory_NewCategory_ReturnsCategory() {
        CategoryRequestDto categoryDto = new CategoryRequestDto();
        categoryDto.setName("Category");
        categoryDto.setDescription("Description");

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        CategoryResponseDto responseDto = new CategoryResponseDto(
                1L,
                categoryDto.getName(),
                categoryDto.getDescription());

        Mockito.when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(responseDto);

        Assertions.assertNotNull(categoryService.save(categoryDto));
    }


}
