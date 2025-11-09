package com.bookstorage.service;

import com.bookstorage.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
