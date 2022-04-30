package com.example.demo.db;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private static final int PAGE_SIZE = 10;

    @Transactional
    public BookEntity createBook(String title, String isbn, String author) {
        BookEntity book = new BookEntity();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setAuthor(author);
        return bookRepository.saveAndFlush(book);
    }

    @Transactional
    public BookEntity createBook(BookEntity bookEntity) {
        return bookRepository.saveAndFlush(bookEntity);
    }

    @Transactional
    public BookEntity getBookByIsbn(String isbn) {
        Optional<BookEntity> optionalBook = bookRepository.findById(isbn);

        return optionalBook
                .orElse(null);
    }

    @Transactional
    public Page<BookEntity> getAllBooks(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return bookRepository.findAll(pageable);
    }

    @Transactional
    public Page<BookEntity> findBooks(String query, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return bookRepository.findAllByIsbnLikeOrTitleLike('%' + query + '%' , '%' + query + '%', pageable);
    }
}
