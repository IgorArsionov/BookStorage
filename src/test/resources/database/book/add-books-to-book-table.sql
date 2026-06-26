INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
    (1, 'Book One', 'Author A', '111', 10.99, 'Book', null, 0),
    (2, 'Book Two', 'Author B', '222', 15.50, 'Book', null, 0),
    (3, 'Book Three', 'Author C', '333', 20.00, 'Book', null, 0);

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 100),
    (2, 100),
    (3, 100);