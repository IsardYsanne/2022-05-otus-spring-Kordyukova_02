package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.otus.library.model.entity.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findAll();

    Book findBookById(String id);

    List<Book> findBooksByAuthors(String authorName);

    @Query(value = "{'title':'?0'}")
    List<Book> findBooksByTitle(String title);

    @Query(value = "{'id':'?0'}", fields = "{'comments': 1}")
    Book findBookWithCommentsById(String id);

    int deleteBookById(String id);
}
