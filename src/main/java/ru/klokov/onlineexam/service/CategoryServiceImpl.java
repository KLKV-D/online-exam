package ru.klokov.onlineexam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.klokov.onlineexam.exception.ResourceAlreadyExistsException;
import ru.klokov.onlineexam.exception.ResourceNotFoundException;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.repository.CategoryRepository;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() { return categoryRepository.findAll(); }

    @Override
    public Page<Category> getAllCategoriesPaginated(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID = " + id));
    }

    @Override
    public Category createCategory(Category category) {
        if (categoryAlreadyExists(category))
            throw new ResourceAlreadyExistsException("Category with name " + category.getName() + " already exists!");
        return categoryRepository.save(category);
    }

    @Override
    public Category editCategory(Category newCategory, Long categoryToUpdateId) {
        if (categoryAlreadyExists(newCategory))
            throw new ResourceAlreadyExistsException("Category with name " + newCategory.getName() + " already exists!");

        Category categoryToUpdate = categoryRepository.findById(categoryToUpdateId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID = " + categoryToUpdateId));

        categoryToUpdate.setName(newCategory.getName());
        categoryToUpdate.setDescription(newCategory.getDescription());
        // TODO set subcategories

        return categoryRepository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID = " + id));
        categoryRepository.deleteById(id);
    }

    @Override
    public Boolean categoryAlreadyExists(Category category) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("name", ignoreCase());
        Example<Category> example = Example.of(category, matcher);
        return categoryRepository.exists(example);
    }
}
