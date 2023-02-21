package ru.otus.library.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.mapper.BookMapper;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.service.CommentService;

import java.util.List;

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

    @GetMapping("/show_all")
    public List<CommentDto> showCommentsForBookId(@RequestParam(name = "id") Long id) {
        return bookMapper.commentListToDto(commentService.findAllFullComments(id));
    }

    @PostMapping("/save")
    public ResponseEntity<CommentDto> saveNewComment(@RequestBody String text,
                                                     @RequestParam(name = "id") Long bookId,
                                                     String userName) {
        final Comment comment = commentService.saveComment(bookId, text, userName);
        return comment.getId() != null ?
                new ResponseEntity<>(bookMapper.commentToDto(comment), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
