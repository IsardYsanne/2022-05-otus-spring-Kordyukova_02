package ru.otus.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    private String authors;

    private String title;

    private String genre;

    private Set<CommentDto> comments;
}
