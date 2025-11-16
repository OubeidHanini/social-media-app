package com.example.questionapp.services;


import com.example.questionapp.dataAccess.UserRepository;
import com.example.questionapp.entities.User;
import com.example.questionapp.security.JWTUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementierung des UserDetailsService für Spring Security
 * Lädt Benutzerdetails für die Authentifizierung aus der Datenbank
 */
@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Konstruktor für Dependency Injection
     * @param userRepository Repository für Benutzer-Datenbankoperationen
     */
    public UserDetailsServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Lädt Benutzerdetails anhand des Benutzernamens (Standard Spring Security Methode)
     * @param username Der Benutzername
     * @return UserDetails-Objekt des gefundenen Benutzers
     * @throws UsernameNotFoundException wenn der Benutzer nicht gefunden wird
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        // User-Entity in UserDetails-Objekt für Spring Security konvertieren
        return JWTUserDetails.create(user);
    }

    /**
     * Lädt Benutzerdetails anhand der Benutzer-ID (für JWT-Token-Validierung)
     * @param id Die Benutzer-ID
     * @return UserDetails-Objekt des gefundenen Benutzers
     */
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).get();
        return JWTUserDetails.create(user);
    }

}
