package com.spring_book.redis.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {

    private Long id;

    private String name;

    private String author;

    private String categoryName;

}