package com.brognara.useringredientsservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Set;

public class JwtUtils {

    private static final String CLIENT_ID = "252883769286-1d5it2rfeqtslt3h0mbkrvdb3i7gh0lk.apps.googleusercontent.com";
    private static final String SECRET_KEY = "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";
    private static final Set<String> VALID_ISSUERS = Set.of("https://accounts.google.com", "accounts.google.com");

    private JwtUtils() {}

    public static Claims extractAllClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // TODO do we need to check if email_verified == true? Probably not
    public static boolean isTokenValid(final Claims claims) {
        return isTokenExpired(claims) && isTokenIssuerValid(claims) && isTokenClientIdValid(claims);
    }

    private static boolean isTokenExpired(final Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private static boolean isTokenIssuerValid(final Claims claims) {
        return VALID_ISSUERS.contains(claims.getIssuer());
    }

    private static boolean isTokenClientIdValid(final Claims claims) {
        return CLIENT_ID.equals(claims.getAudience());
    }

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

}
