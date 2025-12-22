package com.bookstorage.service.impl;

import com.bookstorage.dto.BookDto;
import com.bookstorage.dto.CreateBookRequestDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find Book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find Book by id: " + id)
                );
        Book save = bookRepository.save(bookMapper.updateBook(book, requestDto));
        return bookMapper.toDto(save);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
