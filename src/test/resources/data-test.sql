
INSERT INTO role (name) VALUES ('ADMIN'), ('LIBRARIAN'), ('MEMBER');

INSERT INTO categories (name) VALUES ('Technology'), ('Fiction');

INSERT INTO users (username, password, email, role_id) VALUES
                                                           ('admin', '$2a$10$GWVbpG8yFaSyoZ2.zRPjIu/aaWnqW0LiBHASb5YzKbQHsCo8Zb76W', 'admin@library.com', (SELECT id FROM role WHERE name = 'ADMIN')),
                                                           ('librarian', '$2a$10$pqMKdFM0g7ec3z/DkeLJ1.mKQJzn/yKXQF2gYN2dzkHYWpyLKM59q', 'lib@library.com', (SELECT id FROM role WHERE name = 'LIBRARIAN')),
                                                           ('member', '$2a$10$A3vcF9CV.a.VSYqWbfsNcOyNQYP39KL0KWu/yCwQ4pITrA1H9aNtC', 'member@library.com', (SELECT id FROM role WHERE name = 'MEMBER'));


INSERT INTO books (title, author, isbn, status, category_id, cover_image_url) VALUES
                                                                                  ('The Pragmatic Programmer', 'Andy Hunt', '978-0201616224', 'AVAILABLE', (SELECT id FROM categories WHERE name = 'Technology'), '/images/default-book.png'),
                                                                                  ('Clean Code', 'Robert C. Martin', '978-0132350884', 'AVAILABLE', (SELECT id FROM categories WHERE name = 'Technology'), null),
                                                                                  ('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'BORROWED', (SELECT id FROM categories WHERE name = 'Fiction'), null);


INSERT INTO borrowing_rule (max_books_allowed, borrow_duration_days) VALUES (5, 14);


INSERT INTO borrow_records (user_id, book_id, borrow_date, due_date, status) VALUES
    (
        (SELECT id FROM users WHERE username = 'member'),
        (SELECT id FROM books WHERE title = 'The Great Gatsby'),
        CURRENT_DATE,
        CURRENT_DATE + 14,
        'BORROWED'
    );