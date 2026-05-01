package com.joanlica.gustos_page_backend.user.service.implementation;

import com.joanlica.gustos_page_backend.core.exception.UsuarioNotFoundException;
import com.joanlica.gustos_page_backend.core.util.pages.dto.PageResponse;
import com.joanlica.gustos_page_backend.user.dto.EditUsuarioDTO;
import com.joanlica.gustos_page_backend.user.dto.UsuarioDTO;
import com.joanlica.gustos_page_backend.user.mapper.UsuarioMapper;
import com.joanlica.gustos_page_backend.user.model.Usuario;
import com.joanlica.gustos_page_backend.user.repository.UserRepository;
import com.joanlica.gustos_page_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UsuarioDTO> listarUsuariosActivos(Pageable pageable) {
        Page<UsuarioDTO> page = userRepository.findAll(pageable)
                .map(UsuarioMapper::toDto);
        return PageResponse.from(page);
    }

    private Usuario buscarUsuarioEntidadPorId(Long id) {
        // Solo para Usuarios Activos.
        return userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("No se encontró el usuario con el id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPerfilPropio(String username) {
        Usuario usuario = buscarUsuarioPorUsername(username);
        return UsuarioMapper.toDto(usuario);
    }

    @Override
    public Usuario buscarUsuarioPorUsername(String username) {
        return userRepository.findUsuarioByUser_Username(username)
                .orElseThrow(() -> new UsuarioNotFoundException("El usuario no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = this.buscarUsuarioEntidadPorId(id);
        return UsuarioMapper.toDto(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public void cancelarUsuarioPorId(Long id) {
        // Primero comprobamos que exista.
        this.buscarUsuarioEntidadPorId(id);

        // El deleteById es interceptado por @SoftDelete.
        // Cambia la columna 'activo' a 'false' en vez de borrar la fila.
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioDTO editarUsuarioPropio(String username, EditUsuarioDTO usuarioNuevo) {
        Usuario usuario = buscarUsuarioPorUsername(username);

        usuario.setName(usuarioNuevo.nombre());

        userRepository.save(usuario);
        return UsuarioMapper.toDto(usuario);
    }
}