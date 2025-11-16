package com.example.questionapp.responses;


import com.example.questionapp.entities.Like;
import com.example.questionapp.entities.Post;
import lombok.Data;

import java.util.List;

/**
 * Response-DTO für Post-Daten
 * Diese Klasse wird verwendet um nur gewünschte Post-Informationen zurückzugeben
 * und sensible Daten wie Passwörter auszuschließen
 * 
 * Beispiel JSON: {"id": 102, "userId": 1, "username": "ysk", "text": "Post Inhalt", "title": "Post Titel"}
 */
@Data
public class PostResponse {

    /** Eindeutige ID des Posts */
    private Long id;
    
    /** ID des Benutzers der den Post erstellt hat */
    private Long userId;
    
    /** Benutzername des Post-Erstellers */
    private String username;
    
    /** Textinhalt des Posts */
    private String text;
    
    /** Titel des Posts */
    private String title;

    /** Liste der Likes die dieser Post erhalten hat */
    private List<LikeResponse> postLikes;
    
    /**
     * Konstruktor für die Umwandlung einer Post-Entität in ein Response-DTO
     * Mapping von Entity-Daten zu Response-Objekten
     * 
     * @param post Die Post-Entität aus der Datenbank
     * @param likes Liste der Like-Responses für diesen Post
     */
    public PostResponse(Post post, List<LikeResponse> likes){
       this.id = post.getId();
       this.userId = post.getUser().getId();
       this.username = post.getUser().getUsername();
       this.title = post.getTitle();
       this.text = post.getText();
       this.postLikes = likes;
    }
}
