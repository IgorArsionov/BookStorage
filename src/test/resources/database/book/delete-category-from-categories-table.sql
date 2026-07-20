DELETE FROM books_categories
WHERE category_id = 100;

DELETE FROM categories
WHERE id IN (100);