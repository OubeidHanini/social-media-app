package com.example.questionapp.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

/**
 * Post-Entität für die Darstellung von Beiträgen in der Social Media Anwendung
 * Repräsentiert einen Beitrag eines Benutzers mit Titel, Text und Erstellungsdatum
 */
@Entity
@Data
@Table(name = "post")
public class Post {
    
    /** Eindeutige ID des Posts */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 
     * Benutzer der diesen Post erstellt hat
     * Many-to-One Beziehung: Ein Benutzer kann viele Posts haben
     * Cascade: Wenn der Benutzer gelöscht wird, werden auch seine Posts gelöscht
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /** Titel des Posts */
    private String title;

    /** 
     * Textinhalt des Posts
     * @Lob für große Textmengen, gespeichert als TEXT-Typ in der Datenbank
     */
    @Lob
    @Column(columnDefinition = "text")
    private String text;

    /** 
     * Erstellungsdatum des Posts
     * Automatisch gesetzt beim Speichern mit Zeitstempel
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;


}
