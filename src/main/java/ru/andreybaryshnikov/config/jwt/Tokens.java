package ru.andreybaryshnikov.config.jwt;

public record Tokens(String accessToken, String accessTokenExpiry,
                     String refreshToken, String refreshTokenExpiry) {
}
