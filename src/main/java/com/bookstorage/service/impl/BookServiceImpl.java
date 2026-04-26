package com.bookstorage.service.impl;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookDtoWithoutCategoryIds;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String ERROR_FIND_BOOK = "Can't find Book by id: ";

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookResponseDto save(BookCreateRequestDto requestDto) {
        return bookMapper.toDto(bookRepository.save(
                bookMapper.toEntity(requestDto))
        );
    }

    @Override
    public Page<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDtoWithoutCategoryIds findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERROR_FIND_BOOK + id));
        return bookMapper.toDtoWithoutCategories(book);
    }

    @Override
    public BookResponseDto update(Long id, BookCreateRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(ERROR_FIND_BOOK + id)
                );
        return bookMapper.toDto(bookRepository.save(bookMapper.updateBook(book, requestDto)));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponseDto> findAllByCategoriesId(Long id) {
        return bookRepository.findAllByCategoriesId(id).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
