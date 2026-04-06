package com.bookstorage.service;

import com.bookstorage.dto.user.UserRegistrationRequestDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.RegistrationException;

public interface UserService {
    UserResponseDto findById(Long id);

    UserResponseDto findByEmail(String email);

    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

}
