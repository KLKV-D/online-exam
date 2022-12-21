package ru.klokov.onlineexam.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.klokov.onlineexam.dto.PagedResponse;
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

        PagedResponse<CategoryResponse> response = new PagedResponse<>(
                categoryResponseList,
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                sortField,
                sortDirection
        );

        return ResponseEntity.ok(response);
    }
}
