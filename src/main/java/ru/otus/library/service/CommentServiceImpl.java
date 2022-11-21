package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<String> findCommentsByBookId(Long bookId) {
        return commentRepository.findCommentsByBookId(bookId);
    }

    @Override
    public boolean saveComment(Long bookId, String comment) {
        final Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new RuntimeException("Такой книги не существует.");
        }

        final Comment com = commentRepository.save(new Comment(book, comment));
        book.getComments().add(com);
        return true;
    }

    @Transactional
    @Override
    public boolean updateComment(Comment comment) {
        final Comment commentToUpdate = commentRepository.findById(comment.getId()).orElse(null);
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
