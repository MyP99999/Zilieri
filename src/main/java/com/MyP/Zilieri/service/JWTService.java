package com.MyP.Zilieri.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Objects;

public interface JWTService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Objects> extraClaim, UserDetails userDetails);
}
