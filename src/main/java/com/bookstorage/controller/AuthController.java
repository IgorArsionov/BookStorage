package com.bookstorage.controller;

import com.bookstorage.dto.user.UserLoginRequestDto;
import com.bookstorage.dto.user.UserRegisterOrUpdateDto;
import com.bookstorage.dto.user.UserResponseDto;
import com.bookstorage.exception.RegistrationException;
import com.bookstorage.security.AuthenticationService;
import com.bookstorage.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authService;
    private final UserService userService;

    @PostMapping("/login")
    public boolean login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        return authService.authenticate(userLoginRequestDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(
            @RequestBody @Valid UserRegisterOrUpdateDto userCreateRequestDto
    ) throws RegistrationException {
        return userService.register(userCreateRequestDto);
    }
}
