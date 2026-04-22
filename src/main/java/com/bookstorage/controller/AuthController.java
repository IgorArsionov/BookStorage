package com.bookstorage.controller;

import com.bookstorage.dto.user.UserLoginRequestDto;
import com.bookstorage.dto.user.UserLoginResponseDto;
import com.bookstorage.dto.user.UserRegistrationRequestDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.RegistrationException;
import com.bookstorage.security.AuthenticationService;
import com.bookstorage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "API for managing authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register new user", description = "Register new user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto
    ) throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @Operation(summary = "Login", description = "User authentication")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authentication(requestDto);
    }
}
