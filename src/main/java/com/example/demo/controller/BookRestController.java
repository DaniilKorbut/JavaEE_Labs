package com.example.demo.controller;

import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import com.example.demo.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    @PostMapping( "/book-create")
    public ResponseEntity<BookResponseDto> createBook(
            @RequestBody final BookEntity bookEntity
    ) {
        System.out.println("Accept add book request: " + bookEntity);
        BookEntity createdBook = bookService.createBook(bookEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookResponseDto.of(createdBook.getIsbn(), "Book created/updated successfully"));
    }

    @GetMapping("/get-books")
    public ResponseEntity<Page<BookEntity>> getBooks(
            @RequestParam(name = "name", required = false) final String query, @RequestParam(name = "page", required = false, defaultValue = "0") final int page
    ) {
        System.out.println("Accept get book request: " + (query == null ? "No query" : query));
        Page<BookEntity> response;
        if(query == null || query.equals("")) {
            response = bookService.getAllBooks(page);
        } else {
            response = bookService.findBooks(query, page);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
