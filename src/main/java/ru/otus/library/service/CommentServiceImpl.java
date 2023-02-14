package ru.otus.library.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Flux<String> findAllCommentsTexts(String bookId) {
        return commentRepository.findCommentsByBookId(bookId).map(Comment::getCommentText);
    }

    @Override
    public Flux<Comment> findAllComments(String id) {
        return commentRepository.findCommentsByBookId(id);
    }

    @Override
    public Mono<Comment> saveComment(String bookId, String comment) {
        Mono<Book> bookMono = bookRepository.findBookById(bookId).map(Book::new);
        return commentRepository.save(new Comment(bookMono.block(), comment));
    }

    @Override
    public Mono<Comment> updateComment(Mono<Comment> comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Mono<Comment> updateComment(String id, String newComment) {
        return commentRepository.findById(id)
                .flatMap(comm -> {
                    comm.setCommentText(newComment);
                    return Mono.just(comm);
                })
                .flatMap(commentRepository::save)
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    @Override
    public Mono<Long> deleteCommentById(String id) {
        return commentRepository.deleteCommentById(id);
    }

    @Override
    public void deleteAll() {
        commentRepository.deleteAll();
    }
}
