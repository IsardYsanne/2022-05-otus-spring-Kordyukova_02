package ru.otus.library.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.library.model.entity.Author;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
public class AuthorRepositoryTest {

    private static final String TEST_NAME_1 = "testName";

    private static final String TEST_NAME_2 = "testName2";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private void saveTestAuthor(String testName) {
        final Author author = new Author();
        author.setName(testName);
        entityManager.persist(author);
    }

    @Test
    public void findAllAuthorsNamesTest() {
        List<String> authors = authorRepository.findAllAuthorsNames();
        assertThat(authors).isEmpty();

        saveTestAuthor(TEST_NAME_1);
        saveTestAuthor(TEST_NAME_2);

        authors = authorRepository.findAllAuthorsNames();

        assertThat(authors).hasSize(2).contains(TEST_NAME_1, TEST_NAME_2);
    }

    @Test
    public void findAuthorByNameTest() {
        saveTestAuthor(TEST_NAME_1);
        final Author author = authorRepository.findAuthorByName(TEST_NAME_1);
        assertThat(author.getName()).isEqualTo(TEST_NAME_1);
    }

    @Test
    public void saveNewAuthorTest() {
        saveTestAuthor(TEST_NAME_1);
        List<String> authors = authorRepository.findAllAuthorsNames();
        assertThat(authors).isNotEmpty().hasSize(1).contains(TEST_NAME_1);
    }

    @Test
    public void deleteAuthorTest() {
        saveTestAuthor(TEST_NAME_1);
        saveTestAuthor(TEST_NAME_2);

        List<String> authors = authorRepository.findAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        final Author author = authorRepository.findAuthorByName(TEST_NAME_1);
        authorRepository.delete(author);

        authors = authorRepository.findAllAuthorsNames();
        assertThat(authors).hasSize(1).contains(TEST_NAME_2).doesNotContain(TEST_NAME_1);
    }

    @Test
    public void deleteAllTest() {
        saveTestAuthor(TEST_NAME_1);
        saveTestAuthor(TEST_NAME_2);

        List<String> authors = authorRepository.findAllAuthorsNames();
        assertThat(authors)
                .hasSize(2)
                .contains(TEST_NAME_1, TEST_NAME_2);

        authorRepository.deleteAll();

        authors = authorRepository.findAllAuthorsNames();
        assertThat(authors).isEmpty();
    }
}
