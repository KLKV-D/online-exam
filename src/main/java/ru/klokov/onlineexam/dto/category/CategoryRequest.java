package ru.klokov.onlineexam.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Category name must be not null and not empty")
    @Size(min = 2, max = 100, message = "Category name length must me between 2 and 100")
    private String name;
    @Size(max = 1000, message = "Category description length must be less then 1000")
    private String description;
}
