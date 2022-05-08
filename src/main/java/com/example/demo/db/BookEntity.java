package com.example.demo.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookEntity {

    @Column(name = "title")
    @NotEmpty(message = "Title can't be empty")
    @Length(max = 255, message = "Title is too long")
    private String title;

    @Id
    @Column(name = "isbn")
    @NotEmpty(message = "ISBN can't be empty")
    @Pattern(regexp = "[\\d]{13}", message = "ISBN should consists of 13 digits")
    private String isbn;

    @Column(name = "author")
    @NotEmpty(message = "Author can't be empty")
    @Length(max = 255, message = "Author is too long")
    private String author;
}
