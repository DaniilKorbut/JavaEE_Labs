package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookResponseDto;
import com.example.demo.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookRestController {

    @PostMapping( "/book-create")
    public ResponseEntity<BookResponseDto> createBook(
            @RequestBody final BookDto bookDto
    ) {
        System.out.println("Accept add book request: " + bookDto);
        BookRepository.add(bookDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookResponseDto.of(bookDto.getIsbn(), "Book created successfully"));
    }

    @GetMapping("/get-books")
    public ResponseEntity<List<BookDto>> getBooks(
            @RequestParam(name = "name", required = false) final String query
    ) {
        System.out.println("Accept get book request: " + (query == null ? "No query" : query));
        List<BookDto> response;
        if(query == null) {
            response = BookRepository.getBooks();
        } else {
            response = BookRepository.getBooks(query);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
