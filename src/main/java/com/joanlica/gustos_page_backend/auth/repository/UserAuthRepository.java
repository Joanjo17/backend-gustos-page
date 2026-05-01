package com.joanlica.gustos_page_backend.auth.repository;

import com.joanlica.gustos_page_backend.auth.model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    boolean existsByUsername(String username);

    Optional<UserAuth> findByUsername(String username);
}