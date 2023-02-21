package ru.otus.library.controller;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.AutoSizeConstraints;
import org.springframework.shell.table.SimpleHorizontalAligner;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.library.model.entity.jpa.AuthorJpa;
import ru.otus.library.model.entity.jpa.BookJpa;
import ru.otus.library.model.entity.jpa.CommentJpa;
import ru.otus.library.model.entity.jpa.GenreJpa;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.CommentService;
import ru.otus.library.service.GenreService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private final BookService bookService;

    private final AuthorService authorService;

    private final CommentService commentService;

    private final GenreService genreService;

    private JobLauncher jobLauncher;

    private Job noSqlMigrationJob;

    public LibraryCli(BookService bookService,
                      AuthorService authorService,
                      CommentService commentService,
                      GenreService genreService,
                      JobLauncher jobLauncher,
                      Job noSqlMigrationJob) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.commentService = commentService;
        this.genreService = genreService;
        this.jobLauncher = jobLauncher;
        this.noSqlMigrationJob = noSqlMigrationJob;
    }

    /**
     * В MongoDB появятся данные из PostgresDB.
     */
    @ShellMethod(value = "Start migrate to Mongo", key = "migrate-to-mongo")
    public void migrateToMongo() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(noSqlMigrationJob, new JobParametersBuilder().toJobParameters());
    }

    private String getTableFromList(List<BookJpa> books) {
        final TableModelBuilder<String> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("Book ID")
                .addValue("Author(s)")
                .addValue("Title")
                .addValue("Genre");
        books.forEach(book -> {
            final Optional<String> authors = book.getAuthors().stream().map(AuthorJpa::getName).reduce((a, b) -> a + ", " + b);
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

    @ShellMethod(value = "Get book by title", key = "book-title")
    public BookJpa findBooksByTitle(@ShellOption(help = "book title") String title) {
        return bookService.findBooksByTitle(title).get(0);
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
        final Set<AuthorJpa> authorSet = Arrays.stream(authorsArr).map(AuthorJpa::new).collect(Collectors.toSet());
        final GenreJpa genre = new GenreJpa(genreName);
        final boolean isSuccessful = bookService.saveNewBook(new BookJpa(title, genre, authorSet));
        if (isSuccessful) {
            return "Новая книга была добавлена успешно.";
        } else {
            return "Такая книга уже существует.";
        }
    }

    @ShellMethod(value = "Delete book", key = "delete-book")
    public String deleteBookById(@ShellOption(help = "id of book to delete") Long id) {
        bookService.deleteBookById(id);
        return "Книга успешно удалена.";
    }

    @ShellMethod(value = "Add new genre", key = "add-genre")
    public String saveNewGenre(@ShellOption(help = "genre name") String name) {
        boolean isSuccessful = genreService.saveNewGenre(new GenreJpa(name));
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

    @ShellMethod(value = "Find genre by name", key = "genre-name")
    public String findGenreByName(@ShellOption(help = "genre name") String genreName) {
        final GenreJpa genre = genreService.findByName(genreName);
        return genre.getId() + " " + genre.getName();
    }

    @ShellMethod(value = "Delete genre", key = "delete-genre")
    public String deleteGenreByName(@ShellOption(help = "genre to delete") String genreName) {
        genreService.deleteGenre(genreName);
        return "Жанр успешно удален.";
    }

    @ShellMethod(value = "Add new author", key = "add-author")
    public String saveNewAuthor(@ShellOption(help = "author name") String authorName) {
        authorService.saveAuthor(new AuthorJpa(authorName));
        return "Автор успешно добавлен.";
    }

    @ShellMethod(value = "Show all authors names", key = "all-authors-names")
    public List<String> findAllAuthorsNames() {
        return authorService.findAllAuthorsNames();
    }

    @ShellMethod(value = "Find author by Id", key = "author-id")
    public String findAuthorById(@ShellOption(help = "author id") Long authorId) {
        final AuthorJpa author = authorService.findAuthorById(authorId);
        return author.getName();
    }

    @ShellMethod(value = "Find author by name", key = "author-name")
    public String findAuthorByName(@ShellOption(help = "author name") String authorName) {
        final AuthorJpa author = authorService.findAuthorByName(authorName);
        return author.getId() + " " + author.getName();
    }

    @ShellMethod(value = "Delete author", key = "delete-author")
    public String deleteAuthorById(@ShellOption(help = "delete author by id") Long id) {
        authorService.deleteAuthorById(id);
        return "Автор был успешно удален.";
    }

    @ShellMethod(value = "Find comments by book id", key = "comment-book")
    public List<String> findAllCommentsForBook(@ShellOption(help = "book id") Long bookId) {
        return commentService.findCommentsByBookId(bookId);
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
        final CommentJpa comment = new CommentJpa();
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
        commentService.deleteCommentById(id);
        return "Комментарий успешно удален.";
    }
}
