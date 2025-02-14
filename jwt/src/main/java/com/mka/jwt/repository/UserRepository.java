package com.mka.jwt.repository;

import com.mka.jwt.entities.Role;
import com.mka.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}
