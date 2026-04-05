package com.bookstorage.security;

import com.bookstorage.dto.user.UserLoginRequestDto;
import com.bookstorage.model.User;
import com.bookstorage.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> user = userRepository.findByEmail(userLoginRequestDto.getEmail());
        return user.isPresent()
                && user.get().getPassword().equals(userLoginRequestDto.getPassword());
    }
}
