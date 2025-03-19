package org.burgas.backendserver.controller;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static java.lang.System.out;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
    void getAllCategories() throws Exception {
        mockMvc
                .perform(get("/categories"))
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(new String(result.getResponse().getContentAsByteArray())))
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
    void getCategoryById() throws Exception {
        mockMvc
                .perform(
                        get("/categories/by-id")
                                .param("categoryId", "5")
                )
                .andExpect(status().isOk())
                .andDo(result -> result.getResponse().setContentType(APPLICATION_JSON_VALUE))
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> out.println(new String(result.getResponse().getContentAsByteArray())))
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
    void createCategory() throws Exception {
        @Language("JSON") String content = """
                {
                    "name": "New Category",
                    "description": "New description for category"
                }""";
        mockMvc
                .perform(
                        post("/categories/create")
                                .contentType(APPLICATION_JSON_VALUE)
                                .content(content.getBytes(StandardCharsets.UTF_8))
                )
                .andExpect(status().is3xxRedirection())
                .andDo(result -> result.getResponse().setCharacterEncoding("UTF-8"))
                .andDo(result -> result.getResponse().setContentType(APPLICATION_JSON_VALUE))
                .andDo(result -> out.println(new String(result.getResponse().getContentAsByteArray())))
                .andReturn();
    }
}