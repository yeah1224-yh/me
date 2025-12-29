// src/main/java/com/diro/ift2255/model/Preference.java
package com.diro.ift2255.model;

/**
 * Modèle de données pour les préférences utilisateur
 * Stockées en base de données avec ID client anonyme
 */
public class Preference {

    /* Champs de la table preferences */
    private Integer id;              // ID primaire auto-généré
    private String clientId;         // ID unique généré côté frontend (localStorage)
    private String langue;           // Langue préférée ("fr", "en")
    private String programme;        // Programme suivi (ex: "Baccalauréat en informatique")

    /**
     * Constructeur par défaut requis par Jackson/ORM
     */
    public Preference() {
    }

    /**
     * Constructeur complet
     */
    public Preference(Integer id, String clientId, String langue, String programme) {
        this.id = id;
        this.clientId = clientId;
        this.langue = langue;
        this.programme = programme;
    }

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS
    // --------------------------------------------------------------------

    /* ID primaire de la préférence */
    public Integer getId() { 
        return id; 
    }
    public void setId(Integer id) { 
        this.id = id; 
    }

    /* ID client anonyme (clé de liaison avec frontend) */
    public String getClientId() { 
        return clientId; 
    }
    public void setClientId(String clientId) { 
        this.clientId = clientId; 
    }

    /* Langue préférée de l'utilisateur */
    public String getLangue() { 
        return langue; 
    }
    public void setLangue(String langue) { 
        this.langue = langue; 
    }

    /* Programme académique suivi */
    public String getProgramme() { 
        return programme; 
    }
    public void setProgramme(String programme) { 
        this.programme = programme; 
    }
}
