package com.joanlica.gustos_page_backend.config.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {

    //Con estas configuraciones aseguramos la autenticidad del token a crear
    @Value("${security.jwt.secret.key}")
    private String secretKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;
    @Value("${security.jwt.ttl-seconds}")
    private long ttlSeconds;
    @Value("${security.jwt.refresh.ttl-seconds}")
    private long refreshTtlSeconds;


    private String createToken(Authentication authentication, long expirationTimeSeconds, String tokenType) {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

        //esto está dentro del security context holder
        String username = authentication.getName();

        // También obtenemos los permisos/autorizaciones.
        String[] authArray = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);

        Instant now = Instant.now();

        Date iat = Date.from(now); //IssuedAt
        Date exp = Date.from(now.plusSeconds(expirationTimeSeconds)); //ExpiresAt


        String jwtToken = JWT.create()
                .withIssuer(this.userGenerator)
                .withSubject(username) // Propietario del Token (el username único del usuario)
                .withArrayClaim("authorities", authArray)// Claims, datos contraidos en el JWT
                .withClaim("type", tokenType)
                .withIssuedAt(iat)
                .withExpiresAt(exp)
                .withJWTId(UUID.randomUUID().toString()) // Id del token
                .withNotBefore(iat) // Desde cuando es válido (desde ahora en este caso)
                .sign(algorithm);

        return jwtToken;
    }

    public String createAccessToken(Authentication authentication){
        return createToken(authentication, this.ttlSeconds, "access");
    }

    public String createRefreshToken(Authentication authentication){
        return createToken(authentication, this.refreshTtlSeconds, "refresh");
    }

    // Validar y decodificar token
    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT;
        } catch (IllegalArgumentException e) {
            throw new JWTVerificationException("Invalid token configuration", e);
        }
    }

    public boolean isAccessToken(DecodedJWT decodedJWT) {
        return "access".equals(decodedJWT.getClaim("type").asString());
    }

    public boolean isRefreshToken(DecodedJWT decodedJWT) {
        return "refresh".equals(decodedJWT.getClaim("type").asString());
    }

    // Extraer el username del token decodificado
    public String extractUsername(DecodedJWT decodedJWT) {
        //el subject es el usuario según establecimos al crear el token, entonces es el username.
        return decodedJWT.getSubject();
    }

    // Obtener todos los claims
    public Map<String, Claim> returnAllClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

    // Obtener un claim en específico
    public Claim getSpecificClaim(DecodedJWT decodedJWT, String claimName) {
        return decodedJWT.getClaim(claimName);
    }
}