package com.example.demo.controller;

import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import com.example.demo.db.UserEntity;
import com.example.demo.db.UserService;
import com.example.demo.dto.BookResponseDto;
import com.example.demo.utils.ISBNValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final UserService userService;

    @SneakyThrows
    @PostMapping("/book-create")
    public ResponseEntity<BookResponseDto> createBook(
            @Valid @RequestBody final BookEntity bookEntity
    ) {
        if (!ISBNValidator.isValidISBN(bookEntity.getIsbn())) {
            BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "objectName");
            bindingResult.addError(new FieldError("book", "isbn", "Invalid ISBN"));
            throw new MethodArgumentNotValidException(null, bindingResult);
        }
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
        if (query == null || query.equals("")) {
            response = bookService.getAllBooks(page);
        } else {
            response = bookService.findBooks(query, page);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/get-favourites")
    public ResponseEntity<Set<BookEntity>> getFavourites(Principal principal) {
        Set<BookEntity> response;
        try {
            response = userService.getFavourites(principal.getName());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/favourites/add")
    public ResponseEntity<String> addFavourites(@RequestParam(name = "isbn") String isbn, Principal principal) {
        Optional<UserEntity> userEntity = userService.getUserByUsername(principal.getName());
        if (userEntity.isPresent()) {
            BookEntity bookEntity = bookService.getBookByIsbn(isbn);
            if (bookEntity != null) {
                userService.addToFavourites(userEntity.get(), bookEntity);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(isbn + ": Added to favourites");
    }

    @PostMapping("/favourites/remove")
    public ResponseEntity<String> removeFavourites(@RequestParam(name = "isbn") String isbn, Principal principal) {
        Optional<UserEntity> userEntity = userService.getUserByUsername(principal.getName());
        if (userEntity.isPresent()) {
            BookEntity bookEntity = bookService.getBookByIsbn(isbn);
            if (bookEntity != null) {
                userService.removeFromFavourites(userEntity.get(), bookEntity);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(isbn + ": Removed from favourites");
    }

}
