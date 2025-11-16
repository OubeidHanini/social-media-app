package com.example.questionapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Date;

/**
 * JWT Token Provider - Verantwortlich für die Generierung, Validierung und das Parsen von JWT-Tokens
 * Erstellt für jeden Benutzer bei der Anmeldung einen eindeutigen JWT-Token
 */
@Component
public class JWTTokenProvider {

    @Value("${question.app.secret}")  // Wert aus application.properties
    private String APP_SECRET = "socialMedia";

    @Value("${question.expires.in}")  // Token-Gültigkeitsdauer in Millisekunden
    private long EXPIRES_IN;

    /**
     * Generiert einen JWT-Token basierend auf der Authentifizierung
     * @param auth Das Authentifizierungs-Objekt mit Benutzerinformationen
     * @return Der generierte JWT-Token als String
     */
    public String generateJWTToken(Authentication auth){
        // Principal enthält die Informationen des angemeldeten Benutzers
        JWTUserDetails userDetails = (JWTUserDetails) auth.getPrincipal();
        // Ablaufzeit des Tokens berechnen (aktuelle Zeit + Gültigkeitsdauer)
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        // JWT-Token mit Benutzer-ID, Ausstellungszeit, Ablaufzeit und Signatur erstellen
        return Jwts.builder().setSubject(Long.toString(userDetails.getId()))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
    }

    /**
     * Generiert einen JWT-Token basierend auf der Benutzer-ID (für Refresh-Token)
     * @param userId Die ID des Benutzers
     * @return Der generierte JWT-Token als String
     */
    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userId))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
    }

    /**
     * Extrahiert die Benutzer-ID aus einem JWT-Token
     * @param token Der JWT-Token
     * @return Die Benutzer-ID als Long
     */
    Long getUserIdFromJWT(String token){
        // Token parsen und Claims extrahieren
        Claims claims = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        // Subject (Benutzer-ID) aus den Claims extrahieren und in Long konvertieren
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validiert einen JWT-Token auf Gültigkeit und Ablaufzeit
     * @param token Der zu validierende JWT-Token
     * @return true wenn der Token gültig ist, false andernfalls
     */
    boolean validateToken(String token) {
        try {
            // Token parsen - wenn erfolgreich, ist es ein von uns generierter Token
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
            // Prüfen ob Token nicht abgelaufen ist
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            return false;  // Ungültige Signatur
        } catch (MalformedJwtException e) {
            return false;  // Fehlerhaft formatierter Token
        } catch (ExpiredJwtException e) {
            return false;  // Abgelaufener Token
        } catch (UnsupportedJwtException e) {
            return false;  // Nicht unterstützter Token-Typ
        } catch (IllegalArgumentException e) {
            return false;  // Ungültige Argumente
        }
    }

    /**
     * Prüft ob ein Token abgelaufen ist
     * @param token Der zu prüfende JWT-Token
     * @return true wenn der Token abgelaufen ist, false andernfalls
     */
    private boolean isTokenExpired(String token) {
        // Ablaufzeit aus dem Token extrahieren
        Date expiration = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        // Prüfen ob Ablaufzeit vor der aktuellen Zeit liegt
        return expiration.before(new Date());
    }


}
