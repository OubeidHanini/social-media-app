package com.example.questionapp.controllers;

import com.example.questionapp.dataAccess.UserRepository;
import com.example.questionapp.entities.RefreshToken;
import com.example.questionapp.entities.User;
import com.example.questionapp.requests.RefreshTokenRequest;
import com.example.questionapp.requests.UserRequest;
import com.example.questionapp.responses.AuthenticationResponse;
import com.example.questionapp.security.JWTTokenProvider;
import com.example.questionapp.services.RefreshTokenService;
import com.example.questionapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody UserRequest loginRequest){
        // Benutzer-Anmeldeinformationen in ein Authentication-Token umwandeln
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        // JWT-Token für den authentifizierten Benutzer generieren
        String jwtToken = jwtTokenProvider.generateJWTToken(auth);
        User user = userService.getUserByUsername(loginRequest.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken("Bearer " + jwtToken);
        authenticationResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authenticationResponse.setUserId(user.getId());
        return authenticationResponse;
    }
   /**
    * Benutzerregistrierung - erstellt einen neuen Benutzer im System
    * @param registerRequest Registrierungsanfrage mit Benutzerdaten
    * @return ResponseEntity mit Authentifizierungsantwort und HTTP-Status
    */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequest registerRequest){
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        // Prüfen ob ein Benutzer mit diesem Benutzernamen bereits existiert
        if(userService.getUserByUsername(registerRequest.getUsername()) != null){
            authenticationResponse.setMessage("Username already taken.");
            return new ResponseEntity<>(authenticationResponse, HttpStatus.BAD_REQUEST);
        }
        // Neuen Benutzer erstellen und Passwort verschlüsseln
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.createUser(user);
        
        // Automatisches Login nach erfolgreicher Registrierung
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(registerRequest.getUsername(),registerRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtTokenProvider.generateJWTToken(auth);

        authenticationResponse.setMessage("User Successfully Registered");
        authenticationResponse.setAccessToken("Bearer "+jwtToken);
        authenticationResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authenticationResponse.setUserId(user.getId());
        return new ResponseEntity<>(authenticationResponse,HttpStatus.CREATED);
    }

    /**
     * Token-Erneuerung - erneuert einen abgelaufenen Access-Token mit dem Refresh-Token
     * @param refreshTokenRequest Anfrage mit Benutzer-ID und Refresh-Token
     * @return ResponseEntity mit neuem Access-Token oder Fehlermeldung
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshTokenRequest.getUserId());
        
        // Prüfen ob Refresh-Token gültig und nicht abgelaufen ist
        if(token.getToken().equals(refreshTokenRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)) {
            User user = token.getUser();
            // Neuen Access-Token für den Benutzer generieren
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
            authResponse.setMessage("token successfully refreshed.");
            authResponse.setAccessToken("Bearer " + jwtToken);
            authResponse.setUserId(user.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } else {
            authResponse.setMessage("refresh token is not valid.");
            return new ResponseEntity<>(authResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}
