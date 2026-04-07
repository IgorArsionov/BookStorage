package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.book.BookCreateRequestDto;
import com.bookstorage.dto.book.BookResponseDto;
import com.bookstorage.model.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookResponseDto toDto(Book book);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    Book toEntity(BookCreateRequestDto requestDto);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Book updateBook(@MappingTarget Book book, BookCreateRequestDto updateBook);
}
