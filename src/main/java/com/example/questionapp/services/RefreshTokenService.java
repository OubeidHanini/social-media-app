package com.example.questionapp.services;

import com.example.questionapp.dataAccess.RefreshTokenRepository;
import com.example.questionapp.entities.RefreshToken;
import com.example.questionapp.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Service für die Verwaltung von Refresh-Tokens
 * Erstellt, validiert und verwaltet Refresh-Tokens für die JWT-Authentifizierung
 */
@Service
public class RefreshTokenService {

    @Value("${refresh.token.expires.in}")  // Gültigkeitsdauer in Sekunden aus application.properties
    Long expireSeconds;

    private RefreshTokenRepository refreshTokenRepository;

    /**
     * Konstruktor für Dependency Injection
     * @param refreshTokenRepository Repository für Refresh-Token-Datenbankoperationen
     */
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Erstellt oder aktualisiert einen Refresh-Token für einen Benutzer
     * @param user Der Benutzer für den der Token erstellt werden soll
     * @return Der Token-String des erstellten Refresh-Tokens
     */
    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        // Wenn kein Token existiert, neuen Token erstellen
        if(token == null) {
            token = new RefreshToken();
            token.setUser(user);
        }
        // Eindeutigen Token-String mit UUID generieren
        token.setToken(UUID.randomUUID().toString());
        // Ablaufzeit auf aktuelle Zeit + konfigurierte Sekunden setzen
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    /**
     * Sucht einen Refresh-Token anhand der Benutzer-ID
     * @param userId Die ID des Benutzers
     * @return Der gefundene Refresh-Token oder null
     */
    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    /**
     * Prüft ob ein Refresh-Token abgelaufen ist
     * @param token Der zu prüfende Refresh-Token
     * @return true wenn der Token abgelaufen ist, false andernfalls
     */
    public boolean isRefreshExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

}