package com.bookstorage.service;

import com.bookstorage.dto.user.UserCreateRequestDto;
import com.bookstorage.dto.user.UserRegisterOrUpdateDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.RegistrationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto findById(Long id);

    UserResponseDto findByEmail(String email);

    UserResponseDto save(UserCreateRequestDto requestDto);

    UserResponseDto update(Long id, UserRegisterOrUpdateDto requestDto);

    UserResponseDto register(UserRegisterOrUpdateDto requestDto) throws RegistrationException;

    void deleteById(Long id);

    Page<UserResponseDto> findAll(Pageable pageable);

}
