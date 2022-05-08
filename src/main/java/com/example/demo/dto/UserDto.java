package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @NotEmpty(message = "Login can't be empty")
    @Pattern(regexp = "[A-Za-z\\d]+", message = "Login must contain only latin letters and numbers")
    @Length(max = 30, message = "Login can't be longer than 30 characters")
    private String login;

    @NotEmpty(message = "Password can't be empty")
    @Length(min = 8, max = 20, message = "Password should be from 8 to 20 characters")
    private String password;

}
