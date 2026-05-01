package com.joanlica.gustos_page_backend.user.controller;

import com.joanlica.gustos_page_backend.user.dto.EditUsuarioDTO;
import com.joanlica.gustos_page_backend.user.dto.UsuarioDTO;
import com.joanlica.gustos_page_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "Endpoints para la gestión de perfiles de usuarios")
public class UserController {

    private final UserService userService;

    // --- Endpoints 'ME' (para el usuario logueado) ---

    @Operation(
            summary = "Obtener mi perfil de usuario",
            description = "Obtiene la información de Usuario asociada al usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil del usuario encontrado"),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para este usuario"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> obtenerPerfilPropio(Principal principal) {
        UsuarioDTO usuario = userService.obtenerPerfilPropio(principal.getName());
        return ResponseEntity.ok(usuario);
    }

    @Operation(
            summary = "Editar mi perfil de usuario",
            description = "Edita la información del perfil de Usuario asociada al usuario autenticado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nuevos datos para mi perfil de usuario",
                    content = @Content(schema = @Schema(implementation = EditUsuarioDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para este usuario"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @PutMapping("/me")
    public ResponseEntity<UsuarioDTO> editarUsuarioPropio(Principal principal,
                                                          @Valid @RequestBody EditUsuarioDTO usuarioNuevo) {
        return ResponseEntity.ok(userService.editarUsuarioPropio(principal.getName(), usuarioNuevo));
    }
}