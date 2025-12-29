// src/main/java/com/diro/ift2255/model/Opinion.java
package com.diro.ift2255.model;

/**
 * Modèle de données pour une opinion brute provenant du bot Discord
 * Correspond exactement au format JSON envoyé par le script Python du bot
 */
public class Opinion {

    /* Métadonnées Discord */
    public String message_id;      // ID unique du message Discord
    public String guild_id;        // ID du serveur Discord
    public String channel_id;      // ID du canal Discord
    public String channel_name;    // Nom du canal Discord
    
    /* Informations auteur */
    public String author_id;       // ID Discord de l'auteur
    public String author_name;     // Nom d'utilisateur Discord
    
    /* Horodatage */
    public String created_at;      // Date/heure de création (ISO format)
    
    /* Contenu */
    public String text;            // Texte complet de l'avis
    public String course_code;     // Code du cours (ex: "IFT2255")
    public String professor_name;  // Nom du professeur

    /**
     * Constructeur par défaut requis par Jackson pour la désérialisation JSON
     */
    public Opinion() {
    }

    /**
     * Constructeur complet (optionnel, pour création manuelle)
     */
    public Opinion(
            String message_id,
            String guild_id,
            String channel_id,
            String channel_name,
            String author_id,
            String author_name,
            String created_at,
            String text,
            String course_code,
            String professor_name
    ) {
        this.message_id = message_id;
        this.guild_id = guild_id;
        this.channel_id = channel_id;
        this.channel_name = channel_name;
        this.author_id = author_id;
        this.author_name = author_name;
        this.created_at = created_at;
        this.text = text;
        this.course_code = course_code;
        this.professor_name = professor_name;
    }
}
