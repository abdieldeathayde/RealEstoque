package com.estoque.realcar.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "YWJkaWVsZXN0b3F1ZXNlY3JldGtleTEyMzQ1Njc4OTA=";


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }


    // =============================
    // GERAR TOKEN
    // =============================

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
                )
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // =============================
    // EXTRAIR USERNAME
    // =============================
//    public String extractUsername(String token) {
//        return extractAllClaims(token).getSubject();
//    }

    // =============================
    // VALIDAR TOKEN (⭐ FALTAVA)
    // =============================
    public boolean isTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // =============================
    // VERIFICA EXPIRAÇÃO
    // =============================
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // =============================
    // EXTRAIR CLAIMS
    // =============================

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // =============================
    // CHAVE SECRETA
    // =============================

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
