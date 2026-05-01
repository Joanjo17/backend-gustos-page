package com.joanlica.gustos_page_backend.like.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record LikeCreateDTO(
        @Schema(
                description = "Nombre del gusto.",
                example = "Patata"
        )
        @NotBlank(message = "El nombre del gusto no puede estar vacío.")
        String name,
        @Schema(
                description = "Descripción del gusto.",
                example = "Porque"
        )
        @NotBlank(message = "La descripción del gusto no puede estar vacía.")
        String description,
        @Schema(
                description = "URL de la imagen del gusto.",
                example = "https://example.com/image.jpg"
        )
        @Pattern(
                regexp = "^(https?://).+\\.(jpg|jpeg|png|gif)$",
                message = "La URL de la imagen debe ser válida y terminar con .jpg, .jpeg, .png o .gif."
        )
        String imageUrl,
        @Schema(
                description = "Lista de categorias del gusto",
                example = "[\"Categoria\", \"Categoria 2\"]"
        )
        @NotNull(message = "La lista de categorías no puede ser nula.")
        Set<String> categories
){
}