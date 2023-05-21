package com.josephnzi.monade.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


@Service
@AllArgsConstructor
public class JsonWebToken {
    private final String SECRET="hGy7X/fU/Rc6tAnXb/I+FMBAOunHyvBHL3b3/zkEt8i9jhihXzvNTTUvd7i9mVg+hg";

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaims(String token, Function<Claims,T>claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    public String generateTokens(UserDetails userDetails){
        return generateTokens(new HashMap<>(),userDetails);
    }
    public String generateTokens(Map<String, Objects>extractClaims, UserDetails userDetails){

        return Jwts.builder()
                .setClaims(extractClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+10*6000*24))
                .setSubject(userDetails.getUsername())
                .signWith(getPrivateKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public Boolean isValideToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public Date extractExpirationDate(String token) {
        return extractClaims(token,Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    private Key getPrivateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
