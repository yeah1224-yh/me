// src/main/java/com/diro/ift2255/service/CourseService.java
package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.util.*;

/**
 * Service métier pour la récupération des cours depuis l'API Planifium
 * Gère les appels HTTP et le mapping JSON → objets Java
 */
public class CourseService {

    /* Client HTTP pour les appels API externes */
    private final HttpClientApi clientApi;

    /* URL de base de l'API Planifium */
    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1/courses";

    /**
     * Constructeur avec injection de dépendance
     */
    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
    }

    /**
     * Recherche de cours avec filtres (sigle, name, description, etc.)
     * OBLIGATOIRE : toujours appeler avec au moins un filtre (ex: sigle=IFT)
     * pour éviter de charger tout le catalogue universitaire
     */
    public List<Course> getCourses(Map<String, String> queryParams) {
        /* Normalisation des paramètres */
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        
        /* Construction de l'URL avec query params */
        URI uri = HttpClientApi.buildUri(BASE_URL, params);

        /* Appel API et désérialisation JSON → List<Course> */
        return clientApi.get(uri, new TypeReference<List<Course>>() {});
    }

    /**
     * Récupère un cours spécifique par ID avec paramètres optionnels
     * (response_level=full, include_schedule=true, etc.)
     */
    public Optional<Course> getCourseById(String courseId, Map<String, String> queryParams) {
        /* Normalisation des paramètres */
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        
        /* Construction de l'URL */
        URI uri = HttpClientApi.buildUri(BASE_URL + "/" + courseId, params);

        try {
            /* Appel API et désérialisation JSON → Course */
            Course course = clientApi.get(uri, Course.class);
            return Optional.of(course);
        } catch (RuntimeException e) {
            /* Erreur API → Optional vide */
            return Optional.empty();
        }
    }

    /**
     * Récupère un cours avec ses prérequis complets
     * Utilise response_level=full pour inclure tous les détails
     * Les prérequis sont disponibles dans requirement_text/prerequisite_courses
     */
    public Optional<Course> getCourseWithPrerequisites(String courseId) {
        /* Paramètres pour réponse complète */
        Map<String, String> params = Map.of("response_level", "full");
        return getCourseById(courseId, params);
    }

    /**
     * Récupère le graphe de dépendances d'un ensemble de cours
     * (prérequis + cours qui requièrent ces cours)
     * Retourne Map<String,Object> (JSON brut décodé)
     */
    public Map<String, Object> getCoursesDependency(Map<String, String> queryParams) {
        /* URL spécifique pour les dépendances */
        URI uri = HttpClientApi.buildUri(
                "https://planifium-api.onrender.com/api/v1/courses-dependency",
                queryParams
        );

        /* Désérialisation JSON → Map générique */
        return clientApi.get(uri, new TypeReference<Map<String, Object>>() {});
    }
}
