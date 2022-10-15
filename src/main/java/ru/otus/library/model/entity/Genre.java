package ru.otus.library.model.entity;

public class Genre {

    private Long id;

    private String name;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        if (getId() != null ? !getId().equals(genre.getId()) : genre.getId() != null) return false;
        return getName() != null ? getName().equals(genre.getName()) : genre.getName() == null;
    }
}
