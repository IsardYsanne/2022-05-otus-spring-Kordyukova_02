package ru.otus.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.library.model.entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(final ResultSet resultSet, int i) {
        try {
            final Author author = new Author();
            author.setId(resultSet.getLong("id"));
            author.setName(resultSet.getString("author_name"));
            return author;
        } catch (SQLException sqle) {
            throw new RuntimeException("Author mapping error");
        }
    }
}
