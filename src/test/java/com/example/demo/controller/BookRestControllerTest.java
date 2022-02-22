package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookResponseDto;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateBook() throws Exception {
        String request = new String(BookRestControllerTest.class.getResourceAsStream("/book_create_request.json").readAllBytes());
        String response = new String(BookRestControllerTest.class.getResourceAsStream("/book_create_response.json").readAllBytes());
        System.out.println(request);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .json(response));
    }

    @Test
    void shouldNotCreateBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post("/book-create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid-request-body")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldGetBooks() throws Exception {
        BookRepository.add(new BookDto("Title", "ISBN", "Author"));
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/get-books")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(mvcResult.getResponse().getContentAsByteArray());
        assertThat(node.isArray()).isTrue();

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/get-books")
                        .queryParam("name", "somename")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        node = objectMapper.readTree(mvcResult.getResponse().getContentAsByteArray());
        assertThat(node.isArray()).isTrue();
    }

}