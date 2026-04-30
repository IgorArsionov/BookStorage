package com.bookstorage.service.impl;

import com.bookstorage.dto.user.UserRegistrationRequestDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.EntityNotFoundException;
import com.bookstorage.exception.RegistrationException;
import com.bookstorage.mapper.UserMapper;
import com.bookstorage.model.Role;
import com.bookstorage.model.RoleName;
import com.bookstorage.model.User;
import com.bookstorage.repository.RoleRepository;
import com.bookstorage.repository.UserRepository;
import com.bookstorage.service.ShoppingCartService;
import com.bookstorage.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

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

        User user = userMapper.toEntity(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = repository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Default role " + RoleName.USER + " not found"
                ));

        user.setRoles(Set.of(role));

        userRepository.save(user);

        shoppingCartService.addUserToShopCart(user);

        return userMapper.toDto(user);
    }
}
