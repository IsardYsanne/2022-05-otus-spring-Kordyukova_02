package ru.otus.library.service;

import org.springframework.stereotype.Service;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.repository.BookRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private BookRepository bookRepository;

    public CommentServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<String> findCommentsByBookId(String bookId) {
        return bookRepository.findBookWithCommentsById(bookId).getComments().stream()
                .map(Comment::getCommentText).collect(Collectors.toList());
    }

    @Override
    public boolean saveComment(String bookId, String userName, String comment) {
        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            return false;
        }

        Set<Comment> comments = book.getComments();
        if (comments == null) {
            comments = new HashSet<>();
        }
        comments.add(new Comment(userName, comment));
        book.setComments(comments);
        book = bookRepository.save(book);
        return book.getId() != null;
    }

    @Override
    public void deleteAll() {
        bookRepository.findAll().forEach(book -> book.setComments(new HashSet<>()));
    }
}
