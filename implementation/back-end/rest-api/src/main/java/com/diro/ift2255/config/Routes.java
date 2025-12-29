// src/main/java/com/diro/ift2255/config/Routes.java
package com.diro.ift2255.config;

import com.diro.ift2255.controller.AvisController;
import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.UserController;
import com.diro.ift2255.controller.PreferenceController;

import com.diro.ift2255.service.AvisService;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.UserService;
import com.diro.ift2255.service.PreferenceService;
import com.diro.ift2255.service.StatsCoursService;

import com.diro.ift2255.util.HttpClientApi;
import io.javalin.Javalin;

/**
 * Configuration des routes de l'API REST
 * Enregistre tous les endpoints des contrôleurs
 */
public class Routes {

    /**
     * Enregistre toutes les routes sur l'application Javalin
     *
     * @param app Instance Javalin
     */
    public static void register(Javalin app) {

        // ---------- Services partagés ----------
        HttpClientApi httpClientApi = new HttpClientApi();              // Client HTTP pour API externe
        AvisService avisService = new AvisService();                    // Service gestion des avis
        CourseService courseService = new CourseService(httpClientApi); // Service des cours
        UserService userService = new UserService();                    // Service utilisateurs
        PreferenceService preferenceService = new PreferenceService();  // Service préférences
        StatsCoursService statsCoursService = new StatsCoursService();  // Service statistiques cours

        // ---------- Contrôleurs ----------
        UserController userController = new UserController(userService);
        CourseController courseController =
                new CourseController(courseService, avisService, statsCoursService);
        AvisController avisController = new AvisController(avisService);
        PreferenceController preferenceController =
                new PreferenceController(preferenceService);

        // ---------- ROUTES UTILISATEURS ----------
        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.post("/users", userController::createUser);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);

        // ---------- ROUTES COURS ----------
        app.get("/courses", courseController::getCourses);
        app.get("/courses/{id}", courseController::getCourseById);
        app.get("/courses/{id}/prerequisites", courseController::getCoursePrerequisites);
        app.get("/courses-dependency", courseController::getCoursesDependency);
        app.get("/courses/{id}/stats", courseController::getCourseStats);

        // ---------- ROUTES AVIS ----------
        app.get("/courses/{id}/avis", avisController::getAvisPourCours);
        app.post("/avis", avisController::creerAvis);

        // ---------- ROUTES PREFERENCES ----------
        app.post("/preferences", preferenceController::saveOrUpdate);
        app.get("/preferences/{clientId}", preferenceController::getByClientId);
    }
}
