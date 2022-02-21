package com.example.demo.dto;

import lombok.Data;

@Data(staticConstructor = "of")
public class BookResponseDto {

    private final String isbn;
    private final String message;

}
