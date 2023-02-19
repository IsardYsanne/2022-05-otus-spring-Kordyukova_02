package ru.otus.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "books_authors", joinColumns = @JoinColumn(name = "books_id"),
            inverseJoinColumns = @JoinColumn(name = "authors_id"))
    private Set<Author> authors;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    private Set<Comment> comments;

    private byte[] image;

    public Book(String title, Genre genre, Set<Author> authors, byte[] image) {
        this.title = title;
        this.genre = genre;
        this.authors = authors;
        this.image = image;
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
