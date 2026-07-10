package com.commuteflow.auth.service;

import com.commuteflow.auth.dto.AuthResponse;
import com.commuteflow.auth.dto.LoginRequest;
import com.commuteflow.auth.dto.MeResponse;
import com.commuteflow.auth.dto.RegisterRequest;
import com.commuteflow.auth.security.JwtService;
import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.common.exception.ResourceNotFoundException;
import com.commuteflow.user.entity.User;
import com.commuteflow.user.entity.UserRole;
import com.commuteflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = false)
    public void register(RegisterRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(
                    "An account with this email already exists"
            );
        }

        User user = new User();

        user.setEmail(email);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(UserRole.EMPLOYEE);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase();

        User user = userRepository
                .findByEmail(email)
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

    @Transactional(readOnly = true)
    public MeResponse getCurrentUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        return MeResponse.from(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public MeResponse updateRole(UUID userId, UserRole role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        user.setRole(role);

        return MeResponse.from(user);
    }
}