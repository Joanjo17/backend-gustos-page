package com.joanlica.gustos_page_backend.auth.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UserRolesResponseDTO(
        @Schema(
                description = "ID único del usuario en la base de datos.",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Username del usuario.",
                example = "Joanjo17"
        )
        String username,
        @Schema(
                description = "Lista de roles asignados al usuario.",
                example = "[\"ADMIN\",\"USER\"]"
        )
        Set<String> rolesList
) {
}