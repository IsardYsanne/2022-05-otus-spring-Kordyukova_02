package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.User;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.repository.UserRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<String> findCommentsByBookId(Long bookId) {
        return commentRepository.findCommentsByBookId(bookId);
    }

    @Override
    public List<Comment> findAllFullComments(Long id) {
        return commentRepository.findAllById(id);
    }

    @Override
    public Comment saveComment(Long bookId, String comment, String userName) {
        final Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new RuntimeException("Такой книги не существует.");
        }
        User user = userRepository.findUserByUserName(userName);
        if (user == null) {
            user = userRepository
                    .save(new User(userName, "$2y$10$QjL8S2KHO095gtMtxfoJ9OXmXj4q1mTohDS4c5EI2jkS9lVzXx2pG", "ROLE_USER"));
        }
        final Comment com = commentRepository.save(new Comment(book, comment, user));
        book.getComments().add(com);
        return com;
    }

    @Override
    public Comment updateComment(Comment comment) {
        final Comment commentToUpdate = commentRepository.findById(comment.getId()).orElse(null);
        if (commentToUpdate == null) {
            throw new RuntimeException("Такого комментария не существует.");
        }
        commentToUpdate.setCommentDate(comment.getCommentDate());
        commentToUpdate.setCommentText(comment.getCommentText());
        return commentToUpdate;
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
