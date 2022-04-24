package com.example.demo.db;

import com.example.demo.MySpringTestListeners;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MySpringTestListeners
@SpringBootTest
public class BookServiceTests {

    @Autowired
    private BookService bookService;

    @Test
    @Sql(value = "/BookService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql("/BookService/init.sql")
    void shouldCreateBook() {
        bookService.createBook("Fourth Book", "13", "Fourth Author");
        assertThat(bookService.getAllBooks())
                .hasSize(4);
    }

    @Test
    @Sql(value = "/BookService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql("/BookService/init.sql")
    void shouldGetBookByIsbn() {
        assertThat(bookService.getBookByIsbn("11"))
                .returns("Second Book", BookEntity::getTitle)
                .returns("11", BookEntity::getIsbn)
                .returns("Second Author", BookEntity::getAuthor);
    }

    @Test
    @Sql(value = "/BookService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql("/BookService/init.sql")
    void shouldGetAllBooks() {
        assertThat(bookService.getAllBooks())
                .hasSize(3);
    }

    @Test
    @Sql(value = "/BookService/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql("/BookService/init.sql")
    void shouldFindBooksByQuery() {
        List<BookEntity> results = bookService.findBooks("Second Book");
        assertThat(results)
                .hasSize(1);
        assertThat(results.get(0))
                .returns("Second Book", BookEntity::getTitle)
                .returns("11", BookEntity::getIsbn)
                .returns("Second Author", BookEntity::getAuthor);

        results = bookService.findBooks("Author");
        assertThat(results)
                .hasSize(3);

        results = bookService.findBooks("12");
        assertThat(results)
                .hasSize(1);
        assertThat(results.get(0))
                .returns("Third Book", BookEntity::getTitle)
                .returns("12", BookEntity::getIsbn)
                .returns("Third Author", BookEntity::getAuthor);

    }

    @Test
    @DatabaseSetup("/BookService/init.xml")
    @DatabaseTearDown("/BookService/clean-up.xml")
    @ExpectedDatabase(value = "/BookService/expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    void shouldCreateBook_dbunit() {
        bookService.createBook("Fourth Book", "13", "Fourth Author");
    }

    @Test
    @DatabaseSetup("/BookService/init.xml")
    @DatabaseTearDown("/BookService/clean-up.xml")
    void shouldGetBookByIsbn_dbunit() {
        assertThat(bookService.getBookByIsbn("11"))
                .returns("Second Book", BookEntity::getTitle)
                .returns("11", BookEntity::getIsbn)
                .returns("Second Author", BookEntity::getAuthor);
    }

    @Test
    @DatabaseSetup("/BookService/init.xml")
    @DatabaseTearDown("/BookService/clean-up.xml")
    void shouldGetAllBooks_dbunit() {
        assertThat(bookService.getAllBooks())
                .hasSize(3);
    }

}
