package com.spring_book.redis.model;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBookRequest {

    @NotBlank(message = "Book title cannot be empty")
    private String name;

    @NotBlank(message = "Author cannot be empty")
    private String author;

    @NotBlank(message = "Category cannot be empty")
    private String categoryName;
}