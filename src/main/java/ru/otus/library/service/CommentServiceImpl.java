package ru.otus.library.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Set<Comment> findCommentsByBookId(Long bookId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        final Book book = entityManager.find(Book.class, bookId);
        return book.getComments();
    }

    @Transactional
    @Override
    public boolean saveComment(Long bookId, String comment) {
        final Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            throw new RuntimeException("Такой книги не существует.");
        }

        final Comment com = commentRepository.saveComment(new Comment(book, comment));
        book.getComments().add(com);
        return true;
    }

    @Transactional
    @Override
    public boolean updateComment(Comment comment) {
        final Comment commentToUpdate = commentRepository.findCommentById(comment.getId());
        if (commentToUpdate == null) {
            throw new RuntimeException("Такого комментария не существует.");
        }
        commentToUpdate.setCommentDate(comment.getCommentDate());
        commentToUpdate.setCommentText(comment.getCommentText());
        return true;
    }

    @Transactional
    @Override
    public boolean deleteCommentById(Long id) {
        return commentRepository.deleteCommentById(id);
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}
