package com.example.demo.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final EntityManager entityManager;

    @Transactional
    public BookEntity createBook(String title, String isbn, String author) {
        BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setAuthor(author);
        return entityManager.merge(book);
    }

    @Transactional
    public BookEntity createBook(BookEntity bookEntity) {
        return entityManager.merge(bookEntity);
    }

    @Transactional
    public BookEntity getBookByIsbn(String isbn) {
        return entityManager.find(BookEntity.class, isbn);
    }

    @Transactional
    public List<BookEntity> getAllBooks() {
        return entityManager.createQuery("SELECT b FROM BookEntity b", BookEntity.class)
                .getResultList();
    }

    @Transactional
    public List<BookEntity> findBooks(String query) {
        return entityManager.createQuery("SELECT b FROM BookEntity b WHERE b.title LIKE :query OR b.isbn LIKE :query OR b.author LIKE :query", BookEntity.class)
                .setParameter("query", '%' + query + '%')
                .getResultList();
    }
}
