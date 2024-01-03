package com.mycoolcar.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins="http://localhost:4200")
public class JwtAuthenticationController {

    private final JwtTokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationController(JwtTokenService tokenService,
                                       AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) {
        log.info("jwtTokenRequest = {}", jwtTokenRequest);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jwtTokenRequest.username(),
                        jwtTokenRequest.password());
        log.info("authenticationToken = {}", authenticationToken);
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);
        log.info("authentication.getAuthorities = {}", authentication.getAuthorities());
        log.info("authentication.isAuthenticated() = {}", authentication.isAuthenticated());
        log.info("authentication.getCredentials() = {}", authentication.getCredentials());
        log.info("authentication.getName() = {}", authentication.getName());
        log.info("authentication.getDetails() = {}", authentication.getDetails());
        log.info("authentication.getPrincipal() = {}", authentication.getPrincipal());
        String token = tokenService.generateToken(authentication);
        log.info("token = {}", token);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}
