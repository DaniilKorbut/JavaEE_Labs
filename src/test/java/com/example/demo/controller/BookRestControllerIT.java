package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookRestControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @LocalServerPort
    void setPort(int port) {
        RestAssured.port = port;
    }

    @Test
    @SneakyThrows
    void shouldCreateBook() {
        String request = new String(BookRestControllerIT.class.getResourceAsStream("/book_create_request.json").readAllBytes());
        BookDto bookDto = objectMapper.readValue(request, BookDto.class);

        RestAssured
                .given()
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/book-create")
                .then()
                    .statusCode(201)
                    .body("isbn", CoreMatchers.is(bookDto.getIsbn()))
                    .body("message", CoreMatchers.is("Book created successfully"));
    }

    @Test
    @SneakyThrows
    void shouldGetBooks() {
        String body =
            RestAssured
                    .given()
                        .contentType(ContentType.JSON)
                    .when()
                        .get("/get-books")
                    .then()
                        .statusCode(200)
                        .extract().body().asString();

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.isArray()).isTrue();
    }

    @Test
    @SneakyThrows
    void shouldGetBooksWithParams() {
        String body =
                RestAssured
                        .given()
                            .contentType(ContentType.JSON)
                            .queryParam("name", "Name")
                        .when()
                            .get("/get-books")
                        .then()
                            .statusCode(200)
                            .extract().body().asString();

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.isArray()).isTrue();
    }

    @Test
    @SneakyThrows
    void shouldGetBooksWithRedundantParams() {
        String body =
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .queryParam("name", "Name")
                        .queryParam("nonexistentparam", "Value")
                        .when()
                        .get("/get-books")
                        .then()
                        .statusCode(200)
                        .extract().body().asString();

        JsonNode node = objectMapper.readTree(body);
        assertThat(node.isArray()).isTrue();
    }

}
