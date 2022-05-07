package com.example.demo.dto;

import lombok.Data;

@Data(staticConstructor = "of")
public class UserResponseDto {

    private final String login;
    private final String message;

}
