package com.example.questionapp.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.example.questionapp.security.JWTAuthenticationEntryPoint;
import com.example.questionapp.security.JWTAuthenticationFilter;
import com.example.questionapp.services.UserDetailsServiceImplementation;

/**
 * Spring Security Konfigurationsklasse
 * Konfiguriert die Sicherheitseinstellungen für die gesamte Anwendung
 * einschließlich JWT-Authentifizierung, CORS-Richtlinien und Endpunkt-Berechtigungen
 * Ermöglicht automatische Erstellung und Konfiguration aller Sicherheitskomponenten
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    /** Service für Benutzerdetails und Authentifizierung */
    private UserDetailsServiceImplementation userDetailsService;

    /** Handler für Authentifizierungsausnahmen */
    private JWTAuthenticationEntryPoint handler;

    /**
     * Konstruktor für Dependency Injection
     * @param userDetailsService Service für das Laden von Benutzerdetails
     * @param handler Entry Point für die Behandlung von Authentifizierungsfehlern
     */
    public SecurityConfiguration(UserDetailsServiceImplementation userDetailsService, JWTAuthenticationEntryPoint handler) {
        this.userDetailsService = userDetailsService;
        this.handler = handler;
    }

    /**
     * Bean für JWT-Authentifizierungsfilter
     * @return Neuer JWT-Authentifizierungsfilter
     */
    @Bean
    public JWTAuthenticationFilter JWTAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

    /**
     * Bean für Passwort-Verschlüsselung
     * @return BCrypt-Passwort-Encoder für sichere Passwort-Hashes
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean für Authentifizierungsmanager
     * @param authenticationConfiguration Spring Security Konfiguration
     * @return Authentifizierungsmanager für die Benutzeranmeldung
     * @throws Exception Bei Konfigurationsfehlern
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Bean für CORS-Filter (Cross-Origin Resource Sharing)
     * Ermöglicht Anfragen von verschiedenen Domains und Ports
     * @return Konfigurierter CORS-Filter für Frontend-Integration
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // CORS-Konfiguration für Frontend-Integration
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");           // Alle Origins erlauben (für Entwicklung)
        config.addAllowedHeader("*");           // Alle Header erlauben
        config.addAllowedMethod("OPTIONS");     // Preflight-Anfragen
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Hauptkonfiguration der Sicherheitsfilter-Kette
     * Definiert welche Endpunkte öffentlich zugänglich sind und welche Authentifizierung benötigen
     * @param httpSecurity HTTP-Sicherheitskonfiguration
     * @return Konfigurierte Sicherheitsfilter-Kette
     * @throws Exception Bei Konfigurationsfehlern
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CORS-Unterstützung aktivieren - verwendet den oben definierten CorsFilter
                .cors()
                .and()
                // CSRF-Schutz deaktivieren (für REST-APIs mit JWT und Postman-Tests)
                .csrf().disable()
                // Custom Exception Handler für Authentifizierungsfehler
                .exceptionHandling().authenticationEntryPoint(handler).and()
                // Stateless Session Management (kein Server-seitiger Session-Speicher)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // Autorisierungsregeln definieren
                .authorizeHttpRequests()
                    // Authentifizierungsendpunkte öffentlich zugänglich (kein Token erforderlich)
                    .requestMatchers("/auth/**").permitAll()
                    // GET-Anfragen für Posts öffentlich (zum Lesen ohne Login)
                    .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                    // GET-Anfragen für Kommentare öffentlich
                    .requestMatchers(HttpMethod.GET, "/comments").permitAll()
                    // Alle anderen Anfragen erfordern Authentifizierung
                    .anyRequest().authenticated();

        // JWT-Filter vor dem Standard-Authentifizierungsfilter hinzufügen
        httpSecurity.addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return httpSecurity.build();
    }

}
