package ru.otus.library.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.AutoSizeConstraints;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.library.model.entity.Author;
import ru.otus.library.model.entity.Book;
import ru.otus.library.model.entity.Comment;
import ru.otus.library.model.entity.Genre;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.CommentService;
import ru.otus.library.service.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private final AuthorService authorService;

    private final BookService bookService;

    private final GenreService genreService;

    private final CommentService commentService;

    public LibraryCli(AuthorService authorService, BookService bookService, GenreService genreService, CommentService commentService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.genreService = genreService;
        this.commentService = commentService;
    }

    private String getTableFromList(List<Book> books) {
        final TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("Book ID")
                .addValue("Author(s)")
                .addValue("Title")
                .addValue("Genre");
        books.forEach(book -> {
            final Optional<String> authors = book.getAuthors().stream().map(Author::getName).reduce((a, b) -> a + ", " + b);
            modelBuilder.addRow()
                    .addValue(String.valueOf(book.getId()))
                    .addValue(authors.orElse("Автор не найден."))
                    .addValue(book.getTitle())
                    .addValue(book.getGenre().getName());
        });
        final TableModel model = modelBuilder.build();

        return new TableBuilder(model)
                .on(table())
                .addSizer(new AutoSizeConstraints())
                .addAligner(SimpleHorizontalAligner.left)
                .build()
                .render(500);
    }

    @ShellMethod(value = "Show all books", key = "all-books")
    public String findAllBooks() {
        return getTableFromList(bookService.findAllBooks());
    }

    @ShellMethod(value = "Update book title", key = "update-title-id")
    public String updateBookTitleById(@ShellOption(help = "id") Long id,
                                      @ShellOption(help = "new title") String newTitle) {
        boolean isSuccessful = bookService.updateBookTitleById(id, newTitle);
        if (isSuccessful) {
            return "Название книги успешно обновлено.";
        } else {
            return "Такой книги не существует.";
        }
    }

    @ShellMethod(value = "Get books by authors names", key = "book-of-author")
    public String findBooksByAuthorsName(@ShellOption(help = "authors name") String name) {
        return getTableFromList(bookService.findBooksByAuthorsName(name));
    }

    @ShellMethod(value = "Add new book, use \",\" as delimiter for authors", key = "add-book")
    public String saveNewBook(@ShellOption(help = "title") String title,
                              @ShellOption(help = "genre") String genreName,
                              @ShellOption(help = "authors, use \",\" as delimiter ") String authors) {

        final String[] authorsArr = authors.split(",");
        final Set<Author> authorSet = Arrays.stream(authorsArr).map(Author::new).collect(Collectors.toSet());
        final Genre genre = new Genre(genreName);
        final boolean isSuccessful = bookService.saveNewBook(new Book(title, genre, authorSet));
        if (isSuccessful) {
            return "Новая книга была добавлена успешно.";
        } else {
            return "Такая книга уже существует.";
        }
    }

    @ShellMethod(value = "Delete book", key = "delete-book")
    public String deleteBookById(@ShellOption(help = "id of book to delete") Long id) {
        boolean isSuccessful = bookService.deleteBookById(id);
        if (isSuccessful) {
            return "Книга успешно удалена.";
        } else {
            return "Нечего удалять.";
        }
    }

    @ShellMethod(value = "Add new genre", key = "add-genre")
    public String saveNewGenre(@ShellOption(help = "genre name") String name) {
        boolean isSuccessful = genreService.saveNewGenre(new Genre(name));
        if (isSuccessful) {
            return "Жанр был успешно добавлен.";
        } else {
            return "Такой жанр уже существует.";
        }
    }

    @ShellMethod(value = "Show all genres", key = "all-genres")
    public List<String> findAllGenres() {
        return genreService.findAllGenres();
    }

    @ShellMethod(value = "Delete genre", key = "delete-genre")
    public String deleteGenre(@ShellOption(help = "genre to delete") String genreName) {
        boolean isSuccessful = genreService.deleteGenre(genreName);
        if (isSuccessful) {
            return "Жанр успешно удален.";
        } else {
            return "Нечего удалять.";
        }
    }

    @ShellMethod(value = "Add new author", key = "add-author")
    public void saveNewAuthor(@ShellOption(help = "author name") String authorName) {
        authorService.saveAuthor(new Author(authorName));
    }

    @ShellMethod(value = "Show all authors names", key = "all-authors-names")
    public List<String> findAllAuthorsNames() {
        return authorService.findAllAuthorsNames();
    }


    @ShellMethod(value = "Delete author", key = "delete-author")
    public String deleteAuthorById(@ShellOption(help = "delete author by id") Long id) {
        boolean isSuccessful = authorService.deleteAuthorById(id);
        if (isSuccessful) {
            return "Автор был успешно удален.";
        } else {
            return "Нечего удалять.";
        }
    }

    @ShellMethod(value = "Find comments by book id", key = "comment-book")
    public List<String> findAllCommentsForBook(@ShellOption(help = "book id") Long bookId) {
        return commentService.findCommentsByBookId(bookId).stream().map(Comment::getCommentText).collect(Collectors.toList());
    }

    @ShellMethod(value = "Add new comment", key = "add-comment")
    public String saveNewCommentToBook(@ShellOption(help = "book id") Long bookId,
                                       @ShellOption(help = "comment") String comment) {
        final boolean isSuccessful = commentService.saveComment(bookId, comment);
        if (isSuccessful) {
            return "Комментарий успешно добавлен.";
        } else {
            return "Книга с идентификатором, равным " + bookId + " не существует.";
        }
    }

    @ShellMethod(value = "Update comment", key = "update-comment")
    public String updateCommentById(@ShellOption(help = "id") Long id,
                                    @ShellOption(help = "new comment") String newComment) {
        final Comment comment = new Comment();
        comment.setId(id);
        comment.setCommentText(newComment);
        final boolean isSuccessful = commentService.updateComment(comment);
        if (isSuccessful) {
            return "Комментарий успешно обновлен.";
        } else {
            return "Комментарий не существует.";
        }
    }

    @ShellMethod(value = "Delete comment", key = "del-comment")
    public String deleteCommentById(@ShellOption(help = "id of comment to delete") Long id) {
        final boolean isSuccessful = commentService.deleteCommentById(id);
        if (isSuccessful) {
            return "Комментарий успешно удален.";
        } else {
            return "Комментарий не существует.";
        }
    }
}
