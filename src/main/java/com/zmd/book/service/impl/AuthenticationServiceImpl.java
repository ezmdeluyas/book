package com.zmd.book.service.impl;

import com.zmd.book.config.JwtProperties;
import com.zmd.book.dto.request.LoginRequestDto;
import com.zmd.book.dto.response.LoginResponseDto;
import com.zmd.book.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,  JwtEncoder jwtEncoder, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
        );

        Instant now = Instant.now();
        Instant exp = now.plus(jwtProperties.accessTokenExpirationMinutes(), ChronoUnit.MINUTES);

        List<String> rolesList = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .subject(auth.getName())
                .issuedAt(now)
                .expiresAt(exp)
                .claim("roles", rolesList).build();

        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(() -> "HS256").build(),
                claims
        ));

        String token = jwt.getTokenValue();

        long expiresInSeconds = jwtProperties.accessTokenExpirationMinutes() * 60;

        return new LoginResponseDto(token, "Bearer", expiresInSeconds);
    }
}
