package com.nua.core.utils;

import com.nua.core.base.entities.NUAUserBase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    private static final String BLUE = "\u001B[34m";
    private static final String RESET = "\u001B[0m";

    @Value("${jwt.private}")
    private String privateKeyPem;

    @Value("${jwt.public}")
    private String publicKeyPem;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;



    private final SecureDigestAlgorithm<PrivateKey, PublicKey> algorithm = Jwts.SIG.PS512;

    public String extractUsername(final String token){
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getPublicKey(publicKeyPem))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getSubject();
    }

    public String generateAccessToken(NUAUserBase user) {
        log.info(BLUE + "GENERATE ACCESS TOKEN" + RESET);
        return buildToken(user, jwtExpiration);
    }

    public String generateRefreshToken(NUAUserBase user) {
        log.info(BLUE + "GENERATE REFRESH TOKEN" + RESET);
        return buildToken(user, refreshExpiration);
    }

    public boolean validateToken(String token) {
        return false;
    }

    private String buildToken(final NUAUserBase user, final long expiration){
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(Map.of("name", user.getFullName()))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getPrivateKey(privateKeyPem), algorithm)
                .compact();

    }


    private PrivateKey getPrivateKey(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.US_ASCII);
            byte[] keyBytes = decodePem(pem, "PRIVATE KEY");
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new IllegalStateException("No se pudo cargar la clave privada RSA", e);
        }
    }

    private PublicKey getPublicKey(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            String pem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.US_ASCII);
            byte[] keyBytes = decodePem(pem, "PUBLIC KEY");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new IllegalStateException("No se pudo cargar la clave pública RSA", e);
        }
    }

    private static byte[] decodePem(String pem, String type) {
        String header = "-----BEGIN " + type + "-----";
        String footer = "-----END " + type + "-----";
        String normalized = pem.replace(header, "")
                .replace(footer, "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(normalized.getBytes(StandardCharsets.US_ASCII));
    }



}


