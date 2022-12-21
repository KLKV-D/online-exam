package ru.klokov.onlineexam.service;

import org.springframework.data.domain.Page;
import ru.klokov.onlineexam.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Page<Category> getAllCategoriesPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
}
