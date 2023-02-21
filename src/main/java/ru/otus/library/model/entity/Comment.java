package ru.otus.library.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String userName;

    private String commentText;

    private Date commentDate;

    public Comment(String userName, String comment) {
        this.userName = userName;
        commentText = comment;
        commentDate = new Date();
    }

    public Comment() {
        commentDate = new Date();
    }
}
