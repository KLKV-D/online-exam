package ru.klokov.onlineexam.dto.subcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.klokov.onlineexam.dto.category.CategoryResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryResponse {
    private Long id;
    private String name;
    private String description;
    private CategoryResponse category;
}
