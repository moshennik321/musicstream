package com.example.musicstreaming.repository;

import com.example.musicstreaming.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
