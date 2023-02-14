package ru.otus.library.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.service.CommentService;

@CrossOrigin
@RestController
@RequestMapping("/comments")
@Slf4j
@Api(tags = {"Контроллер для комментариев"})
public class CommentController {

    private final CommentService commentService;

    private final BookMapper bookMapper;

    public CommentController(CommentService commentService, BookMapper bookMapper) {
        this.commentService = commentService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/{bookId}/comment")
    public Flux<CommentDto> showCommentsForBookId(@PathVariable(name = "bookId") String bookId) {
        return commentService.findAllComments(bookId).map(BookMapper::commentToDto);
    }

    @PostMapping("/save")
    public Mono<CommentDto> saveNewComment(@RequestBody String text, @RequestParam(name = "id") String bookId) {
        return commentService.saveComment(bookId, text).map(BookMapper::commentToDto);
    }
}
