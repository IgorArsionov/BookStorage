package com.bookstorage.controller;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.util.TestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtil testUtil;

    @BeforeAll
    static void beforeAll(
            @Autowired
            DataSource dataSource,
            @Autowired
            WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-category-to-categories-table.sql")
            );

            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-books-to-book-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired
            DataSource dataSource
    ) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/delete-books-from-books-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/delete-category-from-categories-table.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    @Sql(scripts = "classpath:database/book/delet-special-book-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Ok() throws Exception {
        BookCreateRequestDto bookRequest = testUtil.initBookWhereAuthorTaras();
        bookRequest.setIsbn("123456789");

        BookResponseDto expected = testUtil.initBookResponseDtoWhereAuthorTaras();
        expected.setIsbn(bookRequest.getIsbn());

        String jsonRequest = objectMapper.writeValueAsString(bookRequest);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookResponseDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Get book by id 1")
    void getBookById_WithValidId1_Ok() throws Exception {
        BookResponseDto expected = new BookResponseDto();
        expected.setId(1L);
        expected.setTitle("Book One");
        expected.setAuthor("Author A");
        expected.setIsbn("111");
        expected.setPrice(new BigDecimal("10.99"));
        expected.setDescription("Book");
        expected.setCoverImage(null);

        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookResponseDto.class
        );

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual));
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Get book by id - not found")
    void getBookById_WithInvalidId1_NotFound() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Update book by id 1")
    void updateBook_WithValidId1_Ok() throws Exception {
        BookCreateRequestDto bookRequest = testUtil.initBookWhereAuthorTaras();
        BookResponseDto expected = testUtil.initBookResponseDtoWhereAuthorTaras();

        String jsonRequest = objectMapper.writeValueAsString(bookRequest);
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookResponseDto.class
        );

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id", "categoryIds"));
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Delete book by id 1")
    void deleteBook_WithValidId1_Ok() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @DisplayName("Create a new book with user role USER")
    void createBook_WithRoleUser_Forbidden() throws Exception {
        BookCreateRequestDto bookRequest = testUtil.initBookWhereAuthorTaras();

        String jsonRequest = objectMapper.writeValueAsString(bookRequest);

        mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @DisplayName("Delete a new book with user role USER")
    void deleteBook_WithRoleUser_Forbidden() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @DisplayName("Find all books")
    void getBooks_WithValidPageable_Ok() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

}
