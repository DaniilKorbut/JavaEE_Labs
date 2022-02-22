package com.example.demo.repository;

import com.example.demo.dto.BookDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookRepository {
    private static final ArrayList<BookDto> books = new ArrayList<>(List.of(
            new BookDto("John Doe's Book", "456", "John Doe"),
            new BookDto("Bred Doe's Book", "457", "Bred Doe"),
            new BookDto("Michael George's Book", "458", "Michael George")
    ));

    public static void add(BookDto bookDto) {
        books.add(bookDto);
    }

    public static List<BookDto> getBooks() {
        return new ArrayList<>(books);
    }

    public static List<BookDto> getBooks(String query) {
        return books.stream()
                    .filter(bookDto -> bookDto.getTitle().contains(query) || bookDto.getIsbn().contains(query))
                    .collect(Collectors.toList());
    }
}
