package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.CommentJpa;
import ru.otus.library.repository.jpa.BookRepositoryJpa;
import ru.otus.library.repository.jpa.CommentRepositoryJpa;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private BookRepositoryJpa bookRepository;

    private CommentRepositoryJpa commentRepository;

    public CommentServiceImpl(BookRepositoryJpa bookRepository, CommentRepositoryJpa commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<String> findCommentsByBookId(Long bookId) {
        return commentRepository.findCommentsByBookId(bookId);
    }

    @Override
    public boolean saveComment(Long bookId, String comment) {
        final BookJpa book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new RuntimeException("Такой книги не существует.");
        }

        final CommentJpa com = commentRepository.save(new CommentJpa(book, comment));
        book.getComments().add(com);
        return true;
    }

    @Transactional
    @Override
    public boolean updateComment(CommentJpa comment) {
        final CommentJpa commentToUpdate = commentRepository.findById(comment.getId()).orElse(null);
        if (commentToUpdate == null) {
            throw new RuntimeException("Такого комментария не существует.");
        }
        commentToUpdate.setCommentDate(comment.getCommentDate());
        commentToUpdate.setCommentText(comment.getCommentText());
        return true;
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}
