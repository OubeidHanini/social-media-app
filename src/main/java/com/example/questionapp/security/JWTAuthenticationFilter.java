package com.example.questionapp.security;

import com.example.questionapp.services.UserDetailsServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT Authentifizierungs-Filter der bei jeder HTTP-Anfrage ausgeführt wird
 * Überprüft ob die Anfrage einen gültigen JWT-Token enthält und authentifiziert den Benutzer
 * Erweitert OncePerRequestFilter um sicherzustellen, dass der Filter nur einmal pro Anfrage ausgeführt wird
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsServiceImplementation userDetailsServiceImplementation;

    /**
     * Hauptfilter-Methode die bei jeder HTTP-Anfrage ausgeführt wird
     * Extrahiert und validiert den JWT-Token, authentifiziert den Benutzer wenn gültig
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            // JWT-Token aus der HTTP-Anfrage extrahieren
            String jwtToken = extractJwtFromRequest(request);
            // Prüfen ob Token vorhanden und gültig ist
            if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                // Benutzer-ID aus dem Token extrahieren
                Long id = jwtTokenProvider.getUserIdFromJWT(jwtToken);
                // Benutzerdetails anhand der ID laden
                UserDetails user = userDetailsServiceImplementation.loadUserById(id);
                if(user != null){
                    // Authentifizierungs-Token erstellen
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Benutzer im SecurityContext authentifizieren - ab hier ist der Benutzer in der gesamten Anwendung verfügbar
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch(Exception e) {
            return;
        }
        // Filter-Kette fortsetzen
        filterChain.doFilter(request, response);
    }

    /**
     * Extrahiert den JWT-Token aus dem Authorization-Header der HTTP-Anfrage
     * @param request Die HTTP-Anfrage
     * @return Der JWT-Token ohne "Bearer " Präfix, oder null wenn nicht vorhanden
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // Authorization-Header aus der Anfrage lesen
        String bearer = request.getHeader("Authorization");
        // Prüfen ob Header vorhanden ist und mit "Bearer " beginnt
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            // "Bearer " Präfix entfernen und nur den Token zurückgeben
            return bearer.substring("Bearer".length()+1);
        }
        return null;
    }

}
