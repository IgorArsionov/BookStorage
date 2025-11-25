package com.bookstorage.service;

import com.bookstorage.dto.BookDto;
import com.bookstorage.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
