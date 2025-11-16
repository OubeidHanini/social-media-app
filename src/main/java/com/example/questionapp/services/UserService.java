package com.example.questionapp.services;


import com.example.questionapp.dataAccess.CommentRepository;
import com.example.questionapp.dataAccess.LikeRepository;
import com.example.questionapp.dataAccess.PostRepository;
import com.example.questionapp.dataAccess.UserRepository;
import com.example.questionapp.entities.Comment;
import com.example.questionapp.entities.Like;
import com.example.questionapp.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service für Benutzerverwaltung und Benutzeraktivitäten
 * Verwaltet CRUD-Operationen für Benutzer und deren Aktivitäten (Posts, Likes, Kommentare)
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private LikeRepository likeRepository;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    /**
     * Konstruktor für Dependency Injection aller benötigten Repositories
     * @param userRepository Repository für Benutzer-Operationen
     * @param likeRepository Repository für Like-Operationen
     * @param commentRepository Repository für Kommentar-Operationen
     * @param postRepository Repository für Post-Operationen
     */
    public UserService(UserRepository userRepository, LikeRepository likeRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }


    /**
     * Gibt alle Benutzer zurück
     * @return Liste aller Benutzer
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Erstellt einen neuen Benutzer
     * @param user Der zu erstellende Benutzer
     * @return Der gespeicherte Benutzer
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Sucht einen Benutzer anhand der ID
     * @param userId Die ID des gesuchten Benutzers
     * @return Der gefundene Benutzer oder null wenn nicht vorhanden
     */
    public User getUserById(Long userId) {
        // Benutzer könnte in der Datenbank nicht existieren - Kontrolle erforderlich
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Aktualisiert einen existierenden Benutzer
     * @param userId Die ID des zu aktualisierenden Benutzers
     * @param newUser Das Benutzer-Objekt mit den neuen Daten
     * @return Der aktualisierte Benutzer oder null wenn nicht gefunden
     */
    public User updateUserById(Long userId, User newUser) {
        Optional<User> user = userRepository.findById(userId);
        // Prüfen ob der Benutzer existiert
        if (user.isPresent()){
            User foundUser = user.get();
            foundUser.setUsername(newUser.getUsername());
            foundUser.setPassword(newUser.getPassword());
            foundUser.setImage(newUser.getImage());
            userRepository.save(foundUser);
            return foundUser;
        }else
            return null;
    }

    /**
     * Löscht einen Benutzer anhand der ID
     * @param userId Die ID des zu löschenden Benutzers
     */
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Sucht einen Benutzer anhand des Benutzernamens
     * @param username Der Benutzername
     * @return Der gefundene Benutzer oder null
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

   
}
