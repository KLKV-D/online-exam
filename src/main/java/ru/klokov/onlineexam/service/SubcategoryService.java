package ru.klokov.onlineexam.service;

import org.springframework.data.domain.Page;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.model.Subcategory;

import java.util.List;

public interface SubcategoryService {
    List<Subcategory> getAllSubcategories();
    Page<Subcategory> getAllSubcategoriesPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
    Subcategory getSubcategoryById(Long id);
    Subcategory createSubcategory(Subcategory subcategory);
    Subcategory editSubcategory(Subcategory newSubcategory, Long SubcategoryToUpdateId);
    void deleteSubcategoryById(Long id);
    Boolean subcategoryAlreadyExists(Subcategory subcategory);
}
