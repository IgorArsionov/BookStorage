package com.bookstorage.service;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookDtoWithoutCategoryIds;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.mapper.BookMapper;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import com.bookstorage.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks()
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find book by valid id 1")
    void findBook_WithValidId1_ReturnBookDtoWithoutCategory() {
        Long bookId = 1L;
        Book book = initBook(bookId);

        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds(
                bookId,
                "BookTitle",
                "Author",
                "1234",
                new BigDecimal("199.99"),
                "",
                ""
        );

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDto);

        BookDtoWithoutCategoryIds actual = bookService.findById(bookId);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(bookDto, actual);

    }

    @Test
    @DisplayName("Catching the non-existent entity exception")
    void findBook_WithNotExistId_ReturnEntityNotFound() {
        Long bookId = 1L;

        Mockito.when(bookRepository.findById(bookId))
                .thenThrow(new EntityNotFoundException("Can't find book by id: " + bookId));

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );

        Assertions.assertEquals("Can't find book by id: " + bookId, exception.getMessage());
    }

    @Test
    @DisplayName("update book")
    void updateBook_WithValidId1_ReturnBook() {
        Long bookId = 1L;
        Book book = initBook(bookId);
        Book expected = initBook(bookId);
        expected.setTitle("NewAuthor");

        BookCreateRequestDto requestBook = new BookCreateRequestDto();
        requestBook.setIsbn("1234");
        requestBook.setPrice(new BigDecimal("199.99"));
        requestBook.setAuthor("NewAuthor");
        requestBook.setTitle("BookTitle");
        requestBook.setDescription("");
        requestBook.setCoverImage("");

        BookResponseDto responseDto = new BookResponseDto();
        responseDto.setIsbn("1234");
        responseDto.setPrice(new BigDecimal("199.99"));
        responseDto.setAuthor("NewAuthor");
        responseDto.setTitle("BookTitle");
        responseDto.setDescription("");
        responseDto.setCoverImage("");

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.updateBook(book, requestBook)).thenReturn(expected);
        Mockito.when(bookRepository.save(expected)).thenReturn(expected);
        Mockito.when(bookMapper.toDto(expected)).thenReturn(responseDto);

        BookResponseDto actual = bookService.update(bookId, requestBook);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Find all book by category")
    void findAllByCategoriesId_ValidCategoryId_ReturnListBooks() {
        Long categoryId = 1L;

        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        List<Book> books = List.of(book1, book2, book3);

        BookResponseDto dto1 = new BookResponseDto()
                .setTitle("B1")
                .setCategoryIds(Set.of(1L));

        BookResponseDto dto2 = new BookResponseDto()
                .setTitle("B2")
                .setCategoryIds(Set.of(1L));

        BookResponseDto dto3 = new BookResponseDto()
                .setTitle("B3")
                .setCategoryIds(Set.of(1L));

        Mockito.when(bookRepository.findAllByCategoriesId(categoryId))
                .thenReturn(books);
        Mockito.when(bookMapper.toDto(book1)).thenReturn(dto1);
        Mockito.when(bookMapper.toDto(book2)).thenReturn(dto2);
        Mockito.when(bookMapper.toDto(book3)).thenReturn(dto3);

        List<BookResponseDto> result = bookService.findAllByCategoriesId(categoryId);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("B1", result.get(0).getTitle());
        Assertions.assertEquals("B2", result.get(1).getTitle());
        Assertions.assertEquals("B3", result.get(2).getTitle());

        Mockito.verify(bookRepository).findAllByCategoriesId(categoryId);
        Mockito.verify(bookMapper, Mockito.times(3)).toDto(Mockito.any(Book.class));


    }

    private Book initBook(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setIsbn("1234");
        book.setPrice(new BigDecimal("199.99"));
        book.setAuthor("Author");
        book.setTitle("BookTitle");
        book.setDescription("");
        book.setCoverImage("");
        return book;
    }

}
