package ru.klokov.onlineexam.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klokov.onlineexam.dto.PagedResponse;
import ru.klokov.onlineexam.dto.category.CategoryRequest;
import ru.klokov.onlineexam.dto.category.CategoryResponse;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper mapper;

    @Value("${page.size}")
    private Integer pageSize;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream()
                .map(category -> mapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<PagedResponse<CategoryResponse>> getAllCategoriesPaginated(
            @PathVariable("pageNumber") Integer pageNumber,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDirection") String sortDirection
    ) {
        Page<Category> page = categoryService.getAllCategoriesPaginated(pageNumber, pageSize, sortField, sortDirection);

        List<CategoryResponse> categoryResponseList = page.getContent().stream()
                .map(category -> mapper.map(category, CategoryResponse.class)).toList();

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        PagedResponse<CategoryResponse> response = new PagedResponse<>(
                categoryResponseList,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                sortField,
                sortDirection,
                reverseSortDirection
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(mapper.map(categoryService.getCategoryById(id), CategoryResponse.class));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryResponse response = mapper.map(categoryService.createCategory(
                mapper.map(categoryRequest, Category.class)), CategoryResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CategoryResponse> editCategory(@RequestBody @Valid CategoryRequest categoryRequest,
                                                         @PathVariable("id") Long id) {
        Category newCategory = mapper.map(categoryRequest, Category.class);
        CategoryResponse response = mapper.map(categoryService.editCategory(newCategory, id), CategoryResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
