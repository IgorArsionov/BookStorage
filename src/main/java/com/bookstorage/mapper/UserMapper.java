package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.user.UserRegistrationRequestDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toEntity(UserRegistrationRequestDto requestDto);
}
