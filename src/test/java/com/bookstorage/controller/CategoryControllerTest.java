package com.bookstorage.controller;

import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
            ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

    }

    @Test
    @DisplayName("Create new category")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    void createCategory_NewCategory_ReturnCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Description test category");

        CategoryResponseDto expected = new CategoryResponseDto(
                1L,
                requestDto.getName(),
                requestDto.getDescription()
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        Assertions.assertEquals(expected.name(), actual.name());
        Assertions.assertEquals(expected.description(), actual.description());
    }

    @Test
    @DisplayName("get Category by id 1")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/category/delete-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getCategory_byId1_ReturnCategory() throws Exception {
        int expected = 1;

        MvcResult result = mockMvc.perform(get("/categories/" + expected))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual.id());
    }

    @Test
    @DisplayName("delete Category by id 1")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/category/add-category-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/category/delete-category-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void deleteCategory_byId1_ReturnsStatusOk() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get book by Category")
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    @Sql(
            scripts = {"classpath:database/category/add-category-to-categories-table.sql",
            "classpath:database/category/add-books-to-book-table.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {"classpath:database/category/delete-books-from-books-table.sql",
            "classpath:database/category/delete-category-from-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getBookByCategoryId_CategoryId1_ReturnListBooks() throws Exception {
        List<BookResponseDto> expected = new ArrayList<>();
        expected.add(new BookResponseDto().setId(1L).setTitle("Book One")
                .setAuthor("Author A").setIsbn("111").setPrice(new BigDecimal("10.99"))
                .setDescription("Book").setCategoryIds(Set.of(1L)));
        expected.add(new BookResponseDto().setId(2L).setTitle("Book Two")
                .setAuthor("Author B").setIsbn("222").setPrice(new BigDecimal("15.50"))
                .setDescription("Book").setCategoryIds(Set.of(1L)));

        MvcResult result = mockMvc.perform(
                get("/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookResponseDto[].class
        );

        Assertions.assertEquals(expected.size(), actual.length);

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    EqualsBuilder.reflectionEquals(expected.get(i), actual[i],
                            "id", "categoryIds"),
                    "Book at index " + i + " does not match"
            );
        }
    }
}
