package com.joanlica.gustos_page_backend.user.repository;

import com.joanlica.gustos_page_backend.user.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

    // Obtenemos todos los users, incluyendo los inactivos.
    // El softDelete no se aplicará a esta query
    @Query(value = "SELECT * FROM user_profiles", nativeQuery = true)
    Page<Usuario> findAllIncludingInactive(Pageable pageable);

    Optional<Usuario> findUsuarioByUser_Username(String userUsername);
}