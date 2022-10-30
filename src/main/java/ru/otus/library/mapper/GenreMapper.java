package ru.otus.library.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.otus.library.model.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet resultSet, int rowNum) {
        try {
            final Genre genre = new Genre();
            genre.setId(resultSet.getLong("id"));
            genre.setName(resultSet.getString("genre_name"));
            return genre;
        } catch (SQLException sqle) {
            throw new RuntimeException("Genre mapping error");
        }
    }
}
