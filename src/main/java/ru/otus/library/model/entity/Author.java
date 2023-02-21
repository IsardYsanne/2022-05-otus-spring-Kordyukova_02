package ru.otus.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "authors")
public class Author {

    @Id
    private String id;

    private String name;

    public Author(String name) {
        this.name = name;
    }
}
