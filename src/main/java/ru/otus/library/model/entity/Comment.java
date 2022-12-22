package ru.otus.library.model.entity;

import java.util.Date;

public class Comment {

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}
