package com.bookstorage.repository;

import com.bookstorage.model.Book;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByCategoriesId(Long categoryId);

    boolean existsById(@NonNull Long id);

}
