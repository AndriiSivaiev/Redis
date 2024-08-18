package com.spring_book.redis.mapper;




import com.spring_book.redis.entity.Book;
import com.spring_book.redis.model.BookResponse;
import com.spring_book.redis.model.UpsertBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(source = "category.categoryName", target = "categoryName")
    BookResponse bookToResponse(Book book);

    @Mapping(source = "categoryName", target = "category.categoryName")
    Book requestToBook(UpsertBookRequest request);

}