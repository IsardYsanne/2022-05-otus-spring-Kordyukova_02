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
import ru.otus.library.model.entity.Genre;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.shell.table.CellMatchers.table;

@ShellComponent
public class LibraryCli {

    private final AuthorService authorService;

    private final BookService bookService;

    private final GenreService genreService;

    public LibraryCli(AuthorService authorService, BookService bookService, GenreService genreService) {
        this.authorService = authorService;
        this.bookService = bookService;
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
    public String getAllBooks() {
        return getTableFromList(bookService.getAllBooks());
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
    public String getBooksByAuthorsName(@ShellOption(help = "authors name") String name) {
        return getTableFromList(bookService.getBooksByAuthorsName(name));
    }

    @ShellMethod(value = "Add new book, use \",\" as delimiter for authors", key = "add-book")
    public String addNewBook(@ShellOption(help = "title") String title,
                             @ShellOption(help = "genre") String genreName,
                             @ShellOption(help = "authors, use \",\" as delimiter ") String authors) {

        final String[] authorsArr = authors.split(",");
        final List<Author> authorList = Arrays.stream(authorsArr).map(Author::new).collect(Collectors.toList());
        final Genre genre = new Genre(genreName);
        final boolean isSuccessful = bookService.addNewBook(new Book(title, genre, authorList));
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
    public String addNewGenre(@ShellOption(help = "genre name") String genre) {
        boolean isSuccessful = genreService.addNewGenre(new Genre(genre));
        if (isSuccessful) {
            return "Жанр был успешно добавлен.";
        } else {
            return "Такой жанр уже существует.";
        }
    }

    @ShellMethod(value = "Show all genres", key = "all-genres")
    public List<String> getAllGenres() {
        return genreService.getAllGenres();
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
    public void addNewAuthor(@ShellOption(help = "author name") String authorName) {
        authorService.saveAuthor(new Author(authorName));
    }

    @ShellMethod(value = "Show all authors names", key = "all-authors-names")
    public List<String> getAllAuthorsNames() {
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
}
