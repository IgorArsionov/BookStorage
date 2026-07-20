package com.bookstorage.service;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookDtoWithoutCategoryIds;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.impl.BookServiceImpl;
import com.bookstorage.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks()
    private BookServiceImpl bookService;

    private final TestUtil testUtil = new TestUtil();

    @Test
    @DisplayName("Find book by valid id 1")
    void findBook_WithValidId1_Ok() {
        Long bookId = 1L;
        Book book = testUtil.initBook(bookId);
        BookDtoWithoutCategoryIds bookDto = testUtil.initBookDtoWithoutCategoryIds(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);
        BookDtoWithoutCategoryIds actual = bookService.findById(bookId);
        assertNotNull(actual);
        assertTrue(reflectionEquals(bookDto, actual));

    }

    @Test
    @DisplayName("Catching the non-existent entity exception")
    void findBook_WithNotExistId_EntityNotFoundException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId))
                .thenThrow(new EntityNotFoundException("Can't find book by id: " + bookId));
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        assertEquals("Can't find book by id: " + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("update book")
    void updateBook_WithValidId1_Ok() {
        Long bookId = 1L;
        Book book = testUtil.initBook(bookId);
        Book modifiedBook = testUtil.initBook(bookId);
        modifiedBook.setTitle("NewTitle");
        BookCreateRequestDto requestBook = testUtil.initBookWhereAuthorTaras();
        requestBook.setTitle("NewTitle");
        BookResponseDto expected = testUtil.initBookResponseDtoWhereAuthorTaras();
        expected.setTitle("NewTitle");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.updateBook(book, requestBook)).thenReturn(modifiedBook);
        when(bookRepository.save(modifiedBook)).thenReturn(modifiedBook);
        when(bookMapper.toDto(modifiedBook)).thenReturn(expected);
        BookResponseDto actual = bookService.update(bookId, requestBook);

        assertTrue(reflectionEquals(expected, actual, "id", "categoryIds"));
    }

    @Test
    @DisplayName("Find all book by category")
    void findAllByCategoriesId_WithValidCategoryId_Ok() {
        Long categoryId = 1L;
        Book bookOne = new Book();
        Book bookTwo = new Book();
        List<Book> books = List.of(bookOne, bookTwo);
        List<BookResponseDto> expected = testUtil.initListBookResponseDto();

        when(bookRepository.findAllByCategoriesId(categoryId))
                .thenReturn(books);
        when(bookMapper.toDto(bookOne)).thenReturn(expected.get(0));
        when(bookMapper.toDto(bookTwo)).thenReturn(expected.get(1));
        List<BookResponseDto> actual = bookService.findAllByCategoriesId(categoryId);

        assertEquals(books.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(reflectionEquals(expected.get(i), actual.get(i),
                    "Book at index " + i + " does not match"));
        }
        verify(bookRepository).findAllByCategoriesId(categoryId);
        verify(bookMapper, Mockito.times(2)).toDto(Mockito.any(Book.class));
    }

    @Test
    @DisplayName("Create new book")
    void createBook_WithValidRequest_Ok() {
        Long bookId = 1L;
        Book book = testUtil.initBook(bookId);
        BookCreateRequestDto requestDto = testUtil.initBookWhereAuthorTaras();
        BookResponseDto expected = testUtil.initBookResponseDtoWhereAuthorTaras();

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookResponseDto actual = bookService.save(requestDto);

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Find all books with pageable")
    void findAll_WithValidPageable_Ok() {
        Pageable pageable = PageRequest.of(0, 3);
        Book bookOne = testUtil.initBook(1L);
        Book bookTwo = testUtil.initBook(2L);
        List<Book> books = List.of(bookOne, bookTwo);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        BookResponseDto dtoOne = testUtil.initBookResponseDtoWhereAuthorTaras();
        BookResponseDto dtoTwo = testUtil.initBookResponseDtoWhereAuthorTaras();

        List<BookResponseDto> expectedList = List.of(dtoOne, dtoTwo);

        when(bookRepository.findAll(pageable))
                .thenReturn(bookPage);

        when(bookMapper.toDto(bookOne)).thenReturn(dtoOne);
        when(bookMapper.toDto(bookTwo)).thenReturn(dtoTwo);

        Page<BookResponseDto> actual = bookService.findAll(pageable);

        assertNotNull(actual);
        assertEquals(2, actual.getContent().size());

        for (int i = 0; i < expectedList.size(); i++) {
            assertTrue(reflectionEquals(
                    expectedList.get(i),
                    actual.getContent().get(i),
                    "id",
                    "categoryIds"
            ));
        }

        verify(bookRepository).findAll(pageable);
        verify(bookMapper, Mockito.times(2)).toDto(Mockito.any(Book.class));
    }

}
