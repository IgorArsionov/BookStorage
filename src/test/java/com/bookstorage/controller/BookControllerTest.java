package com.bookstorage.controller;

import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void createBook_CrateNewBook_ReturnsThisBook() throws Exception {
        BookCreateRequestDto bookRequest = new BookCreateRequestDto();
        bookRequest.setAuthor("Taras Shevchenko");
        bookRequest.setTitle("Kobzar");
        bookRequest.setIsbn("0123456789");
        bookRequest.setPrice(new BigDecimal("199.99"));
        bookRequest.setCategoryIds(Set.of(100L));

        BookResponseDto expected = new BookResponseDto();
        expected.setAuthor(bookRequest.getAuthor());
        expected.setTitle(bookRequest.getTitle());
        expected.setIsbn(bookRequest.getIsbn());
        expected.setPrice(bookRequest.getPrice());
        expected.setCategoryIds(bookRequest.getCategoryIds());

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

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Get book by id 1")
    void getBookById_GetBookWithId1_ReturnsBook() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookResponseDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.getId());
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Get book by id - not found")
    void getBookById_GetBookWithId1_Returns404() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Update book by id 1")
    void updateBook_UpdateBookById1_SuccessUpdateBook() throws Exception {
        BookCreateRequestDto bookRequest = new BookCreateRequestDto();
        bookRequest.setAuthor("Taras Shevchenko");
        bookRequest.setTitle("Kobzar");
        bookRequest.setIsbn("111");
        bookRequest.setPrice(new BigDecimal("199.99"));
        bookRequest.setCategoryIds(Set.of(100L));

        BookResponseDto expected = new BookResponseDto();
        expected.setAuthor(bookRequest.getAuthor());
        expected.setTitle(bookRequest.getTitle());
        expected.setIsbn(bookRequest.getIsbn());
        expected.setPrice(bookRequest.getPrice());

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

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id", "categoryIds");
    }

    @Test
    @WithMockUser(username = "admin@admim.com", roles = {"ADMIN"})
    @DisplayName("Delete book by id 1")
    void deleteBookById_DeleteBookWithId1_SuccessDelete() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@user.com", roles = {"USER"})
    @DisplayName("Create a new book with user role USER")
    void createBook_WithRoleUser_ReturnsForbiddenStatus() throws Exception {
        BookCreateRequestDto bookRequest = new BookCreateRequestDto();
        bookRequest.setAuthor("Taras Shevchenko");
        bookRequest.setTitle("Kobzar");
        bookRequest.setIsbn("0123456789");
        bookRequest.setPrice(new BigDecimal("199.99"));
        bookRequest.setCategoryIds(Set.of(100L));

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
    void deleteBook_WithRoleUser_ReturnsForbiddenStatus() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }

}
