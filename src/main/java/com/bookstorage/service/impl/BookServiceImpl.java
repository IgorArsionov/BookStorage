package com.bookstorage.service.impl;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookResponseDto save(BookCreateRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookResponseDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find Book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookResponseDto update(Long id, BookCreateRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find Book by id: " + id)
                );
        return bookMapper.toDto(bookRepository.save(bookMapper.updateBook(book, requestDto)));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
