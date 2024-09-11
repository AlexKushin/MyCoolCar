package com.mycoolcar.jwt;

public record JwtTokenResponse(String token, int expiresIn) {
}
