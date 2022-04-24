package com.example.demo.controller;

import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import com.example.demo.dto.BookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<BookEntity>> getBooks(
            @RequestParam(name = "name", required = false) final String query
    ) {
        System.out.println("Accept get book request: " + (query == null ? "No query" : query));
        List<BookEntity> response;
        if(query == null) {
            response = bookService.getAllBooks();
        } else {
            response = bookService.findBooks(query);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
