package com.example.demo.controller;

import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping("/")
    public String index(Model model) {
        Page<BookEntity> page = bookService.getAllBooks(0);
        model.addAttribute("books", page.getContent());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        return "index";
    }

    @RequestMapping("/book/{isbn}")
    public String getBook(final Model model, @PathVariable(value="isbn") String isbn){
        BookEntity book = bookService.getBookByIsbn(isbn);
        System.out.println(book);
        if(book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book");
        }
        model.addAttribute("book", book);
        return "book";
    }

}
