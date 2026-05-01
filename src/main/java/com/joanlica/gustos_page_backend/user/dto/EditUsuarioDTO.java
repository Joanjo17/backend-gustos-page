package com.joanlica.gustos_page_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;


public record EditUsuarioDTO(
        @Schema(
                description = "Nombre(s) del usuario.",
                example = "Joan Josep",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre

) {
}