package com.bookstorage.repository.impl;

import com.bookstorage.exception.DataProcessingException;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.model.Book;
import com.bookstorage.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Book", Book.class).list();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find all books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> bookQuery = session.createQuery("from Book where id=:id", Book.class);
            bookQuery.setParameter("id", id);
            return bookQuery.uniqueResultOptional();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find Book with id: " + id, e);
        }
    }
}
