package com.bookstorage.service.impl;

import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.CategoryMapper;
import com.bookstorage.model.Category;
import com.bookstorage.repository.CategoryRepository;
import com.bookstorage.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id: " + id)
        ));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        return categoryMapper.toDto(
                categoryRepository.save(
                        categoryMapper.toEntity(requestDto)
                )
        );
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find category by id " + id));

        categoryMapper.updateCategory(category, requestDto);

        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
