package com.example.questionapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

/**
 * JWT Authentifizierungs-Entry-Point für die Behandlung von nicht autorisierten Anfragen
 * Implementiert AuthenticationEntryPoint um 401 Fehler bei ungültiger Authentifizierung zu senden
 */
@Component  // Ermöglicht Spring, diese Klasse als Bean zu injizieren
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Wird aufgerufen wenn eine nicht autorisierte Anfrage empfangen wird
     * Sendet eine 401 Unauthorized HTTP-Antwort zurück
     * @param request Die HTTP-Anfrage
     * @param response Die HTTP-Antwort 
     * @param authException Die Authentifizierungsausnahme
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Sende 401 Unauthorized Fehler mit der Ausnahmemeldung zurück
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
