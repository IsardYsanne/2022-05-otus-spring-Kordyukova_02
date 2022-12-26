package ru.otus.library.mapper;

import org.springframework.stereotype.Component;
import ru.otus.library.dto.GenreDto;
import ru.otus.library.model.entity.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public List<GenreDto> genreListToDto(List<Genre> genres) {
        return genres.stream().map(GenreMapper::genreToDto).collect(Collectors.toList());
    }

    public static GenreDto genreToDto(Genre genre) {
        final GenreDto result = new GenreDto();
        result.setId(genre.getId());
        result.setGenreName(genre.getName());
        return result;
    }

    public Genre dtoToGenre(GenreDto genreDto) {
        return new Genre(genreDto.getGenreName());
    }
}
