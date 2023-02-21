package ru.otus.library.model.entity.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "book_comments")
@Getter
@Setter
public class CommentJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private BookJpa book;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "comment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

    public CommentJpa(BookJpa book, String commentText) {
        this.book = book;
        this.commentText = commentText;
        this.commentDate = new Date();
    }

    public CommentJpa() {
        commentDate = new Date();
    }
}
