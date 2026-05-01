package com.joanlica.gustos_page_backend.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRoleRequestDTO(
        @Schema(
                description = "Nuevo nombre único del rol.",
                example = "CUSTOMER",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre del rol no puede estar vacío")
        @Pattern(regexp = "^[A-Z_]+$", message = "El nombre del rol solo debe contener mayúsculas y guiones bajos")
        String roleName
) {
}