package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookDtoWithoutCategoryIds;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.model.Book;
import com.bookstorage.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {

    BookResponseDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookResponseDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toEntity(BookCreateRequestDto requestDto);

    @AfterMapping
    default void setCategories(
            @MappingTarget Book book,
            BookCreateRequestDto requestDto
    ) {
        if (requestDto.getCategoryIds() == null) {
            return;
        }

        Set<Category> categories = requestDto.getCategoryIds().stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());

        book.setCategories(categories);
    }

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book updateBook(@MappingTarget Book book, BookCreateRequestDto updateBook);

}
