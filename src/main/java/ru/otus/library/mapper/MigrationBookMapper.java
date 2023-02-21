package ru.otus.library.mapper;

import org.springframework.stereotype.Component;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.jpa.CommentJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MigrationBookMapper {

    public Book bookJpaToBookMongo(BookJpa book) {
        final Book bookMongo = new Book();

        final Set<AuthorJpa> authorsJpa = book.getAuthors();
        final Set<Author> authorsSet = authorsJpa.stream().map(authorJpa -> {
            final Author author = new Author();
            author.setId(authorJpa.getId().toString());
            author.setName(authorJpa.getName());
            return author;
        }).collect(Collectors.toSet());

        bookMongo.setAuthors(authorsSet);
        bookMongo.setTitle(book.getTitle());

        GenreJpa genreJpa = book.getGenre();
        String genreName = " ";

        if (Objects.nonNull(genreJpa) && Objects.nonNull(genreJpa.getName())) {
            genreName = genreJpa.getName();
        }
        bookMongo.setGenre(genreName);

        final Set<CommentJpa> commentsJpa = book.getComments();

        if (Objects.nonNull(commentsJpa) && commentsJpa.size() > 0) {
            Set<Comment> mongoComments = new HashSet<>();

            for (CommentJpa comment : commentsJpa) {
                Comment mongoComment = new Comment();
                mongoComment.setCommentText(comment.getCommentText());
                mongoComment.setCommentDate(comment.getCommentDate());
                mongoComments.add(mongoComment);
            }
            bookMongo.setComments(mongoComments);
        }

        return bookMongo;
    }
}
