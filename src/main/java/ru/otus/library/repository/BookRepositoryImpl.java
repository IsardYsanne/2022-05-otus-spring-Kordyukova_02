package ru.otus.library.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;

import java.util.HashMap;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    public BookRepositoryImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Book findBookById(final Long id) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        final List<Book> books = namedJdbc.query(
                "SELECT books.id, books.title, genres.id as genre_id, genres.genre_name, authors.id as author_id, authors.author_name " +
                        "FROM books " +
                        "LEFT OUTER JOIN genres ON books.genre_id = genres.id " +
                        "LEFT OUTER JOIN books_authors ON books.id = books_authors.books_id " +
                        "LEFT OUTER JOIN authors ON books_authors.authors_id = authors.id " +
                        "WHERE books.id = :id", mapSqlParameterSource, new BookMapper());
        Book result = null;
        if (!books.isEmpty()) {
            result = books.get(0);
        }
        return result;
    }

    @Override
    public List<Book> findAllBooks() {
        return namedJdbc.getJdbcOperations().query(
                "SELECT books.id, books.title, genres.id as genre_id, genres.genre_name, authors.id as author_id, authors.author_name " +
                        "FROM books " +
                        "LEFT OUTER JOIN genres ON books.genre_id = genres.id " +
                        "LEFT OUTER JOIN books_authors ON books.id = books_authors.books_id " +
                        "LEFT OUTER JOIN authors ON books_authors.authors_id = authors.id ", new BookMapper());
    }

    @Override
    public List<Book> findBooksByAuthor(final Author author) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("id", author.getId());
        return namedJdbc.query(
                "SELECT books.id, books.title, genres.id as genre_id, genres.genre_name, authors.id as author_id, authors.author_name " +
                        "FROM books " +
                        "LEFT OUTER JOIN genres ON books.genre_id = genres.id " +
                        "LEFT OUTER JOIN books_authors ON books.id = books_authors.books_id " +
                        "LEFT OUTER JOIN authors ON books_authors.authors_id = authors.id " +
                        "WHERE authors.id = :id", params, new BookMapper());
    }

    @Override
    public List<Book> findBooksByTitle(final String title) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("title", title);
        return namedJdbc.query(
                "SELECT books.id, books.title, genres.id as genre_id, genres.genre_name, authors.id as author_id, authors.author_name " +
                        "FROM books " +
                        "LEFT OUTER JOIN genres ON books.genre_id = genres.id " +
                        "LEFT OUTER JOIN books_authors ON books.id = books_authors.books_id " +
                        "LEFT OUTER JOIN authors ON books_authors.authors_id = authors.id " +
                        "WHERE books.title = :title", mapSqlParameterSource, new BookMapper());
    }

    @Override
    public List<String> findAllTitles() {
        return namedJdbc.getJdbcOperations().queryForList("SELECT title FROM books", String.class);
    }

    @Override
    public Book saveBook(final Book book) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("title_name", book.getTitle());
        mapSqlParameterSource.addValue("genre_id", book.getGenre().getId());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update(
                "INSERT INTO books (title, genre_id) " +
                        "VALUES (:title_name, :genre_id)", mapSqlParameterSource, keyHolder, new String[]{"id"});
        Long bookId = keyHolder.getKey().longValue();

        for (final Author author : book.getAuthors()) {
            final MapSqlParameterSource mapSqlParameterSource2 = new MapSqlParameterSource();
            mapSqlParameterSource2.addValue("author_id", author.getId());
            mapSqlParameterSource2.addValue("book_id", bookId);
            namedJdbc.update(
                    "INSERT INTO books_authors (books_id, authors_id) " +
                            "VALUES (:book_id, :author_id)", mapSqlParameterSource2);
        }

        book.setId(bookId);
        return book;
    }

    @Override
    public int updateBookTitleById(Long id, String newTitle) {
        final MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        param.addValue("new_title", newTitle);
        return namedJdbc.update(
                "UPDATE books SET title = :new_title " +
                        "WHERE id = :id", param);
    }

    @Override
    public int deleteBookById(Long id) {
        final MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        return namedJdbc.update("DELETE FROM books WHERE id = :id", param);
    }

    @Override
    public int deleteAll() {
        namedJdbc.getJdbcOperations().update("DELETE FROM books_authors");
        return namedJdbc.getJdbcOperations().update("DELETE FROM books");
    }
}
