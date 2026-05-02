package com.joanlica.gustos_page_backend.auth.controller;

import com.joanlica.gustos_page_backend.auth.dto.admin.UpdateUserRolesDTO;
import com.joanlica.gustos_page_backend.auth.dto.admin.UserRolesResponseDTO;
import com.joanlica.gustos_page_backend.auth.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/auth")
@Tag(
        name = "Admin - User Management",
        description = "Endpoints para peticiones reservadas a administradores."
)
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(
            summary = "Actualizar roles de un usuario",
            description = """
                    Permite a un usuario con rol ADMIN modificar los roles asociados a un usuario existente.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Roles actualizados correctamente",
                            content = @Content(schema = @Schema(implementation = UserRolesResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
                    @ApiResponse(responseCode = "403", description = "Usuario no autorizado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PutMapping("/{username}")
    public ResponseEntity<UserRolesResponseDTO> updateUserRole(
            @Parameter(
                    description = "Username del usuario al que modificar el rol.",
                    example = "user_joan"
            )
            @PathVariable String username,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nueva lista de roles",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateUserRolesDTO.class))
            )
            @Valid @RequestBody UpdateUserRolesDTO updateUserRolesDTO
    ) {
        UserRolesResponseDTO updatedUser = adminAuthService.updateRoleList(username, updateUserRolesDTO);
        return ResponseEntity.ok(updatedUser);
    }
}