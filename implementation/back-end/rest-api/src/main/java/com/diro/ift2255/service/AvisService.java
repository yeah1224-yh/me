// src/main/java/com/diro/ift2255/service/AvisService.java
package com.diro.ift2255.service;

import com.diro.ift2255.model.Avis;
import com.diro.ift2255.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des avis des cours
 * Opérations CRUD sur la table 'avis'
 */
public class AvisService {

    /**
     * Recherche des avis pour un cours avec filtres optionnels
     * 
     * @param coursId ID du cours (obligatoire)
     * @param session Filtre session (optionnel)
     * @param volumeTravail Filtre volume de travail (optionnel)
     * @param difficulte Filtre difficulté (optionnel)
     * @return Liste des avis filtrés
     */
    public List<Avis> getAvisPourCours(String coursId,
                                       Optional<String> session,
                                       Optional<Integer> volumeTravail,
                                       Optional<Integer> difficulte) {

        /* Construction dynamique de la requête SQL */
        StringBuilder sql = new StringBuilder(
            "SELECT id, cours_id, auteur, texte, session, volume_travail, difficulte, note " +
            "FROM avis WHERE cours_id = ?"
        );

        /* Paramètres de la requête */
        List<Object> params = new ArrayList<>();
        params.add(coursId);

        /* Ajout des filtres optionnels */
        session.ifPresent(s -> {
            sql.append(" AND session = ?");
            params.add(s);
        });
        volumeTravail.ifPresent(v -> {
            sql.append(" AND volume_travail = ?");
            params.add(v);
        });
        difficulte.ifPresent(d -> {
            sql.append(" AND difficulte = ?");
            params.add(d);
        });

        List<Avis> result = new ArrayList<>();

        /* Exécution de la requête */
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            /* Liaison des paramètres */
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            /* Lecture des résultats */
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Avis avis = new Avis();
                    avis.setId(rs.getInt("id"));
                    avis.setCoursId(rs.getString("cours_id"));
                    avis.setAuteur(rs.getString("auteur"));
                    avis.setTexte(rs.getString("texte"));
                    avis.setSession(rs.getString("session"));
                    avis.setVolumeTravail((Integer) rs.getObject("volume_travail"));
                    avis.setDifficulte((Integer) rs.getObject("difficulte"));
                    avis.setNote((Integer) rs.getObject("note"));
                    result.add(avis);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des avis pour le cours " + coursId, e);
        }

        return result;
    }

    /**
     * Ajoute un nouvel avis en base de données
     * 
     * @param avis Avis à insérer
     * @return Avis avec ID généré
     */
    public Avis ajouterAvis(Avis avis) {
        /* Requête d'insertion avec retour de la clé générée */
        String sql = "INSERT INTO avis (cours_id, auteur, texte, session, volume_travail, difficulte, note) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            /* Liaison des paramètres obligatoires */
            ps.setString(1, avis.getCoursId());
            ps.setString(2, avis.getAuteur());
            ps.setString(3, avis.getTexte());
            ps.setString(4, avis.getSession());

            /* Paramètres numériques optionnels (NULL si absent) */
            if (avis.getVolumeTravail() != null) {
                ps.setInt(5, avis.getVolumeTravail());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (avis.getDifficulte() != null) {
                ps.setInt(6, avis.getDifficulte());
            } else {
                ps.setNull(6, Types.INTEGER);
            }

            if (avis.getNote() != null) {
                ps.setInt(7, avis.getNote());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            /* Exécution de l'insertion */
            ps.executeUpdate();

            /* Récupération de l'ID généré */
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    avis.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'avis", e);
        }

        return avis;
    }

    /**
     * Compte le nombre total d'avis pour un cours
     * 
     * @param coursId ID du cours
     * @return Nombre d'avis
     */
    public int countAvisForCourse(String coursId) {
        if (coursId == null) return 0;

        String sql = "SELECT COUNT(*) AS total FROM avis WHERE cours_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, coursId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des avis pour le cours " + coursId, e);
        }

        return 0;
    }
}
