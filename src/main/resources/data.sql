INSERT INTO authors (author_name) VALUES ('Д.Оруэлл');
INSERT INTO authors (author_name) VALUES ('Е.Замятин');
INSERT INTO authors (author_name) VALUES ('Р.Бредбери');
INSERT INTO authors (author_name) VALUES ('В.Набоков');
INSERT INTO authors (author_name) VALUES ('А.Грибоедов');
INSERT INTO authors (author_name) VALUES ('Т.Кампанелла');
INSERT INTO authors (author_name) VALUES ('С.Лем');

INSERT INTO genres (genre_name) VALUES ('научная фантастика');
INSERT INTO genres (genre_name) VALUES ('антиутопия');
INSERT INTO genres (genre_name) VALUES ('роман');
INSERT INTO genres (genre_name) VALUES ('комедия');
INSERT INTO genres (genre_name) VALUES ('утопия');

INSERT INTO books (title, genre_id)
VALUES ('Город Солнца', (SELECT id FROM genres WHERE genre_name = 'утопия'));

INSERT INTO books (title, genre_id)
VALUES ('Горе от ума', (SELECT id FROM genres WHERE genre_name = 'комедия'));

INSERT INTO books (title, genre_id)
VALUES ('Лолита', (SELECT id FROM genres WHERE genre_name = 'роман'));

INSERT INTO books (title, genre_id)
VALUES ('Мы', (SELECT id FROM genres WHERE genre_name = 'антиутопия'));

INSERT INTO books (title, genre_id)
VALUES ('Солярис', (SELECT id FROM genres WHERE genre_name = 'научная фантастика'));

INSERT INTO books (title, genre_id)
VALUES ('1984', (SELECT id FROM genres WHERE genre_name = 'антиутопия'));

INSERT INTO books (title, genre_id)
VALUES ('451 градус по Фаренгейту', (SELECT id FROM genres WHERE genre_name = 'антиутопия'));

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Д.Оруэлл'),
           (SELECT id FROM books WHERE title = '1984'));

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Т.Кампанелла'),
           (SELECT id FROM books WHERE title = 'Город Солнца')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'А.Грибоедов'),
           (SELECT id FROM books WHERE title = 'Горе от ума')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'В.Набоков'),
           (SELECT id FROM books WHERE title = 'Лолита')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Е.Замятин'),
           (SELECT id FROM books WHERE title = 'Мы')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'С.Лем'),
           (SELECT id FROM books WHERE title = 'Солярис')
       );

INSERT INTO books_authors (authors_id, books_id)
VALUES (
           (SELECT id FROM authors WHERE author_name = 'Р.Бредбери'),
           (SELECT id FROM books WHERE title = '451 градус по Фаренгейту')
       );

INSERT INTO book_comments (comment_text, comment_date, book_id)
VALUES ('Главный герой вызывает отвращение, а книга необычная, не для всех.',
        '2022-10-10 12:42:12',
        (SELECT id FROM books WHERE title = 'Лолита'));

INSERT INTO book_comments (comment_text, comment_date, book_id)
VALUES ('Я боюсь оставлять коммент на эту книгу.',
        '2021-07-14 16:45:11',
        (SELECT id FROM books WHERE title = '1984'));

INSERT INTO book_comments (comment_text, comment_date, book_id)
VALUES ('Я ваще ничего не понял. ' ||
        'Однако мне понравилось :). ',
        '2019-05-02 09:12:12',
        (SELECT id FROM books WHERE title = 'Город Солнца'));