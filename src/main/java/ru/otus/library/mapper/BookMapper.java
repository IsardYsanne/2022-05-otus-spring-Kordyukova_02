package ru.otus.library.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMapper implements ResultSetExtractor<List<Book>> {

    private Map<Long, Book> books = new HashMap<>();

    @Override
    public List<Book> extractData(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                final Long id = resultSet.getLong("id");
                Book book = books.get(id);
                if (book == null) {
                    book = new Book();
                    book.setId(id);
                    book.setTitle(resultSet.getString("title"));
                    final Genre genre = new Genre();
                    genre.setId(resultSet.getLong("genre_id"));
                    genre.setName(resultSet.getString("genre_name"));
                    book.setGenre(genre);
                    final List<Author> authors = new ArrayList<>();
                    extractAuthor(resultSet, book, authors);
                    books.put(id, book);
                } else {
                    final List<Author> authors = book.getAuthors();
                    extractAuthor(resultSet, book, authors);
                }
            }
            return new ArrayList<>(books.values());
        } catch (SQLException e) {
            throw new RuntimeException("Book extracting error. " + e.getLocalizedMessage());
        } catch (DataAccessException de) {
            throw new RuntimeException("Data access error. " + de.getLocalizedMessage());
        }
    }

    private void extractAuthor(ResultSet resultSet, Book book, List<Author> authors) {
        try {
            final String name = resultSet.getString("author_name");
            if (name != null) {
                final Author author = new Author();
                author.setId(resultSet.getLong("author_id"));
                author.setName(name);
                authors.add(author);
            }
            book.setAuthors(authors);
        } catch (SQLException e) {
            throw new RuntimeException("Author extracting error. " + e.getLocalizedMessage());
        }
    }
}
