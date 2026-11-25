package com.phong.testsec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.Keys;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // Tạo signing key từ secret string
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ① GENERATE TOKEN — gọi khi user login thành công
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())      // sub = email
                .claim("role", userDetails.getAuthorities()
                        .iterator().next().getAuthority()) // thêm role vào payload
                .issuedAt(new Date())                    // iat = thời điểm tạo
                .expiration(new Date(System.currentTimeMillis() + expiration)) // exp
                .signWith(getSigningKey())               // ký bằng SECRET_KEY
                .compact();                              // build thành chuỗi JWT
    }

    // ② EXTRACT USERNAME — đọc email từ token
    public String extractUsername(String token) {
        var t = extractClaim(token, Claims::getSubject);
        return t;
    }

    // ③ VALIDATE TOKEN — token đúng chữ ký và chưa hết hạn không?
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // --- Helper methods ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // dùng key để verify chữ ký
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}