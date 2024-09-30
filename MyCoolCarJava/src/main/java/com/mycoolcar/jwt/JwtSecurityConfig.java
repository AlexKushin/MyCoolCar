package com.mycoolcar.jwt;

import com.mycoolcar.configs.RestAuthenticationEntryPoint;
import com.mycoolcar.services.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        //CarController
                        .requestMatchers(HttpMethod.GET, "/api/top_cars").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cars/my").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/cars/subscriptions").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/cars/new").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/cars/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        //FileController
                        .requestMatchers(HttpMethod.GET, "/getImages/**").permitAll()
                        //PostController
                        .requestMatchers(HttpMethod.POST, "/api/car-logbook/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/car-club/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/user/news").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/car-club-posts/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/car-logbook-posts/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/car-logbook-posts/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/cars/*/logbook").hasAnyRole("ADMIN", "MODERATOR", "USER")
                        //UserController
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/registration").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/registration/confirm").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/resetPassword").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/savePassword").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/me").hasRole("USER")

                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(withDefaults()))
                .httpBasic(
                        HttpBasicConfigurer::disable).
                exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer
                                .authenticationEntryPoint(
                                        new RestAuthenticationEntryPoint()))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserService userService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bcryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return new ProviderManager(authenticationProvider);
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        JWKSet jwkSet = new JWKSet(rsaKey());
        return ((jwkSelector, securityContext)
                -> jwkSelector.select(jwkSet));
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    JwtDecoder jwtDecoder() throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey().toRSAPublicKey())
                .build();
    }

    @Bean
    public RSAKey rsaKey() {

        KeyPair keyPair = keyPair();

        return new RSAKey
                .Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    public KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to generate an RSA Key Pair", e);
        }
    }

    @Bean
    PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Remove the "SCOPE_" prefix
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return authenticationConverter;
    }
}
