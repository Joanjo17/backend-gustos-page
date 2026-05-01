package com.joanlica.gustos_page_backend.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserAuthResponseDTO(
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
                description = "Nombre del usuario.",
                example = "Joan"
        )
        String name

) {
}