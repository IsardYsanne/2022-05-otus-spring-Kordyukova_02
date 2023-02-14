package ru.otus.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"authors", "comments"})
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    @DBRef
    private Genre genre;

    @DBRef
    private Set<Author> authors;

    @DBRef
    private Set<Comment> comments;

    public Book(String title, Genre genre, Set<Author> authors) {
        this.title = title;
        this.genre = genre;
        this.authors = authors;
    }

    public Book(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
    }
}
