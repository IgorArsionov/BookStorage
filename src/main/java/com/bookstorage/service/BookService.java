package com.bookstorage.service;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponseDto save(BookCreateRequestDto requestDto);

    Page<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto findById(Long id);

    BookResponseDto update(Long id, BookCreateRequestDto requestDto);

    void deleteById(Long id);
}
