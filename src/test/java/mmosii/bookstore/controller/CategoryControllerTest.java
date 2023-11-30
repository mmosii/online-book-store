package mmosii.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mmosii.bookstore.dto.category.CategoryDto;
import mmosii.bookstore.dto.category.CreateCategoryRequestDto;
import mmosii.bookstore.repository.book.CategoryRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:database/category/add-categories-input-data.sql")
@Sql(scripts = "classpath:database/category/delete-categories-table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest {
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Create category with valid dto")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void createCategory_validRequestDto_returnsDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Comedy", "some desc");

        MvcResult result = mockMvc.perform(post("/api/categories/")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("name", requestDto.name())
                .hasFieldOrPropertyWithValue("description", requestDto.description())
                .hasFieldOrPropertyWithValue("id", 3L);
    }

    @Test
    @DisplayName("Get category by valid id")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void getCategoryById_validId_returnsDto() throws Exception {
        Long id = 1L;

        CategoryDto expected = new CategoryDto(1L, "fantasy", null);

        MvcResult result = mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", expected.id())
                .hasFieldOrPropertyWithValue("name", expected.name());
    }

    @Test
    @DisplayName("Delete category by valid id")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void deleteCategoryById_validId() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(categoryRepository.findById(id)).isEmpty();
    }
}
