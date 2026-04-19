package com.bookstorage.controller;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book API", description = "API for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Retrieve a list of all books")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Page<BookResponseDto> getBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(summary = "Get book by ID", description = "Retrieve a book by its ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Create a new book", description = "Create a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto createBook(@RequestBody @Valid BookCreateRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @Operation(summary = "Update an existing book", description = "Update an existing book")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto updateBook(
            @PathVariable Long id, @RequestBody @Valid BookCreateRequestDto requestDto
    ) {
        return bookService.update(id, requestDto);
    }

    @Operation(summary = "Delete a book by ID", description = "Delete a book by its ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
