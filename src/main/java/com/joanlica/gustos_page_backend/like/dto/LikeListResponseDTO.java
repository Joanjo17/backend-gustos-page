package com.joanlica.gustos_page_backend.like.dto;


import com.joanlica.gustos_page_backend.user.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record LikeListResponseDTO(
        UsuarioDTO user,
        @Schema(
                description = "Lista de gustos"
        )
        List<LikeDTO> likes
) {
}