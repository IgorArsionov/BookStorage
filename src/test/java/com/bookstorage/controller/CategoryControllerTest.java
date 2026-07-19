package com.bookstorage.controller;

import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.dto.category.CategoryRequestDto;
import com.bookstorage.dto.category.CategoryResponseDto;
import com.bookstorage.util.TestUtil;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestUtil testUtil;

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
    @Sql(
            scripts = "classpath:database/category/delete-category-by-name.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createCategory_WithValidRequest_Ok() throws Exception {
        CategoryRequestDto requestDto = testUtil.initCategoryRequest();
        CategoryResponseDto expected = testUtil.initCategoryResponse(null);

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

        assertNotNull(actual);
        assertNotNull(actual.id());
        assertTrue(reflectionEquals(expected, actual, "id"));
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
    void getCategory_WithValidId1_Ok() throws Exception {
        Long categoryId = 1L;
        CategoryResponseDto expected = testUtil.initCategoryResponse(categoryId);

        MvcResult result = mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryResponseDto.class
        );

        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
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
    void deleteCategory_WithValidId1_Ok() throws Exception {
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
    void getBookByCategory_WithValidCategoryId1_Ok() throws Exception {
        List<BookResponseDto> expected = testUtil.initListBookResponseDto();

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

        assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(
                    reflectionEquals(expected.get(i), actual[i],
                            "id", "categoryIds"),
                    "Book at index " + i + " does not match"
            );
        }
    }
}
