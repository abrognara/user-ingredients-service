package com.brognara.useringredientsservice.security;

import com.brognara.useringredientsservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // get JWT from request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        // validate nonnull and format, then grab the jwt
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        // extract claims
        Claims claims = JwtUtils.extractAllClaims(jwt);
        // validate jwt
        if (!JwtUtils.isTokenValid(claims)) {
            filterChain.doFilter(request, response);
            return;
        }

        String firstName = (String) claims.get("given_name");
        String lastName = (String) claims.get("family_name");
        String email = (String) claims.get("email");
        String googleUserId = (String) claims.get("sub");
        boolean emailVerified = (boolean) claims.get("email_verified");

        System.out.println("first name: " + firstName + ", last name: " + lastName + ", email: " + email +
                ", google user id: " + googleUserId + ", email verified: " + emailVerified);

        if (googleUserId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =
        }
    }
}
