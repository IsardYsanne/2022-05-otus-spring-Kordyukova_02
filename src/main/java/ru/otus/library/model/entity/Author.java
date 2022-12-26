package ru.otus.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name")
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "authors")
    private Set<Book> books;

    public Author(String authorName) {
        this.name = authorName;
    }

    public Author(Set<Book> books, String name) {
        this.books = books;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        if (getId() != null ? !getId().equals(author.getId()) : author.getId() != null) return false;
        if (getBooks() != null ? !getBooks().equals(author.getBooks()) : author.getBooks() != null) return false;
        return getName() != null ? getName().equals(author.getName()) : author.getName() == null;
    }
}
