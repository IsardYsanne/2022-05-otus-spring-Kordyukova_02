package ru.otus.library.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"book"})
@Document(collection = "book_comments")
public class Comment {

    @Id
    private String id;

    @DBRef
    private Book book;

    private String commentText;

    private Date commentDate;

    public Comment(Book book, String commentText) {
        this.book = book;
        this.commentText = commentText;
        this.commentDate = new Date();
    }

    public Comment() {
        commentDate = new Date();
    }
}
