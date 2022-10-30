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