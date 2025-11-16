package com.example.questionapp.security;

import com.example.questionapp.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JWT Benutzerdetails - Implementiert UserDetails für Spring Security
 * Spring Security benötigt ein UserDetails-Objekt für Authentifizierungsprozesse
 * Diese Klasse wird für JWT-basierte Authentifizierung verwendet
 */
@Getter
@Setter
@Data
public class JWTUserDetails implements UserDetails {

    public Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Privater Konstruktor für JWTUserDetails
     * @param id Benutzer-ID
     * @param username Benutzername
     * @param password Passwort
     * @param authorities Benutzerberechtigungen
     */
    private JWTUserDetails(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Factory-Methode zur Erstellung von JWTUserDetails aus einem User-Objekt
     * @param user Das User-Entitätsobjekt
     * @return Ein UserDetails-Objekt für Spring Security
     */
    public static JWTUserDetails create(User user) {
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        // Standard-Berechtigung "user" zuweisen (kein Admin-System implementiert)
        authoritiesList.add(new SimpleGrantedAuthority("user"));
        return new JWTUserDetails(user.getId(), user.getUsername(), user.getPassword(), authoritiesList);
    }

    /** @return true - Konto ist nicht abgelaufen */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** @return true - Konto ist nicht gesperrt */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** @return true - Anmeldedaten sind nicht abgelaufen */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** @return true - Konto ist aktiviert */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
