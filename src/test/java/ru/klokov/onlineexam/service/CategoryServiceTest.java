package ru.klokov.onlineexam.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.klokov.onlineexam.exception.ResourceAlreadyExistsException;
import ru.klokov.onlineexam.exception.ResourceNotFoundException;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.repository.CategoryRepository;
import ru.klokov.onlineexam.service.impl.CategoryServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category;

    @BeforeEach
    public void setup() {
        category = Category.builder().id(1L).name("category").description("description").build();
    }

    @Test
    public void CategoryService_GetAllCategories_ReturnAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        List<Category> expectedCategories = categoryService.getAllCategories();

        Assertions.assertThat(expectedCategories).isNotNull();
        Assertions.assertThat(expectedCategories.size()).isEqualTo(1);
        Assertions.assertThat(expectedCategories.get(0).getName()).isEqualTo(category.getName());
    }

    @Test
    public void CategoryService_GetAllCategoriesPaginated_ReturnAllCategoriesPaginated() {
        when(categoryRepository.findAll(Mockito.any(Pageable.class))).thenReturn(Mockito.mock(Page.class));

        Page<Category> expectedPage = categoryService.getAllCategoriesPaginated(1, 5, "id", "asc");

        Assertions.assertThat(expectedPage).isNotNull();
    }

    @Test
    public void CategoryService_GetCategoryById_ReturnCategoryWithGivenId() {
        when(categoryRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(category));

        Category expectedCategory = categoryService.getCategoryById(category.getId());

        Assertions.assertThat(expectedCategory).isNotNull();
        Assertions.assertThat(expectedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    public void CategoryService_GetCategoryById_ThrowsResourceNotFoundException() {
        when(categoryRepository.findById(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with ID = " + category.getId()));

        Assertions.assertThatThrownBy(() -> categoryService.getCategoryById(category.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID = " + category.getId());
    }

    @Test
    public void CategoryService_CreateCategory_ReturnCreatedCategory() {
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        Category expectedCategory = categoryService.createCategory(category);

        Assertions.assertThat(expectedCategory).isNotNull();
        Assertions.assertThat(expectedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    public void CategoryService_CreateCategory_ThrowsResourceAlreadyExistsException() {
        when(categoryRepository.save(Mockito.any(Category.class)))
                .thenThrow(new ResourceAlreadyExistsException("Category with name " + category.getName() + " already exists!"));

        Assertions.assertThatThrownBy(() -> categoryService.createCategory(category))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Category with name " + category.getName() + " already exists!");
    }

    @Test
    public void CategoryService_EditCategory_ReturnEditedCategory() {
        when(categoryRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(Mockito.any(Category.class))).thenReturn(category);

        Category expectedCategory = categoryService.editCategory(category, category.getId());

        Assertions.assertThat(expectedCategory).isNotNull();
        Assertions.assertThat(expectedCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    public void CategoryService_EditCategory_ThrowsResourceAlreadyExistsException() {
        when(categoryRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(category));
        when(categoryRepository.save(Mockito.any(Category.class)))
                .thenThrow(new ResourceAlreadyExistsException("Category with name " + category.getName() + " already exists!"));

        Assertions.assertThatThrownBy(() -> categoryService.editCategory(category, category.getId()))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Category with name " + category.getName() + " already exists!");
    }

    @Test
    public void CategoryService_EditCategory_ThrowsResourceNotFoundException() {
        when(categoryRepository.findById(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with ID = " + category.getId()));

        Assertions.assertThatThrownBy(() -> categoryService.editCategory(category, category.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID = " + category.getId());
    }

    @Test
    public void CategoryService_DeleteById_ReturnNothing() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        doNothing().when(categoryRepository).deleteById(category.getId());

        assertAll(() -> categoryService.deleteCategoryById(category.getId()));
    }

    @Test
    public void CategoryService_DeleteByCategoryId_ThrowsResourceNotFoundException() {
        when(categoryRepository.findById(Mockito.any(Long.class)))
                .thenThrow(new ResourceNotFoundException("Category not found with ID = " + category.getId()));

        Assertions.assertThatThrownBy(() -> categoryService.deleteCategoryById(category.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category not found with ID = " + category.getId());
    }

    @Test
    public void CategoryService_CategoryAlreadyExists_ReturnExistedCategory() {
        when(categoryRepository.exists(Mockito.any(Example.class))).thenReturn(Boolean.TRUE);

        Assertions.assertThat(categoryService.categoryAlreadyExists(category)).isTrue();
    }
}
