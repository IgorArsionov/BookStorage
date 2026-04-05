package com.bookstorage.mapper;

import com.bookstorage.config.MapperConfig;
import com.bookstorage.dto.user.UserCreateRequestDto;
import com.bookstorage.dto.user.UserRegisterOrUpdateDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserCreateRequestDto requestDto);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "password", source = "updateUser.password")
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUser(@MappingTarget User user, UserRegisterOrUpdateDto updateUser);
}
