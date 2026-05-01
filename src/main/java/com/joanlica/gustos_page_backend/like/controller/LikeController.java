package com.joanlica.gustos_page_backend.like.controller;

import com.joanlica.gustos_page_backend.like.dto.LikeCreateDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeEditDTO;
import com.joanlica.gustos_page_backend.like.dto.LikeListResponseDTO;
import com.joanlica.gustos_page_backend.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/likes")
@Tag(
        name = "Gustos",
        description = "Endpoints para gestionar los gustos de los usuarios."
)
public class LikeController {

    private LikeService likeService;

    @Operation(
            summary = "Obtener gustos por Username",
            description = "Obtener listado de gustos de usuario por su username.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de gustos enviado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LikeListResponseDTO.class)
                            )
                    )
            }
    )
    @GetMapping("/user/{username}")
    public ResponseEntity<LikeListResponseDTO> getLikesByUsername(@PathVariable String username){
        LikeListResponseDTO response = likeService.getLikesByUsername(username);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener gustos propios",
            description = "Obtener listado de gustos de usuario autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de gustos enviado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LikeListResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado / Credenciales incorrectas",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping
    public ResponseEntity<LikeListResponseDTO> getMyLikes(Principal principal){

        String username = principal.getName();
        LikeListResponseDTO response = likeService.getLikesByUsername(username);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Añadir gusto",
            description = "Añadir un gusto del usuario autenticado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos para crear un gusto nuevo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LikeCreateDTO.class)
                    )
            ),responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Se envia el objeto creado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LikeDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado / Credenciales incorrectas",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping
    public ResponseEntity<LikeDTO> addLike(Principal principal, @RequestBody LikeCreateDTO newLike){
        LikeDTO likeCreated = likeService.addLike(principal.getName(), newLike);
        return new ResponseEntity<>(likeCreated, null, 201);
    }

    @Operation(
            summary = "Editar gusto",
            description = "Editar un gusto del usuario autenticado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos para editar un gusto existente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LikeCreateDTO.class)
                    )
            ),responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Se envia el objeto editado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LikeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autorizado / Credenciales incorrectas",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Gusto no pertenece al usuario autenticado",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Gusto no encontrado",
                    content = @Content(mediaType = "application/json")
            )
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<LikeDTO> editLike(@PathVariable Long id, @RequestBody LikeEditDTO newLike, Principal principal){
        LikeDTO likeEdited = likeService.editLike(id, newLike, principal.getName());
        return ResponseEntity.ok(likeEdited);
    }


    @Operation(
            summary = "Eliminar gusto",
            description = "Eliminar un gusto del usuario autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Se envia respuesta sin contenido",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LikeDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado / Credenciales incorrectas",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Gusto no pertenece al usuario autenticado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gusto no encontrado",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id, Principal principal){
        likeService.deleteLike(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}