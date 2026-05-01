package com.joanlica.gustos_page_backend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioDTO(
        @Schema(description = "ID único del usuario.", example = "1")
        Long id,

        @Schema(description = "Nombre(s) del usuario.", example = "Joan Josep")
        String name,

        @Schema(description = "Username del usuario.", example = "joanjo.17")
        String username
) {
}