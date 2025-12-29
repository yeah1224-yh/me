package com.diro.ift2255;

import io.javalin.Javalin;
import com.diro.ift2255.config.Routes;
import com.diro.ift2255.util.DatabaseUtil;

public class Main {
    public static void main(String[] args) {
        // Initialiser la base de données
        DatabaseUtil.initDatabase();
        DatabaseUtil.importStatsFromCsvIfEmpty();
        
        // Créer l'application Javalin
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });        
        });

        // Enregistrer les routes
        Routes.register(app);

        // Démarrer le serveur
        app.start(7070);
        
        System.out.println("Serveur démarré sur http://localhost:7070");
    }
}