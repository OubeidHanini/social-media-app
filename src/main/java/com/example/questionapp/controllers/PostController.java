package com.example.questionapp.controllers;

import com.example.questionapp.entities.Post;
import com.example.questionapp.requests.CreatePostRequest;
import com.example.questionapp.requests.UpdatePostRequest;
import com.example.questionapp.responses.PostResponse;
import com.example.questionapp.services.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller für Post-Verwaltung
 * Verwaltet alle HTTP-Endpunkte für CRUD-Operationen von Posts
 */
@RestController
@RequestMapping("/posts")
public class PostController {
    private PostService postService;

    /**
     * Konstruktor für Dependency Injection
     * @param postService Service für Post-Geschäftslogik
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Ruft alle Posts ab, optional gefiltert nach Benutzer-ID
     * @param userId Optionale Benutzer-ID als Query-Parameter (posts?userId=123)
     * @return Liste aller Posts oder Posts eines spezifischen Benutzers
     */
    @GetMapping  // RequestParam => posts?userId=userId - liest Parameter aus der URL-Abfrage
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId) {
        // Wenn keine userId angegeben ist, werden alle Posts zurückgegeben
        return postService.getAllPosts(userId);
    }

    /**
     * Erstellt einen neuen Post
     * @param newPostRequest Request-Objekt mit den Post-Daten
     * @return Der erstellte Post
     */
    @PostMapping
    public Post createPost(@RequestBody CreatePostRequest newPostRequest) {
        return postService.createPost(newPostRequest);
    }

    /*
     * Alternative Implementierung für einfache Post-Rückgabe
     * @GetMapping("/{postId}")
     * public Post getPostById(@PathVariable Long postId) {
     *     return postService.getPostById(postId);
     * }
     */

    /**
     * Ruft einen Post anhand der ID ab, inklusive Likes
     * @param postId Die ID des gewünschten Posts (aus dem URL-Pfad: posts/123)
     * @return Post-Response mit Details und Likes
     */
    @GetMapping("/{postId}")  // PathVariable => posts/postId - liest ID direkt aus dem URL-Pfad
    public PostResponse getPostById(@PathVariable Long postId) {
        return postService.getPostByIdWithLikes(postId);
    }

    /**
     * Aktualisiert einen existierenden Post
     * @param postId Die ID des zu aktualisierenden Posts
     * @param updatePostRequest Request-Objekt mit den neuen Post-Daten
     * @return Der aktualisierte Post
     */
    @PutMapping("/{postId}")
    public Post updatePostById(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest){
        return postService.updatePostById(postId, updatePostRequest);
    }

    /**
     * Löscht einen Post anhand der ID
     * @param postId Die ID des zu löschenden Posts
     */
    @DeleteMapping("/{postId}")
    public void deletePostById(@PathVariable Long postId){
        postService.deletePostById(postId);
    }

}
