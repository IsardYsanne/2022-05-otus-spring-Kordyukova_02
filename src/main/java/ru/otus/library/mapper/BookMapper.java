package ru.otus.library.mapper;

import org.springframework.stereotype.Component;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.Genre;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book dtoToBook(BookDto bookDto) {
        final Set<Author> authors = authorsToSet(bookDto.getAuthors());
        final Genre genre = new Genre(bookDto.getGenre());
        return new Book(bookDto.getTitle(), genre, authors);
    }

    public static BookDto bookToDto(Book book) {
        final BookDto result = new BookDto();
        result.setId(book.getId());
        result.setAuthors(book
                .getAuthors()
                .stream()
                .map(Author::getName)
                .reduce((x, y) -> x + ", " + y)
                .orElse("no author"));
        result.setTitle(book.getTitle());
        final Genre genre = book.getGenre();
        if (genre != null) {
            result.setGenre(genre.getName());
        } else {
            result.setGenre(" ");
        }
        Set<CommentDto> commentDto = Collections.emptySet();
        if (book.getComments() != null) {
            commentDto = book.getComments()
                    .stream()
                    .map(BookMapper::commentToDto)
                    .collect(Collectors.toSet());
        }
        result.setComments(commentDto);
        return result;
    }

    public List<BookDto> bookListToDto(List<Book> books) {
        return books.stream().map(BookMapper::bookToDto).collect(Collectors.toList());
    }

    public static CommentDto commentToDto(Comment comment) {
        final CommentDto result = new CommentDto();
        result.setTitle(comment.getBook().getTitle());
        result.setText(comment.getCommentText());
        final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", new Locale("ru"));
        result.setDate(format.format(comment.getCommentDate()));
        return result;
    }

    public List<CommentDto> commentListToDto(List<Comment> comments) {
        return comments.stream().map(BookMapper::commentToDto).collect(Collectors.toList());
    }

    private Set<Author> authorsToSet(String authors) {
        final String[] authorsArr = authors.split(",");
        return Arrays.stream(authorsArr).map(s -> new Author(s.trim())).collect(Collectors.toSet());
    }
}
