package com.joanlica.gustos_page_backend.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateRoleRequestDTO(
        @Schema(
                description = "Nombre único del rol (ej: 'ADMIN', 'USER').",
                example = "USER",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Pattern(regexp = "^[A-Z_]+$", message = "El nombre del rol solo debe contener mayúsculas y guiones bajos (ej: 'ROLE_NAME')")
        String roleName
) {
}