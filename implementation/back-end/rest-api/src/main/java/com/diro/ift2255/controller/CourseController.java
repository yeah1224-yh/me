// src/main/java/com/diro/ift2255/controller/CourseController.java
package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.service.StatsCoursService;
import com.diro.ift2255.service.StatsCoursService.CoursStats;
import com.diro.ift2255.util.ResponseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des cours
 * Gère les endpoints /courses, /courses/{id}, statistiques, prérequis, etc.
 */
public class CourseController {

    /* Services injectés */
    private final CourseService service;               // Service principal des cours
    private final AvisService avisService;             // Service des avis (pour nbAvis)
    private final StatsCoursService statsCoursService; // Service des statistiques

    /**
     * Constructeur avec injection de dépendances
     */
    public CourseController(CourseService service,
                            AvisService avisService,
                            StatsCoursService statsCoursService) {
        this.service = service;
        this.avisService = avisService;
        this.statsCoursService = statsCoursService;
    }

    /**
     * GET /courses
     * Recherche de cours avec filtres (sigle, name, description)
     */
    public void getCourses(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        if (queryParams.isEmpty() ||
                (!queryParams.containsKey("sigle") &&
                 !queryParams.containsKey("name") &&
                 !queryParams.containsKey("description"))) {

            ctx.status(400).json(
                    ResponseUtil.formatError(
                            "Au moins un paramètre de filtre (sigle, name ou description) est requis pour la recherche de cours."
                    )
            );
            return;
        }

        List<Course> courses = service.getCourses(queryParams);

        // Ajout du nombre d'avis pour chaque cours
        for (Course c : courses) {
            int count = avisService.countAvisForCourse(c.getId());
            c.setNbAvis(count);
        }

        ctx.json(courses);
    }

    /**
     * GET /courses/{id}
     * Récupère les détails complets d'un cours spécifique
     */
    public void getCourseById(Context ctx) {
        String id = ctx.pathParam("id");

        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Map<String, String> queryParams = extractQueryParams(ctx);

        Optional<Course> course = service.getCourseById(id, queryParams);
        if (course.isPresent()) {
            Course c = course.get();
            int count = avisService.countAvisForCourse(c.getId());
            c.setNbAvis(count);
            ctx.json(c);
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun cours ne correspond à l'ID: " + id));
        }
    }

    /**
     * GET /courses/{id}/prerequisites
     * Récupère les prérequis d'un cours
     */
    public void getCoursePrerequisites(Context ctx) {
        String id = ctx.pathParam("id");

        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Optional<Course> courseOpt = service.getCourseWithPrerequisites(id);

        if (courseOpt.isEmpty()) {
            ctx.status(404).json(
                    ResponseUtil.formatError("Impossible de récupérer les informations complètes pour le cours: " + id)
            );
            return;
        }

        ctx.json(courseOpt.get());
    }

    /**
     * GET /courses-dependency
     * Graphe de dépendances (prérequis + cours qui le requièrent)
     */
    public void getCoursesDependency(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        if (!queryParams.containsKey("course_ids")) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre course_ids est requis."));
            return;
        }

        Map<String, Object> graph = service.getCoursesDependency(queryParams);
        ctx.json(graph);
    }

    /**
     * GET /courses/{id}/stats (NOUVEAU)
     * Statistiques académiques d'un cours (moyenne, taux de réussite, etc.)
     */
    public void getCourseStats(Context ctx) {
        String id = ctx.pathParam("id");
        System.out.println("[CourseController] GET /courses/" + id + "/stats");

        if (!validateCourseId(id)) {
            System.out.println("[CourseController] ID invalide: '" + id + "'");
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        System.out.println("[CourseController] ID valide, appel statsCoursService.getStatsForCourse(" + id + ")");

        // Récupération des statistiques
        CoursStats stats = statsCoursService.getStatsForCourse(id);
        if (stats == null) {
            System.out.println("[CourseController] AUCUNE statistique trouvée pour: " + id);
            ctx.status(404);
            ctx.json(
                    ResponseUtil.formatError("Aucune statistique trouvée pour le cours: " + id)
            );
        } else {
            System.out.println("[CourseController] Stats trouvées pour " + id + " -> " + stats);
            ctx.json(stats);
        }
    }

    // --------------------------------------------------------------------
    // Méthodes utilitaires internes
    // --------------------------------------------------------------------

    /**
     * Validation d'un ID de cours (minimum 6 caractères)
     */
    private boolean validateCourseId(String courseId) {
        return courseId != null && courseId.trim().length() >= 6;
    }

    /**
     * Extraction des paramètres de requête HTTP en Map
     */
    private Map<String, String> extractQueryParams(Context ctx) {
        Map<String, String> queryParams = new HashMap<>();
        ctx.queryParamMap().forEach((key, values) -> {
            if (!values.isEmpty()) {
                queryParams.put(key, values.get(0));
            }
        });
        return queryParams;
    }
}
