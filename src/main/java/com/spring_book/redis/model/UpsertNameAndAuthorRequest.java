package com.spring_book.redis.model;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertNameAndAuthorRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String author;
}