package ru.otus.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.library.model.entity.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c.commentText FROM Comment c WHERE c.book.id = :bookId")
    List<String> findCommentsByBookId(@Param(value = "bookId") Long bookId);

    List<Comment> findAllById(Long id);
}
