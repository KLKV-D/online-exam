package ru.klokov.onlineexam.dto.subcategory;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryRequest {
    @NotBlank(message = "Subcategory name must be not null and not empty")
    @Size(min = 1, max = 100, message = "Category name length must me between 2 and 100")
    private String name;
    @Size(max = 1000, message = "Subcategory description length must be less then 1000")
    private String description;
    @NotNull(message = "Category id must be a number greater then 0")
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long categoryId;
}
