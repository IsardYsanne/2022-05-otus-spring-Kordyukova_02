package ru.otus.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.mapper.AuthorMapper;
import ru.otus.library.model.entity.Author;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    public AuthorRepositoryImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Author findAuthorById(final String id) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);

        return namedJdbc.queryForObject("SELECT * FROM authors WHERE id = :id", mapSqlParameterSource, new AuthorMapper());
    }

    @Override
    public Author findAuthorByName(final String name) {
        final HashMap<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("name", name);
        try {
            return namedJdbc.queryForObject("SELECT * FROM authors WHERE author_name = :name", sqlParams, new AuthorMapper());
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public List<String> findAllAuthorsNames() {
        return namedJdbc.getJdbcOperations().queryForList("SELECT author_name FROM authors", String.class);
    }

    @Override
    public Author saveAuthor(final Author author) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("name", author.getName());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update("INSERT INTO authors(author_name) VALUES (:name)", mapSqlParameterSource, keyHolder, new String[]{"id"});
        author.setId(keyHolder.getKey().longValue());
        author.setBooks(Collections.emptyList());

        return author;
    }

    @Override
    public int deleteAuthor(final Author author) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", author.getId());

        return namedJdbc.update("DELETE FROM authors WHERE id = :id", mapSqlParameterSource);
    }

    @Override
    public int deleteAuthorById(final Long id) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);

        return namedJdbc.update("DELETE FROM authors WHERE id = :id", mapSqlParameterSource);
    }

    @Override
    public int deleteAll() {
        return namedJdbc.getJdbcOperations().update("DELETE FROM authors");
    }
}
