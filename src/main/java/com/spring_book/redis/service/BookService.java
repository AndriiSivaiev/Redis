package com.spring_book.redis.service;




import com.spring_book.redis.configuration.properties.BookCacheProperties;
import com.spring_book.redis.entity.Book;
import com.spring_book.redis.entity.Category;
import com.spring_book.redis.exception.EntityNotFoundException;
import com.spring_book.redis.repository.BookRepository;
import com.spring_book.redis.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@Slf4j
@EnableCaching
@CacheConfig(cacheManager = "redisCacheManager")
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Cacheable(BookCacheProperties.CacheNames.FIND_ALL_BOOKS)
    public List<Book> findAll() {
        log.debug("BookService -> findAll");
        return bookRepository.findAll();
    }

    @Cacheable(cacheNames = BookCacheProperties.CacheNames.FIND_BY_BOOK_ID, key = "#id")
    public Book findByBookId(Long id) {
        log.debug("BookService -> findByBookId id = {}", id);
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MessageFormat
                .format("Book with ID {0} not found", id)));
    }

    @Cacheable(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BY_CATEGORY, key = "#categoryName")
    public List<Book> findAllByCategory(String categoryName) {
        log.debug("BookService -> findAllByCategory categoryName = {}", categoryName);
        return bookRepository.findAllByCategoryCategoryName(categoryName);
    }

    @Cacheable(cacheNames = BookCacheProperties.CacheNames.FIND_BY_AUTHOR_AND_NAME, key = "#author+#name")
    public Book findByAuthorAndName(String author, String name) {
        log.debug("BookService -> findByAuthorAndTitle author: {} and name: {}", author, name);
        return bookRepository.findByAuthorAndName(author, name).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Book with author {0} and name {1} not found", author, name)
        ));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BOOKS, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BY_CATEGORY, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_AUTHOR_AND_NAME, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_BOOK_ID, allEntries = true)
    })
    public Book create(Book book, String categoryName) {
        if (bookRepository.existsByAuthorAndName(book.getAuthor(), book.getName())) {
            throw new IllegalStateException(
                    String.format("Book with this author: %s and name: %s already exists",
                            book.getAuthor(), book.getName()));
        }


        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setCategoryName(categoryName);
                    return categoryRepository.save(newCategory);
                });


        Book newBook = new Book();
        newBook.setAuthor(book.getAuthor());
        newBook.setName(book.getName());
        newBook.setCategory(category);

        log.debug("Created new book: {}", newBook);

        return bookRepository.save(newBook);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BOOKS, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BY_CATEGORY, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_AUTHOR_AND_NAME, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_BOOK_ID, key = "#id", beforeInvocation = true)
    })
    public Book update(Book book,Long id) {
        Book existedBook = findByBookId(id);
        Category existedCategory = existedBook.getCategory();
        existedCategory.setCategoryName(book.getCategory().getCategoryName());
        existedBook.setAuthor(book.getAuthor());
        existedBook.setName(book.getName());

        log.debug("Update book: {}", existedBook.getId());
        return bookRepository.save(existedBook);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BOOKS, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_ALL_BY_CATEGORY, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_AUTHOR_AND_NAME, allEntries = true),
            @CacheEvict(cacheNames = BookCacheProperties.CacheNames.FIND_BY_BOOK_ID, key = "#id", beforeInvocation = true)
    })
    public void deleteById(Long id) {
        log.info("Delete book by Id: {}", id);
        bookRepository.deleteById(id);
    }
}