package com.joanlica.gustos_page_backend.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LoginUserRequestDTO(
        @Schema(
                description = "Nombre de usuario único.",
                example = "jperez",
                minLength = 3,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Pattern(regexp="^[a-zA-Z0-9_]+$",
                message="El nombre de usuario solo puede contener letras, números y guiones bajos")
        String username,

        @Schema(
                description = "Contraseña del usuario.",
                example = "PasswOrd!123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}