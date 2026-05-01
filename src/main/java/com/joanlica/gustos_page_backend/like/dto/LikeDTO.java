package com.joanlica.gustos_page_backend.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record LikeDTO(
        @Schema(
                description = "Id del gusto.",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Nombre del gusto.",
                example = "Patata"
        )
        String name,
        @Schema(
                description = "Descripción del gusto.",
                example = "Porque"
        )
        String description,
        @Schema(
                description = "URL de la imagen del gusto.",
                example = "https://example.com/image.jpg"
        )
        String imageUrl,
        @Schema(
                description = "Lista de categorias del gusto",
                example = "[\"Categoria\", \"Categoria 2\"]"
        )
        Set<String> categories
) {
}