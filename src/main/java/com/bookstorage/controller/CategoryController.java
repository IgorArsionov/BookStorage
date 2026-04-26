package com.bookstorage.controller;

import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.service.BookService;
import com.bookstorage.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category API", description = "API for managing categories")
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(summary = "Create category", description = "Create a new category")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CategoryResponseDto createCategory(
            @RequestBody @Valid CategoryRequestDto requestDto
    ) {
        return categoryService.save(requestDto);
    }

    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.findAll();
    }

    @Operation(summary = "Get category by id", description = "Get category by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update category", description = "Update category by id")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public CategoryResponseDto updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequestDto requestDto
    ) {
        return categoryService.update(id, requestDto);
    }

    @Operation(summary = "Delete category", description = "Delete category by id")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Delete category", description = "Delete category by id")
    @GetMapping("/{id}/books")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookResponseDto> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.findAllByCategoriesId(id);
    }

}
