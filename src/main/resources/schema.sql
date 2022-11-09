CREATE TABLE IF NOT EXISTS authors
(
    id          SERIAL PRIMARY KEY,
    author_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS genres
(
    id         SERIAL PRIMARY KEY,
    genre_name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS books
(
    id       SERIAL PRIMARY KEY,
    title    VARCHAR(255),
    genre_id INTEGER REFERENCES genres (id)
);

CREATE TABLE IF NOT EXISTS books_authors
(
    authors_id INTEGER REFERENCES authors (id) ON DELETE CASCADE,
    books_id   INTEGER REFERENCES books (id) ON DELETE CASCADE,
    PRIMARY KEY (authors_id, books_id)
);

CREATE TABLE IF NOT EXISTS book_comments (
    id SERIAL PRIMARY KEY,
    comment_text TEXT,
    comment_date TIMESTAMP,
    book_id INTEGER REFERENCES books(id) ON DELETE CASCADE
);