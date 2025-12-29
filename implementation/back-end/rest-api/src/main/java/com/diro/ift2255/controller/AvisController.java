// src/main/java/com/diro/ift2255/controller/AvisController.java
package com.diro.ift2255.controller;

import com.diro.ift2255.model.Avis;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.util.ResponseUtil;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des avis des cours
 * Gère les endpoints /courses/{id}/avis et /avis
 */
public class AvisController {

    /* Service d'affaires pour les avis */
    private final AvisService service;

    /**
     * Constructeur avec injection de dépendance
     * 
     * @param service Service AvisService
     */
    public AvisController(AvisService service) {
        this.service = service;
    }

    /**
     * GET /courses/{id}/avis
     * Consulte les avis pour un cours donné avec filtres optionnels
     * Exemple : GET /courses/IFT2255/avis?session=A24&volumeTravail=3&difficulte=4
     */
    public void getAvisPourCours(Context ctx) {
        /* Extraction de l'ID du cours depuis l'URL */
        String coursId = ctx.pathParam("id");

        /* Extraction des paramètres de filtre optionnels */
        String session = ctx.queryParam("session");
        String volumeTravailStr = ctx.queryParam("volumeTravail");
        String difficulteStr = ctx.queryParam("difficulte");

        /* Conversion en Optional pour les valeurs optionnelles */
        Optional<String> sessionOpt = Optional.ofNullable(session).filter(s -> !s.isBlank());
        Optional<Integer> volumeTravailOpt = parseIntOptional(volumeTravailStr);
        Optional<Integer> difficulteOpt = parseIntOptional(difficulteStr);

        /* Appel service avec filtres */
        List<Avis> avis = service.getAvisPourCours(
                coursId,
                sessionOpt,
                volumeTravailOpt,
                difficulteOpt
        );

        /* Réponse JSON */
        ctx.json(avis);
    }

    /**
     * POST /avis
     * Crée un nouvel avis pour un cours
     * Supporte 2 formats JSON :
     * 1) App mobile : {coursId, auteur, texte, note, ...}
     * 2) Bot Discord : {course_code, author_name, text, ...}
     */
    public void creerAvis(Context ctx) {
        /* Lecture du corps JSON comme Map générique */
        @SuppressWarnings("unchecked")
        Map<String, Object> raw = (Map<String, Object>) ctx.bodyAsClass(Map.class);

        /* Création de l'objet Avis */
        Avis avis = new Avis();

        /* Mapping flexible des champs obligatoires */
        // 1) coursId : "coursId" (app) ou "course_code" (bot)
        Object coursIdRaw = raw.getOrDefault("coursId", raw.get("course_code"));
        String coursId = coursIdRaw != null ? coursIdRaw.toString() : null;

        // 2) auteur : "auteur" (app) ou "author_name" (bot)
        Object auteurRaw = raw.getOrDefault("auteur", raw.get("author_name"));
        String auteur = auteurRaw != null ? auteurRaw.toString() : null;

        // 3) texte : "texte" (app) ou "text" (bot)
        Object texteRaw = raw.getOrDefault("texte", raw.get("text"));
        String texte = texteRaw != null ? texteRaw.toString() : null;

        /* Validation des champs obligatoires */
        if (coursId == null || coursId.isBlank() || texte == null || texte.isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError(
                    "Les champs coursId/course_code et texte/text sont obligatoires."
            ));
            return;
        }

        /* Remplissage des champs obligatoires */
        avis.setCoursId(coursId);
        avis.setAuteur(auteur);
        avis.setTexte(texte);

        /* Champs optionnels numériques (valeur par défaut = 0) */
        avis.setNote(parseIntOptionalObj(raw.get("note")).orElse(0));
        avis.setVolumeTravail(parseIntOptionalObj(raw.get("volumeTravail")).orElse(0));
        avis.setDifficulte(parseIntOptionalObj(raw.get("difficulte")).orElse(0));

        /* Champ optionnel session */
        Object sessionRaw = raw.get("session");
        if (sessionRaw != null) {
            avis.setSession(sessionRaw.toString());
        }

        /* Sauvegarde et réponse */
        Avis created = service.ajouterAvis(avis);
        ctx.status(201).json(created);
    }

    // --------------------------------------------------------------------
    // Méthodes utilitaires internes
    // --------------------------------------------------------------------

    /**
     * Parse une chaîne en Integer optionnel (null/empty → empty)
     */
    private Optional<Integer> parseIntOptional(String value) {
        try {
            return (value == null || value.isBlank())
                    ? Optional.empty()
                    : Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parse un Object en Integer optionnel (null → empty)
     */
    private Optional<Integer> parseIntOptionalObj(Object value) {
        if (value == null) return Optional.empty();
        try {
            return Optional.of(Integer.parseInt(value.toString()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
