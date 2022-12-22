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
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.CommentService;
import ru.otus.library.service.GenreService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private final BookService bookService;

    private final AuthorService authorService;

    private final CommentService commentService;

    private final GenreService genreService;

    public LibraryCli(BookService bookService, AuthorService authorService, CommentService commentService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.commentService = commentService;
        this.genreService = genreService;
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

            String genre = book.getGenre();
            String genreName = "";
            if (genre != null) {
                genreName = genre;
            }

            modelBuilder.addRow()
                    .addValue(String.valueOf(book.getId()))
                    .addValue(authors.orElse("Автор не найден."))
                    .addValue(book.getTitle())
                    .addValue(genreName);
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
    public String updateBookTitleById(@ShellOption(help = "id") String id,
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
                              @ShellOption(help = "authors, use \",\" as delimiter ") String authorsNames) {

        final String[] authorsArr = authorsNames.split(",");
        final Set<Author> authorSet = new HashSet<>();
        for (String authorName : authorsArr) {
            final Author author = new Author(authorName);
            authorSet.add(author);
            authorService.saveAuthor(author);
        }
        final boolean isSuccessful = bookService.saveNewBook(new Book(title, genreName, authorSet));
        if (isSuccessful) {
            return "Новая книга была добавлена успешно.";
        } else {
            return "Такая книга уже существует.";
        }
    }

    @ShellMethod(value = "Delete book", key = "delete-book")
    public String deleteBookById(@ShellOption(help = "id of book to delete") String id) {
        bookService.deleteBookById(id);
         return "Книга успешно удалена.";
    }

    @ShellMethod(value = "Show all genres", key = "all-genres")
    public List<String> findAllGenres() {
        return genreService.findAllGenres();
    }

    @ShellMethod(value = "Add new genre", key = "add-genre")
    public String saveNewGenre(@ShellOption(help = "genre name") String name,
                               @ShellOption(help = "book id") String id) {
        genreService.saveGenre(name, id);
        return "Жанр был успешно добавлен.";
    }

    @ShellMethod(value = "Show all authors names", key = "all-authors-names")
    public List<String> findAllAuthorsNames() {
        return authorService.findAllAuthorsNames();
    }

    @ShellMethod(value = "Add new author", key = "add-author")
    public String saveNewAuthor(@ShellOption(help = "author name") String authorName) {
        authorService.saveAuthor(new Author(authorName));
        return "Автор успешно добавлен.";
    }

    @ShellMethod(value = "Update author", key = "update-author")
    public String updateAuthor(@ShellOption(help = "author id") String id,
                               @ShellOption(help = "new author name") String newAuthorName,
                               @ShellOption(help = "author id") String bookId) {
        authorService.updateAuthorById(id, newAuthorName, bookId);
        return "Автор успешно добавлен/обновлен.";
    }

    @ShellMethod(value = "Find comments by book id", key = "comment-book")
    public List<String> findAllCommentsForBook(@ShellOption(help = "book id") String bookId) {
        return commentService.findCommentsByBookId(bookId);
    }

    @ShellMethod(value = "Add new comment", key = "add-comment")
    public String saveNewCommentToBook(@ShellOption(help = "book id") String bookId,
                                       @ShellOption(help = "user name") String userName,
                                       @ShellOption(help = "comment") String comment) {
        final boolean isSuccessful = commentService.saveComment(bookId, userName, comment);
        if (isSuccessful) {
            return "Комментарий успешно добавлен.";
        } else {
            return "Книга с идентификатором, равным " + bookId + " не существует.";
        }
    }
}
