package mmosii.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mmosii.bookstore.dto.category.CategoryDto;
import mmosii.bookstore.dto.category.CreateCategoryRequestDto;
import mmosii.bookstore.exception.EntityNotFoundException;
import mmosii.bookstore.mapper.CategoryMapper;
import mmosii.bookstore.model.Category;
import mmosii.bookstore.repository.book.CategoryRepository;
import mmosii.bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Save new category with valid CreateCategoryRequestDto")
    void save_validRequestDto_returnsDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Historic", "some desc");

        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        CategoryDto categoryDto = new CategoryDto(1L,
                category.getName(),
                category.getDescription());

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(requestDto);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Update existing category with valid CreateCategoryRequestDto")
    void update_validRequestDto_returnsDto() {
        Long id = 1L;

        Category category = new Category();
        category.setName("dramatic");
        category.setId(id);

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Historic", "some desc");

        Category requestCategory = new Category();
        requestCategory.setId(id);
        requestCategory.setName(requestDto.name());
        requestCategory.setDescription(requestDto.description());

        CategoryDto categoryDto = new CategoryDto(1L,
                requestCategory.getName(),
                requestCategory.getDescription());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategory(requestDto, category);
        when(categoryRepository.save(category)).thenReturn(requestCategory);
        when(categoryMapper.toDto(requestCategory)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(id, requestDto);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Update not existing category")
    void update_notValidRequestDto_throwsException() {
        Long id = 100L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Historic", "some desc");

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.update(id, requestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Get category by valid id")
    void getById_validId_returnsValidDto() {
        Category category = new Category();
        category.setName("dramatic");
        category.setId(1L);

        CategoryDto categoryDto = new CategoryDto(category.getId(),
                category.getName(),
                category.getDescription());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(1L);

        assertThat(actual).isEqualTo(categoryDto);
    }

    @Test
    @DisplayName("Get category by not existing id")
    void getById_notValidId_throwsException() {
        Long id = 100L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Delete category by valid id")
    void deleteById_validId() {
        Long id = 1L;
        doNothing().when(categoryRepository).deleteById(id);

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(categoryRepository);
    }
}
