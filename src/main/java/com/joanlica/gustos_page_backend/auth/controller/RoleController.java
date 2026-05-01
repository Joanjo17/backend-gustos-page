package com.joanlica.gustos_page_backend.auth.controller;

import com.joanlica.gustos_page_backend.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.role.RoleResponseDTO;
import com.joanlica.gustos_page_backend.auth.dto.role.UpdateRoleRequestDTO;
import com.joanlica.gustos_page_backend.auth.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Endpoints para la gestión interna de roles. " +
        "TODOS requieren rol de ADMIN.")
public class RoleController {

    private final RoleService roleService;

    @Operation(
            summary = "Listar todos los roles (ADMIN)",
            description = "Obtiene una lista de todos los roles disponibles en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de roles",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @Operation(
            summary = "Obtener rol por ID (ADMIN)",
            description = "Obtiene un rol específico por su ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Rol encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        RoleResponseDTO role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @Operation(
            summary = "Obtener rol por Nombre (ADMIN)",
            description = "Obtiene un rol específico por su nombre (ej: 'ROLE_USER').",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Rol encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)", content = @Content)
            }
    )
    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String roleName) {
        RoleResponseDTO role = roleService.findByName(roleName);
        return ResponseEntity.ok(role);
    }

    @Operation(
            summary = "Crear un nuevo rol (ADMIN)",
            description = "Crea un nuevo rol en la base de datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del nuevo rol (el prefijo 'ROLE_' se añade automáticamente si no se incluye)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateRoleRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Rol creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos (ej: nombre vacío o formato incorrecto)", content = @Content),
                    @ApiResponse(responseCode = "409", description = "El nombre del rol ya existe (Conflicto)", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody CreateRoleRequestDTO role) {
        RoleResponseDTO newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }

    @Operation(
            summary = "Actualizar un rol (ADMIN)",
            description = "Actualiza el nombre de un rol existente.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nuevos datos para el rol",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateRoleRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Rol actualizado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = RoleResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Rol no encontrado", content = @Content),
                    @ApiResponse(responseCode = "409", description = "El nuevo nombre de rol ya existe (Conflicto)", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)", content = @Content)
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateRoleRequestDTO role) {
        RoleResponseDTO newRole = roleService.update(id, role);
        return ResponseEntity.ok(newRole);
    }
}