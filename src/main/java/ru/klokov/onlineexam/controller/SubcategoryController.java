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
import ru.klokov.onlineexam.dto.subcategory.SubcategoryRequest;
import ru.klokov.onlineexam.dto.subcategory.SubcategoryResponse;
import ru.klokov.onlineexam.model.Category;
import ru.klokov.onlineexam.model.Subcategory;
import ru.klokov.onlineexam.service.CategoryService;
import ru.klokov.onlineexam.service.SubcategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubcategoryController {
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private final ModelMapper mapper;

    @Value("${page.size}")
    private Integer pageSize;

    @GetMapping("/all")
    public ResponseEntity<List<SubcategoryResponse>> getAllSubcategories() {
        return ResponseEntity.ok(subcategoryService.getAllSubcategories().stream()
                .map(subcategory -> mapper.map(subcategory, SubcategoryResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<PagedResponse<SubcategoryResponse>> getAllSubcategoriesPaginated(
            @PathVariable("pageNumber") Integer pageNumber,
            @RequestParam("sortField") String sortField,
            @RequestParam("sortDirection") String sortDirection
    ) {
        Page<Subcategory> page = subcategoryService.getAllSubcategoriesPaginated(pageNumber, pageSize, sortField, sortDirection);

        List<SubcategoryResponse> subcategoryResponseList = page.getContent().stream()
                .map(subcategory -> mapper.map(subcategory, SubcategoryResponse.class)).toList();

        String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";

        PagedResponse<SubcategoryResponse> response = new PagedResponse<>(
                subcategoryResponseList,
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
    public ResponseEntity<SubcategoryResponse> getSubcategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(mapper.map(subcategoryService.getSubcategoryById(id), SubcategoryResponse.class));
    }

    @PostMapping
    public ResponseEntity<SubcategoryResponse> createSubcategory(@RequestBody @Valid SubcategoryRequest subcategoryRequest) {
        Category category = categoryService.getCategoryById(subcategoryRequest.getCategoryId());
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryRequest.getName());
        subcategory.setDescription(subcategoryRequest.getDescription());
        subcategory.setCategory(category);
        SubcategoryResponse response = mapper.map(subcategoryService.createSubcategory(subcategory), SubcategoryResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<SubcategoryResponse> editCategory(@RequestBody @Valid SubcategoryRequest subcategoryRequest,
                                                         @PathVariable("id") Long id) {
        Subcategory newSubcategory = mapper.map(subcategoryRequest, Subcategory.class);
        SubcategoryResponse response = mapper.map(subcategoryService.editSubcategory(newSubcategory, id), SubcategoryResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable("id") Long id) {
        subcategoryService.deleteSubcategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
