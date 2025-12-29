// src/main/java/com/diro/ift2255/controller/PreferenceController.java
package com.diro.ift2255.controller;

import com.diro.ift2255.model.Preference;
import com.diro.ift2255.service.PreferenceService;
import com.diro.ift2255.util.ResponseUtil;
import io.javalin.http.Context;

/**
 * Contrôleur REST pour la gestion des préférences utilisateur
 * Gère les endpoints /preferences et /preferences/{clientId}
 */
public class PreferenceController {

    /* Service d'affaires pour les préférences */
    private final PreferenceService service;

    /**
     * Constructeur avec injection de dépendance
     * 
     * @param service Service PreferenceService
     */
    public PreferenceController(PreferenceService service) {
        this.service = service;
    }

    /**
     * GET /preferences/{clientId}
     * Récupère les préférences d'un utilisateur par son ID client anonyme
     */
    public void getByClientId(Context ctx) {
        /* Extraction de l'ID client depuis l'URL */
        String clientId = ctx.pathParam("clientId");
        
        /* Validation de l'ID client */
        if (clientId == null || clientId.isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("clientId manquant"));
            return;
        }
        
        /* Recherche des préférences */
        Preference pref = service.getByClientId(clientId);
        
        if (pref == null) {
            /* Pas de préférences trouvées → 404 */
            ctx.status(404);
        } else {
            /* Réponse JSON avec les préférences */
            ctx.json(pref);
        }
    }

    /**
     * POST /preferences
     * Sauvegarde ou met à jour les préférences utilisateur
     * Corps JSON : {clientId, langue, programme}
     */
    public void saveOrUpdate(Context ctx) {
        /* Désérialisation JSON → objet Preference */
        Preference pref = ctx.bodyAsClass(Preference.class);
        
        /* Validation champ obligatoire : clientId */
        if (pref.getClientId() == null || pref.getClientId().isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("clientId est obligatoire"));
            return;
        }
        
        /* Valeur par défaut pour langue */
        if (pref.getLangue() == null || pref.getLangue().isBlank()) {
            pref.setLangue("fr");    // Langue par défaut : français
        }
        
        /* Sauvegarde ou mise à jour en base */
        Preference saved = service.saveOrUpdate(pref);
        
        /* Réponse 200 avec l'objet sauvegardé */
        ctx.status(200).json(saved);
    }
}
