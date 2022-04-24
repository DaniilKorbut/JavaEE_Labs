package com.example.demo.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookEntity {

    @Column(name = "title")
    private String title;

    @Id
    @Column(name = "isbn")
    private String isbn;

    @Column(name = "author")
    private String author;
}
