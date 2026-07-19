package com.bookstorage.service;

import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.CategoryMapper;
import com.bookstorage.model.Category;
import com.bookstorage.repository.CategoryRepository;
import com.bookstorage.service.impl.CategoryServiceImpl;
import com.bookstorage.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @DisplayName("Get category by id")
    void getCategory_WithValidId1_Ok() {
        Long categoryId = 1L;

        Category category = testUtil.initCategory(categoryId);

        CategoryResponseDto expected = testUtil.initCategoryResponse(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.getById(categoryId);

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));

    }

    @Test
    @DisplayName("Catching the non-existent entity exception")
    void getCategory_WithNonExistId_EntityNotFoundException() {
        Long categoryId = 1L;

        Mockito.when(categoryRepository.findById(categoryId))
                .thenThrow(new EntityNotFoundException("Can't find category with id: " + categoryId));
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );

        assertEquals(
                "Can't find category with id: " + categoryId,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Update category by id 1")
    void updateCategory_WithValidId1_Ok() {
        Long categoryId = 1L;

        Category category = testUtil.initCategory(categoryId);

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy category");

        CategoryResponseDto expected = new CategoryResponseDto(
                categoryId,
                requestDto.getName(),
                requestDto.getDescription()
        );

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.update(categoryId, requestDto);
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Create new category")
    void createCategory_WithValidId1_Ok() {
        Long categoryId = 1L;
        CategoryRequestDto categoryDto = testUtil.initCategoryRequest();
        Category category = testUtil.initCategory(null);
        CategoryResponseDto expected = testUtil.initCategoryResponse(categoryId);

        Mockito.when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(categoryDto);

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Find all category")
    void findAll_WhenCategoryExist_Ok() {
        Category categoryOne = testUtil.initCategory(1L);
        Category categoryTwo = testUtil.initCategory(2L);
        Category categoryThree = testUtil.initCategory(3L);
        List<Category> categories = List.of(categoryOne, categoryTwo, categoryThree);
        CategoryResponseDto dtoOne = testUtil.initCategoryResponse(1L);
        CategoryResponseDto dtoTwo = testUtil.initCategoryResponse(2L);
        CategoryResponseDto dtoThree = testUtil.initCategoryResponse(3L);
        List<CategoryResponseDto> expected = List.of(dtoOne, dtoTwo, dtoThree);

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);
        Mockito.when(categoryMapper.toDto(categoryOne)).thenReturn(dtoOne);
        Mockito.when(categoryMapper.toDto(categoryTwo)).thenReturn(dtoTwo);
        Mockito.when(categoryMapper.toDto(categoryThree)).thenReturn(dtoThree);
        List<CategoryResponseDto> actual = categoryService.findAll();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(reflectionEquals(
                    expected.get(i),
                    actual.get(i)
            ));
        }
    }


}
