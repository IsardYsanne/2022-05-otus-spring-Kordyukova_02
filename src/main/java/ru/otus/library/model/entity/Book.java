package ru.otus.library.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private String genre;

    private Set<Comment> comments;

    private Set<Author> authors;

    public Book(String title, String genre, Set<Author> authorList) {
        this.authors = authorList;
        this.genre = genre;
        this.title = title;
    }

    public Book() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;

        if (getId() != null ? !getId().equals(book.getId()) : book.getId() != null) return false;
        if (getTitle() != null ? !getTitle().equals(book.getTitle()) : book.getTitle() != null) return false;
        return getGenre() != null ? getGenre().equals(book.getGenre()) : book.getGenre() == null;
    }
}
