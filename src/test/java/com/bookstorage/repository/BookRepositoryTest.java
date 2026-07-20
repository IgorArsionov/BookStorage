package com.bookstorage.repository;

import com.bookstorage.model.Book;
import com.bookstorage.model.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAll_WithValidCategoriesId_Ok() {
        Category category = new Category();
        category.setName("Fantastic");
        category.setDescription("fantastic movies");
        category = categoryRepository.save(category);

        Long categoryId = category.getId();

        Book book = new Book();
        book.setTitle("StarWars");
        book.setAuthor("G. Lucas");
        book.setCategories(Set.of(category));

        bookRepository.save(book);

        List<Book> actual = bookRepository.findAllByCategoriesId(categoryId);

        assertEquals(1, actual.size());
        assertTrue(actual.get(0).getCategories().stream()
                .anyMatch(c -> c.getId().equals(categoryId)));
    }

}
