package com.example.questionapp.exceptions;

/**
 * Ausnahme die geworfen wird, wenn ein Benutzer nicht gefunden wurde
 * Erbt von RuntimeException für unchecked Exception-Behandlung
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Standard-Konstruktor ohne Nachricht
     * Ruft den Konstruktor der RuntimeException-Klasse auf
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Konstruktor mit benutzerdefinierten Fehlernachricht
     * @param message Die Fehlernachricht die angezeigt werden soll
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
