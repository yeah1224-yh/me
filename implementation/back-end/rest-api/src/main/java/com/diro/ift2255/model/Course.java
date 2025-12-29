// src/main/java/com/diro/ift2255/model/Course.java
package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * Modèle de données pour un cours universitaire
 * Désérialise les réponses JSON de l'API externe
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {

    /* Informations principales du cours */
    private String id;                           // ID unique (ex: "IFT2255")
    private String name;                         // Nom du cours
    private String description;                  // Description détaillée
    private double credits;                      // Nombre de crédits

    /* Disponibilités */
    private Map<String, Boolean> available_terms;    // Sessions disponibles (automne, hiver, été)
    private Map<String, Boolean> available_periods;  // Périodes (jour, soir)

    /* Horaires */
    private List<Schedule> schedules;            // Liste des créneaux horaires

    /* Prérequis et relations */
    private String requirement_text;             // Texte des prérequis
    private List<String> prerequisite_courses;   // Liste des prérequis (IDs)
    private List<String> equivalent_courses;     // Cours équivalents
    private List<String> concomitant_courses;    // Cours concomitants

    /* Métadonnées ajoutées par l'app */
    private int nbAvis;                          // Nombre d'avis pour ce cours

    /* Constructeur par défaut */
    public Course() {}

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS - Informations principales
    // --------------------------------------------------------------------

    /* ID unique du cours */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /* Nom du cours */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /* Description du cours */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /* Nombre de crédits */
    public double getCredits() { return credits; }
    public void setCredits(double credits) { this.credits = credits; }

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS - Disponibilités
    // --------------------------------------------------------------------

    /* Sessions disponibles (automne=true, hiver=true, été=false) */
    public Map<String, Boolean> getAvailable_terms() { return available_terms; }
    public void setAvailable_terms(Map<String, Boolean> available_terms) { 
        this.available_terms = available_terms; 
    }

    /* Périodes disponibles (daytime=true, evening=false) */
    public Map<String, Boolean> getAvailable_periods() { return available_periods; }
    public void setAvailable_periods(Map<String, Boolean> available_periods) { 
        this.available_periods = available_periods; 
    }

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS - Horaires et prérequis
    // --------------------------------------------------------------------

    /* Liste des créneaux horaires */
    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }

    /* Texte descriptif des prérequis */
    public String getRequirement_text() { return requirement_text; }
    public void setRequirement_text(String requirement_text) { 
        this.requirement_text = requirement_text; 
    }

    /* Liste des cours prérequis (IDs) */
    public List<String> getPrerequisite_courses() { return prerequisite_courses; }
    public void setPrerequisite_courses(List<String> prerequisite_courses) { 
        this.prerequisite_courses = prerequisite_courses; 
    }

    /* Cours équivalents */
    public List<String> getEquivalent_courses() { return equivalent_courses; }
    public void setEquivalent_courses(List<String> equivalent_courses) { 
        this.equivalent_courses = equivalent_courses; 
    }

    /* Cours concomitants (peuvent être pris ensemble) */
    public List<String> getConcomitant_courses() { return concomitant_courses; }
    public void setConcomitant_courses(List<String> concomitant_courses) { 
        this.concomitant_courses = concomitant_courses; 
    }

    // --------------------------------------------------------------------
    // GETTERS ET SETTERS - Métadonnées app
    // --------------------------------------------------------------------

    /* Nombre d'avis pour ce cours (calculé dynamiquement) */
    public int getNbAvis() {
        return nbAvis;
    }

    public void setNbAvis(int nbAvis) {
        this.nbAvis = nbAvis;
    }

    // --------------------------------------------------------------------
    // Classe interne : créneau horaire
    // --------------------------------------------------------------------

    /**
     * Représente un créneau horaire (jour + heure début/fin)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        private String day;      // Jour (ex: "Lundi", "Mardi")
        private String start;    // Heure début (ex: "08:30")
        private String end;      // Heure fin (ex: "10:00")

        public Schedule() {}

        /* Jour de la semaine */
        public String getDay() { return day; }
        public void setDay(String day) { this.day = day; }

        /* Heure de début */
        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }

        /* Heure de fin */
        public String getEnd() { return end; }
        public void setEnd(String end) { this.end = end; }
    }
}
