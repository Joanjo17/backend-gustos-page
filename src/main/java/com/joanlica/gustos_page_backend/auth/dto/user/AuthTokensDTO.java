package com.joanlica.gustos_page_backend.auth.dto.user;

public record AuthTokensDTO(
        String accessToken,
        String refreshToken,
        UserAuthResponseDTO user
) {}