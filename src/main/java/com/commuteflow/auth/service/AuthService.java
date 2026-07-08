package com.commuteflow.auth.service;

import com.commuteflow.auth.dto.AuthResponse;
import com.commuteflow.auth.dto.LoginRequest;
import com.commuteflow.auth.dto.RegisterRequest;
import com.commuteflow.auth.security.JwtService;
import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.user.entity.User;
import com.commuteflow.user.entity.UserRole;
import com.commuteflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException(
                    "An account with this email already exists"
            );
        }

        User user = new User();

        user.setEmail(request.email().toLowerCase().trim());

        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        user.setRole(UserRole.EMPLOYEE);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email().toLowerCase().trim())
                .orElseThrow(() ->
                        new BadRequestException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new BadRequestException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs() / 1000
        );
    }
}