package ru.otus.library.mapper;

import org.springframework.stereotype.Component;
import ru.otus.library.dto.AuthorDto;
import ru.otus.library.model.entity.Author;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public List<AuthorDto> authorListToDto(List<Author> authorsNames) {
        return authorsNames.stream().map(AuthorMapper::authorToDto).collect(Collectors.toList());
    }

    public static AuthorDto authorToDto(Author author) {
        final AuthorDto result = new AuthorDto();
        result.setId(author.getId());
        result.setName(author.getName());
        return result;
    }

    public Author dtoToAuthor(AuthorDto authorDto) {
        return new Author(authorDto.getName());
    }
}
