package ru.otus.library.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.mapper.GenreMapper;
import ru.otus.library.model.entity.Genre;

import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private final NamedParameterJdbcOperations namedJdbc;

    public GenreRepositoryImpl(NamedParameterJdbcOperations namedJdbc) {
        this.namedJdbc = namedJdbc;
    }

    @Override
    public Genre findGenreByName(final String genreName) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("genre_name", genreName);
        try {
            return namedJdbc.queryForObject("SELECT * FROM genres WHERE genre_name = :genre_name", mapSqlParameterSource, new GenreMapper());
        } catch (DataAccessException e) {
            throw new RuntimeException("Data access error. " + e.getLocalizedMessage());
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        return namedJdbc.getJdbcOperations().query("SELECT * FROM genres", new GenreMapper());
    }

    @Override
    public Genre saveGenre(final Genre genre) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("genre_name", genre.getName());
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbc.update("INSERT INTO genres (genre_name) VALUES (:genre_name)", mapSqlParameterSource, keyHolder, new String[]{"id"});
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public int deleteGenre(final Genre genre) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", genre.getId());
        namedJdbc.update("UPDATE books SET genre_id = null WHERE genre_id = :id", mapSqlParameterSource);
        return namedJdbc.update("DELETE FROM genres WHERE id = :id", mapSqlParameterSource);
    }

    @Override
    public int deleteAll() {
        namedJdbc.getJdbcOperations().update("UPDATE books SET genre_id = null");
        return namedJdbc.getJdbcOperations().update("DELETE FROM genres");
    }
}
