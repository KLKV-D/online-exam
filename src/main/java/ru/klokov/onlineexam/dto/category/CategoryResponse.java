package ru.klokov.onlineexam.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
}
