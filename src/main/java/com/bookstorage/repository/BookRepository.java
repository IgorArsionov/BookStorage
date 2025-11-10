package com.bookstorage.repository;

import com.bookstorage.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
