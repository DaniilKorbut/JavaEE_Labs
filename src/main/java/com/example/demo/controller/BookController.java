package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookResponseDto;
import com.example.demo.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("books", BookRepository.books);
        return "index";
    }

    @RequestMapping(value = "/book-create", method = RequestMethod.POST)
    public ResponseEntity<BookResponseDto> createBook(
            @RequestBody final BookDto bookDto
    ) {
        System.out.println("Accept add book request: " + bookDto);
        BookRepository.books.add(bookDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BookResponseDto.of(bookDto.getIsbn(), "Book created successfully"));
    }

    @ResponseBody
    @GetMapping("/get-books")
    public List<BookDto> getBooks(
            @RequestParam(name = "name", required = false) final String query
    ) {
        System.out.println("Accept get book request: " + (query == null ? "No query" : query));
        List<BookDto> response = null;
        if(query == null) {
            response = BookRepository.books;
        } else {
            response = BookRepository.books.stream()
                        .filter(bookDto -> bookDto.getTitle().contains(query) || bookDto.getIsbn().contains(query))
                        .collect(Collectors.toList());
        }
        return response;
    }

}
