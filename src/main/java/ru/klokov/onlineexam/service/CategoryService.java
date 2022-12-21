package ru.klokov.onlineexam.service;

import org.springframework.data.domain.Page;
import ru.klokov.onlineexam.dto.category.CategoryResponse;
import ru.klokov.onlineexam.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Page<Category> getAllCategoriesPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category editCategory(Category newCategory, Long categoryToUpdateId);
    void deleteCategoryById(Long id);
}
