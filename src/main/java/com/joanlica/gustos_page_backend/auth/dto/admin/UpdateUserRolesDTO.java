package com.joanlica.gustos_page_backend.auth.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record UpdateUserRolesDTO(
        @Schema(
                description = "Lista de roles asignados al usuario.",
                example = "[\"ADMIN\",\"USER\"]"
        )
        @NotNull(message = "La lista de roles no puede ser nula.")
        Set<String> rolesList
) {
}