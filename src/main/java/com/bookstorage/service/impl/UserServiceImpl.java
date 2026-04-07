package com.bookstorage.service.impl;

import com.bookstorage.dto.user.UserRegistrationRequestDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.exception.RegistrationException;
import com.bookstorage.mapper.UserMapper;
import com.bookstorage.repository.UserRepository;
import com.bookstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto findById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("User by id: " + id + " not found")
                )
        );
    }

    @Override
    public UserResponseDto findByEmail(String email) {
        return userMapper.toDto(userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User with email: " + email + " not found"
                        )
                )
        );
    }

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto
    ) throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "User with email: " + requestDto.getEmail() + " already exists"
            );
        }
        return userMapper.toDto(userRepository.save(userMapper.toEntity(requestDto)));
    }
}
