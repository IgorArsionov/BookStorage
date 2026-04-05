package com.bookstorage.service.impl;

import com.bookstorage.dto.user.UserCreateRequestDto;
import com.bookstorage.dto.user.UserRegisterOrUpdateDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.exception.RegistrationException;
import com.bookstorage.mapper.UserMapper;
import com.bookstorage.model.User;
import com.bookstorage.repository.UserRepository;
import com.bookstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public UserResponseDto save(UserCreateRequestDto requestDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(requestDto)));
    }

    @Override
    public UserResponseDto update(Long id, UserRegisterOrUpdateDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("User by id: " + id + " not found")
                );
        return userMapper.toDto(userRepository.save(userMapper.updateUser(user, requestDto)));
    }

    @Override
    public UserResponseDto register(
            UserRegisterOrUpdateDto requestDto
    ) throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "User with email: " + requestDto.getEmail() + " already exists"
            );
        }
        User user = new User();
        return userMapper.toDto(userRepository.save(userMapper.updateUser(user, requestDto)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }
}
