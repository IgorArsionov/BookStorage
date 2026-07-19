package com.bookstorage.util;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookDtoWithoutCategoryIds;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.model.Book;
import com.bookstorage.model.Category;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class TestUtil {
    public BookCreateRequestDto initBookWhereAuthorTaras() {
        BookCreateRequestDto book = new BookCreateRequestDto();
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("111");
        book.setDescription("Book");
        book.setCoverImage(null);
        book.setPrice(new BigDecimal("199.99"));
        book.setCategoryIds(Set.of(100L));
        return book;
    }

    //
    public BookResponseDto initBookResponseDtoWhereAuthorTaras() {
        BookResponseDto book = new BookResponseDto();
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setIsbn("111");
        book.setDescription("Book");
        book.setCoverImage(null);
        book.setPrice(new BigDecimal("199.99"));
        book.setCategoryIds(Set.of(100L));
        return book;
    }

    public BookDtoWithoutCategoryIds initBookDtoWithoutCategoryIds(Long id) {
        return new BookDtoWithoutCategoryIds(
                id,
                "Kobzar",
                "Taras Shevchenko",
                "111",
                new BigDecimal("199.99"),
                "Book",
                null
        );
    }

    public Book initBook(Long id) {
        Book book = new Book();
        if (id != null) {
        book.setId(id);
        }
        book.setIsbn("111");
        book.setPrice(new BigDecimal("199.99"));
        book.setAuthor("Taras Shevchenko");
        book.setTitle("Kobzar");
        book.setDescription("Book");
        book.setCoverImage(null);
        return book;
    }

    public List<BookResponseDto> initListBookResponseDto() {

        BookResponseDto dtoOne = new BookResponseDto()
                .setTitle("Book One")
                .setAuthor("Author A")
                .setIsbn("111")
                .setPrice(new BigDecimal("10.99"))
                .setDescription("Book")
                .setCoverImage(null)
                .setCategoryIds(Set.of(1L));

        BookResponseDto dtoTwo = new BookResponseDto()
                .setTitle("Book Two")
                .setAuthor("Author B")
                .setIsbn("222")
                .setPrice(new BigDecimal("15.50"))
                .setDescription("Book")
                .setCoverImage(null)
                .setCategoryIds(Set.of(1L));

        return List.of(dtoOne, dtoTwo);
    }

    public Category initCategory(Long id) {
        Category category = new Category();
        if (id != null) {
            category.setId(id);
        }
        category.setName("Fantastic");
        category.setDescription("Fantastic");
        return category;
    }

    public CategoryResponseDto initCategoryResponse(Long id) {
        return new CategoryResponseDto(
                id,
                "Fantastic",
                "Fantastic"
        );
    }

    public CategoryRequestDto initCategoryRequest() {
        CategoryRequestDto category = new CategoryRequestDto();
        category.setName("Fantastic");
        category.setDescription("Fantastic");
        return category;
    }
}
