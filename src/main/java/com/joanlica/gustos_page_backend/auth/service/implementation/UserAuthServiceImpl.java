package com.joanlica.gustos_page_backend.auth.service.implementation;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.joanlica.gustos_page_backend.auth.dto.user.AuthTokensDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.gustos_page_backend.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.gustos_page_backend.auth.model.UserAuth;
import com.joanlica.gustos_page_backend.auth.repository.RoleRepository;
import com.joanlica.gustos_page_backend.auth.repository.UserAuthRepository;
import com.joanlica.gustos_page_backend.auth.service.UserAuthService;
import com.joanlica.gustos_page_backend.config.util.JwtUtils;
import com.joanlica.gustos_page_backend.core.exception.RoleNotFoundException;
import com.joanlica.gustos_page_backend.core.exception.UserAlreadyExistsException;
import com.joanlica.gustos_page_backend.user.model.Usuario;
import com.joanlica.gustos_page_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @Override
    public AuthTokensDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        // Verificar si el username ya está en uso. Se lanza excepción si es así.
        if (userAuthRepository.existsByUsername(registerUserRequestDTO.username())) {
            throw new UserAlreadyExistsException("Username is already in use");
        }


        var roles = Set.of(roleRepository.findByRoleName("USER")   // todos los registrados serán USERs
                .orElseThrow(() -> new RoleNotFoundException("Some roles not found")));


        // Guardamos el UserAuth
        var u = new UserAuth();
        u.setUsername(registerUserRequestDTO.username());
        u.setPassword(passwordEncoder.encode(registerUserRequestDTO.password())); //Encriptamos la contraseña
        u.setRolesList(roles);
        UserAuth savedUser = userAuthRepository.save(u);

        // Creación del Usuario asociado
        var usuario = new Usuario();
        usuario.setName(registerUserRequestDTO.username().concat(String.valueOf(LocalDateTime.now().getYear())));
        usuario.setUser(savedUser);

        userRepository.save(usuario);

        // Autenticar al usuario recién registrado y generar el token JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        savedUser.getUsername(),
                        registerUserRequestDTO.password()
                )
        );
        // Se guarda el usuario autenticado en el contexto de seguridad.
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createAccessToken(authentication);
        String refreshToken = jwtUtils.createRefreshToken(authentication);


        return new AuthTokensDTO(
                accessToken,
                refreshToken,
                new UserAuthResponseDTO(savedUser.getId(), savedUser.getUsername())
        );
    }

    @Override
    public AuthTokensDTO login(LoginUserRequestDTO loginUserRequestDTO) {
        // Autenticar al usuario recién registrado y generar el token JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserRequestDTO.username(),
                        loginUserRequestDTO.password()
                )
        );
        // Se guarda el usuario autenticado en el contexto de seguridad.
        // SecurityContextHolder.getContext().setAuthentication(authentication);

        UserAuth user = userAuthRepository.findByUsername(loginUserRequestDTO.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtUtils.createAccessToken(authentication);
        String refreshToken = jwtUtils.createRefreshToken(authentication);


        return new AuthTokensDTO(
                accessToken,
                refreshToken,
                new UserAuthResponseDTO(
                        user.getId(),
                        loginUserRequestDTO.username()
                )
        );
    }

    @Override
    public String refresh(String refreshToken) {
        DecodedJWT decodedJWT = jwtUtils.validateToken(refreshToken);

        if (!jwtUtils.isRefreshToken(decodedJWT)) {
            throw new JWTVerificationException("Invalid refresh token");
        }

        String username = jwtUtils.extractUsername(decodedJWT);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );

        return jwtUtils.createAccessToken(authentication);
    }
}