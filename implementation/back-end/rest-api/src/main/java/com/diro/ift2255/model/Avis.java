// src/main/java/com/diro/ift2255/model/Avis.java
package com.diro.ift2255.model;

/**
 * Modèle de données pour un avis sur un cours
 * Représente un commentaire utilisateur avec évaluations
 */
public class Avis {

    /* Champs de l'avis */
    private int id;                    // ID unique de l'avis (auto-généré)
    private String coursId;            // ID du cours (ex: "IFT2255")
    private String auteur;             // Nom de l'auteur (anonyme ou pseudo)
    private String texte;              // Contenu du commentaire
    private Integer note;              // Note globale (1-5, optionnelle)
    private String session;            // Session (ex: "A24", "H25", optionnelle)
    private Integer volumeTravail;     // Volume de travail perçu (1-5, optionnelle)
    private Integer difficulte;        // Difficulté perçue (1-5, optionnelle)

    /* Constructeur par défaut */
    public Avis() {
    }

    /* Constructeur complet */
    public Avis(int id, String coursId, String auteur, String texte,
                Integer note, String session, Integer volumeTravail, Integer difficulte) {
        this.id = id;
        this.coursId = coursId;
        this.auteur = auteur;
        this.texte = texte;
        this.note = note;
        this.session = session;
        this.volumeTravail = volumeTravail;
        this.difficulte = difficulte;
    }

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS
    // --------------------------------------------------------------------

    /* ID unique de l'avis */
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    /* ID du cours concerné */
    public String getCoursId() { 
        return coursId; 
    }
    public void setCoursId(String coursId) { 
        this.coursId = coursId; 
    }

    /* Auteur de l'avis */
    public String getAuteur() { 
        return auteur; 
    }
    public void setAuteur(String auteur) { 
        this.auteur = auteur; 
    }

    /* Contenu textuel de l'avis */
    public String getTexte() { 
        return texte; 
    }
    public void setTexte(String texte) { 
        this.texte = texte; 
    }

    /* Note globale (1-5) */
    public Integer getNote() { 
        return note; 
    }
    public void setNote(Integer note) { 
        this.note = note; 
    }

    /* Session du cours (ex: "A24", "H25") */
    public String getSession() { 
        return session; 
    }
    public void setSession(String session) { 
        this.session = session; 
    }

    /* Volume de travail perçu (1-5) */
    public Integer getVolumeTravail() { 
        return volumeTravail; 
    }
    public void setVolumeTravail(Integer volumeTravail) { 
        this.volumeTravail = volumeTravail; 
    }

    /* Difficulté perçue (1-5) */
    public Integer getDifficulte() { 
        return difficulte; 
    }
    public void setDifficulte(Integer difficulte) { 
        this.difficulte = difficulte; 
    }
}
