package com.example.demo.controller;

import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import com.example.demo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "index";
    }

    @RequestMapping("/book/{isbn}")
    public String getBook(final Model model, @PathVariable(value="isbn") String isbn){
        BookEntity book = bookService.getBookByIsbn(isbn);
        System.out.println(book);
        model.addAttribute("book", book);
        return "book";
    }

}
