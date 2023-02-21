package ru.otus.library.model.entity.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
public class GenreJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_name")
    private String name;

    @OneToMany(mappedBy = "genre")
    private Set<BookJpa> books;

    public GenreJpa(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreJpa genre = (GenreJpa) o;
        if (getId() != null ? !getId().equals(genre.getId()) : genre.getId() != null) return false;
        return getName() != null ? getName().equals(genre.getName()) : genre.getName() == null;
    }
}
