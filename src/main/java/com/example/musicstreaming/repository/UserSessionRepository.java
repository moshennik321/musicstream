package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.SessionStatus;
import com.example.musicstreaming.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshTokenAndStatus(String refreshToken, SessionStatus status);

    Optional<UserSession> findByUserIdAndStatus(Long userId, SessionStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE UserSession us SET us.status = com.example.musicstreaming.model.SessionStatus.REVOKED, " +
            "us.revokedAt = CURRENT_TIMESTAMP " +
            "WHERE us.user.id = :userId AND us.status = com.example.musicstreaming.model.SessionStatus.ACTIVE")
    void revokeAllUserSessions(@Param("userId") Long userId);

    Optional<UserSession> findByRefreshToken(String refreshToken);
}
