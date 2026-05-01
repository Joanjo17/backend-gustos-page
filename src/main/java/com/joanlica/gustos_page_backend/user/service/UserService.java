package com.joanlica.gustos_page_backend.user.service;

import com.joanlica.gustos_page_backend.core.util.pages.dto.PageResponse;
import com.joanlica.gustos_page_backend.user.dto.EditUsuarioDTO;
import com.joanlica.gustos_page_backend.user.dto.UsuarioDTO;
import com.joanlica.gustos_page_backend.user.model.Usuario;
import org.springframework.data.domain.Pageable;

public interface UserService {

    PageResponse<UsuarioDTO> listarUsuariosActivos(Pageable pageable);

    Usuario buscarUsuarioPorUsername(String username);
    UsuarioDTO obtenerPerfilPropio(String username);
    UsuarioDTO buscarUsuarioPorId(Long id);

    void cancelarUsuarioPorId(Long id);

    UsuarioDTO editarUsuarioPropio(String username, EditUsuarioDTO usuarioNuevo);
}