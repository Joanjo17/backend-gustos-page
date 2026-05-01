package com.joanlica.gustos_page_backend.auth.controller;

import com.joanlica.gustos_page_backend.auth.dto.user.AuthTokensDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.gustos_page_backend.auth.service.UserAuthService;
import com.joanlica.gustos_page_backend.config.util.AuthCookieFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(
        name = "Autenticación",
        description = "Endpoints para registro, login, refresh token y logout mediante cookies HttpOnly."
)
public class UserAuthController {

    private final UserAuthService userAuthService;

    private final AuthCookieFactory authCookieFactory;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = """
                Registra un nuevo usuario y su perfil asociado.
                
                Si el registro es correcto, el backend devuelve los datos públicos del usuario
                y establece dos cookies HttpOnly:
                
                - access_token: token JWT de acceso.
                - refresh_token: token JWT para renovar el access token.
                
                Los tokens no se devuelven en el body de la respuesta.
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos necesarios para registrar un usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente. Cookies access_token y refresh_token establecidas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "El username ya está en uso",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        AuthTokensDTO auth = userAuthService.register(registerUserRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(auth.user().id())
                .toUri();

        return ResponseEntity
                .created(location)
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(auth.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createRefreshCookie(auth.refreshToken()).toString())
                .body(auth.user());
    }

    @Operation(
            summary = "Inicio de sesión",
            description = """
                Autentica a un usuario existente.
                
                Si las credenciales son correctas, el backend devuelve los datos públicos
                del usuario y establece dos cookies HttpOnly:
                
                - access_token: token JWT de acceso.
                - refresh_token: token JWT para renovar el access token.
                
                Los tokens no se devuelven en el body de la respuesta.
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credenciales del usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario logueado exitosamente. Cookies access_token y refresh_token establecidas.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales incorrectas",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> login(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDTO) {
        AuthTokensDTO auth = userAuthService.login(loginUserRequestDTO);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(auth.accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createRefreshCookie(auth.refreshToken()).toString())
                .body(auth.user());
    }

    @Operation(
            summary = "Renovar access token",
            description = """
                Renueva el access token usando la cookie HttpOnly refresh_token.
                
                Si el refresh token es válido, el backend establece una nueva cookie
                access_token. No devuelve tokens en el body.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Access token renovado correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Refresh token ausente, expirado o inválido",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) {
        String newAccessToken = userAuthService.refresh(refreshToken);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.createAccessCookie(newAccessToken).toString())
                .build();
    }

    @Operation(
            summary = "Obtener token CSRF",
            description = """
                    Devuelve el token CSRF necesario para realizar las peticiones protegidas al usar cookies HttpOnly.
                    
                    Para cada petición protegida, se debe incluir este valor en la cabecera X-XSRF-TOKEN en operaciones protegidas,
                    como refresh o logout.
                    """,
            security = {},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token CSRF devuelto correctamente")
            }
    )
    @GetMapping("/csrf")
    public ResponseEntity<CsrfToken> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }

    @Operation(
            summary = "Cerrar sesión",
            description = """
                Elimina las cookies access_token y refresh_token del navegador.
                
                Este endpoint no invalida tokens en base de datos si no existe una
                blacklist o almacenamiento persistente de refresh tokens.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sesión cerrada correctamente. Cookies eliminadas."
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.deleteAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authCookieFactory.deleteRefreshCookie().toString())
                .build();
    }
}