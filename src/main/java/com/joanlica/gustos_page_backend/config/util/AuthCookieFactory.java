package com.joanlica.gustos_page_backend.config.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthCookieFactory {

    @Value("${security.jwt.ttl-seconds}")
    private long accessTtlSeconds;

    @Value("${security.jwt.refresh.ttl-seconds}")
    private long refreshTtlSeconds;

    @Value("${app.cookies.secure}")
    private boolean secure;

    @Value("${app.cookies.same-site}")
    private String sameSite;

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(accessTtlSeconds)
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/api/v1/auth/refresh")
                .maxAge(refreshTtlSeconds)
                .build();
    }

    public ResponseCookie deleteAccessCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(0)
                .build();
    }

    public ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .build();
    }
}