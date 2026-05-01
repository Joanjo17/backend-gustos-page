package com.joanlica.gustos_page_backend.auth.service;

import com.joanlica.gustos_page_backend.auth.dto.user.AuthTokensDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.RegisterUserRequestDTO;

public interface UserAuthService {
    // Metodo para registrar un nuevo usuario
    AuthTokensDTO register(RegisterUserRequestDTO registerUserRequestDTO);

    // Metodo para loguear un usuario
    AuthTokensDTO login(LoginUserRequestDTO loginUserRequestDTO);

    // Metodo para refresh
    String refresh(String refreshToken);
}