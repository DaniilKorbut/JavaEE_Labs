package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/books";
    }

    @GetMapping("/book-create")
    public String bookForm() {
        return "book-create";
    }

    @PostMapping("/book-create")
    public String saveBook(BookDto bookDto, Model model) {
        System.out.println("Save book: " + bookDto);
        BookRepository.books.add(bookDto);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("books", BookRepository.books);
        return "saved-books";
    }

}
