package ru.otus.library.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.library.model.entity.jpa.CommentJpa;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<CommentJpa, Long> {

    @Query("SELECT c.commentText FROM CommentJpa c WHERE c.book.id = :bookId")
    List<String> findCommentsByBookId(@Param(value = "bookId") Long bookId);
}
