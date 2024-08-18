package com.spring_book.redis.configuration.properties;



import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "book.cache")
public class BookCacheProperties {

    private final List<String> cacheNames = new ArrayList<>();
    private final Map<String, CacheProperties> caches = new HashMap<>();

    @Data
    public static class CacheProperties {
        private Duration expiry = Duration.ZERO;
    }

    public interface CacheNames {
        String FIND_ALL_BOOKS = "findAllBooks";
        String FIND_BY_BOOK_ID = "findByBookId";
        String FIND_ALL_BY_CATEGORY = "findAllByCategory";
        String FIND_BY_AUTHOR_AND_NAME = "findByAuthorAndName";
    }
}