package com.joanlica.gustos_page_backend.user.mapper;

import com.joanlica.gustos_page_backend.user.dto.UsuarioDTO;
import com.joanlica.gustos_page_backend.user.model.Usuario;


public class UsuarioMapper {

    public static UsuarioDTO toDto(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(),
                usuario.getName(), usuario.getUser().getUsername());
    }

}