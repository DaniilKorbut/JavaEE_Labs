package com.example.demo.controller;

import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("books", BookRepository.getBooks());
        return "index";
    }

}
