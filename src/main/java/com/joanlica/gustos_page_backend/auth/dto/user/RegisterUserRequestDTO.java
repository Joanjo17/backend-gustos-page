package com.joanlica.gustos_page_backend.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
        @Schema(
                description = "Nombre de usuario único.",
                example = "jperez",
                minLength = 3,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        @Pattern(regexp="^[a-zA-Z0-9_]+$",
                message="El nombre de usuario solo puede contener letras, números y guiones bajos")
        String username,

        @Schema(
                description = "Contraseña con mínimo 8 caracteres, al menos una letra, un número y un símbolo.",
                example = "PasswOrd!123",
                minLength = 8,
                pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
                message = "La contraseña debe tener al menos una letra, un número y un símbolo")
        String password
) {
}