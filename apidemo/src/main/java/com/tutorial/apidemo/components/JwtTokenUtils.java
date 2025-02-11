package com.tutorial.apidemo.components;

import com.tutorial.apidemo.models.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Getter
    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    public String extractUsername(String token) {
        return !token.trim().isEmpty() ? extractClaim(token, Claims::getSubject) : null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        return generateToken(new HashMap<>(), customUserDetails);
    }

    public String generateToken(Map<String, Object> claims, CustomUserDetails customUserDetails) {
        return buildToken(claims, customUserDetails, this.JWT_EXPIRATION);
    }

    private String buildToken(
            Map<String, Object> claims,
            CustomUserDetails customUserDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(Long.toString(customUserDetails.getUser().getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, CustomUserDetails customUserDetails) {
        final String username = extractUsername(token);
        return (username.equals(customUserDetails.getUser().getId().toString()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
