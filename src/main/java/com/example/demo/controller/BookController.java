package com.example.demo.controller;

import com.example.demo.config.MyPasswordEncoder;
import com.example.demo.db.BookEntity;
import com.example.demo.db.BookService;
import com.example.demo.db.UserEntity;
import com.example.demo.db.UserService;
import com.example.demo.dto.UserDto;
import com.example.demo.type.UserAlreadyExistAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final HttpServletRequest servletRequest;
    private final MyPasswordEncoder myPasswordEncoder;

    @RequestMapping("/")
    public String index(Model model) {
        Page<BookEntity> page = bookService.getAllBooks(0);
        model.addAttribute("books", page.getContent());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        return "index";
    }

    @RequestMapping("/book/{isbn}")
    public String getBook(final Model model, @PathVariable(value = "isbn") String isbn, Principal principal) {
        BookEntity book = bookService.getBookByIsbn(isbn);
        System.out.println(book);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book");
        }
        if (principal != null) {
            String username = principal.getName();
            Optional<UserEntity> myUser = null;
            try {
                myUser = userService.getUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
            }
            if (myUser != null && myUser.isPresent()) {
                boolean isFavourite = userService.isUserFavourite(myUser.get(), book);
                model.addAttribute("isFavourite", isFavourite);
            } else {
                model.addAttribute("isFavourite", false);
            }
        }
        model.addAttribute("book", book);
        return "book";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(final Model model, @RequestParam(name = "alreadyExists", required = false) String alreadyExists, @RequestParam(name = "invalidData", required = false) String invalidData) {
        model.addAttribute("isAlreadyExists", !(alreadyExists == null));
        model.addAttribute("isInvalidData", !(invalidData == null));
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(@Valid UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .login(userDto.getLogin())
                .password(myPasswordEncoder.encode(userDto.getPassword()))
                .build();

        userService.registerUser(userEntity);

        try {
            servletRequest.login(userDto.getLogin(), userDto.getPassword());
        } catch (ServletException e) {
            e.printStackTrace();
            return "redirect:/login";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/favourites", method = RequestMethod.GET)
    public String favourites(Model model, Principal principal) {
        Set<BookEntity> response;
        try {
            response = userService.getFavourites(principal.getName());
            model.addAttribute("books", response);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return "favourites";
    }
}
