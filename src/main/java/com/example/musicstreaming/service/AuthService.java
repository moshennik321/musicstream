package com.example.musicstreaming.service;

import com.example.musicstreaming.model.SessionStatus;
import com.example.musicstreaming.model.User;
import com.example.musicstreaming.model.UserSession;
import com.example.musicstreaming.repository.UserRepository;
import com.example.musicstreaming.repository.UserSessionRepository;
import com.example.musicstreaming.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Регистрация нового пользователя с ролью
    public Map<String, String> register(String username, String password, String email, String role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username '" + username + "' already exists");

        }
        if (password == null || password.length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters long");
        }
        String specialChars = "!@#$%^&*()_+-={}[]|:;\"'<>,.?/";
        boolean hasSpecial = password.chars()
                .anyMatch(ch -> specialChars.indexOf(ch) >= 0);

        if (!hasSpecial) {
            throw new RuntimeException("Password must contain at least one special character");
        }
        // Хотя бы одна заглавная буква
        boolean hasUppercase = password.chars()
                .anyMatch(Character::isUpperCase);
        if (!hasUppercase) {
            throw new RuntimeException("Password must contain at least one uppercase letter");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User with username '" + username + "' already exists");
        }

        String userRole = role != null ? role.replace("ROLE_", "") : "USER";
        if (!userRole.equals("ADMIN") && !userRole.equals("USER")) {
            throw new RuntimeException("Invalid role. Must be USER or ADMIN");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(userRole);
        user = userRepository.save(user);

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getId());

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        UserSession session = new UserSession(user, refreshToken, expiresAt);
        userSessionRepository.save(session);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("role", user.getRole());
        return tokens;
    }

    public Map<String, String> register(String username, String password, String email) {
        return register(username, password, email, "USER");
    }

    public Map<String, String> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getId());

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        UserSession session = new UserSession(user, refreshToken, expiresAt);
        userSessionRepository.save(session);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("role", user.getRole());
        return tokens;
    }

    public Map<String, String> refreshTokens(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshTokenAndStatus(
                refreshToken, SessionStatus.ACTIVE);
        if (sessionOpt.isEmpty()) {
            throw new RuntimeException("Session not found or expired");
        }

        UserSession session = sessionOpt.get();
        User user = session.getUser();

        session.setStatus(SessionStatus.REVOKED);
        session.setRevokedAt(LocalDateTime.now());
        userSessionRepository.save(session);

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getId());

        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        UserSession newSession = new UserSession(user, newRefreshToken, expiresAt);
        userSessionRepository.save(newSession);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("role", user.getRole());
        return tokens;
    }

    public void logout(String refreshToken) {
        Optional<UserSession> sessionOpt = userSessionRepository.findByRefreshToken(refreshToken);
        if (sessionOpt.isPresent()) {
            UserSession session = sessionOpt.get();
            session.setStatus(SessionStatus.REVOKED);
            session.setRevokedAt(LocalDateTime.now());
            userSessionRepository.save(session);
        }
    }
}
