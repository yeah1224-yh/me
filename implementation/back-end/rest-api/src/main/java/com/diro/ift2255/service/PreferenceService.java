// src/main/java/com/diro/ift2255/service/PreferenceService.java
package com.diro.ift2255.service;

import com.diro.ift2255.model.Preference;
import com.diro.ift2255.util.DatabaseUtil;

import java.sql.*;

/**
 * Service métier pour la gestion des préférences utilisateur
 * Opérations CRUD sur la table 'preferences'
 */
public class PreferenceService {

    /**
     * Recherche les préférences par ID client anonyme
     *
     * @param clientId ID client unique (localStorage frontend)
     * @return Preference trouvée ou null
     */
    public Preference getByClientId(String clientId) {
        String sql = "SELECT id, client_id, langue, programme " +
                     "FROM preferences WHERE client_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Preference(
                        rs.getInt("id"),
                        rs.getString("client_id"),
                        rs.getString("langue"),
                        rs.getString("programme")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lecture préférences pour client " + clientId, e);
        }
        return null;
    }

    /**
     * UPSERT : Insert ou Update selon existence
     * Si clientId existe → UPDATE, sinon → INSERT
     *
     * @param pref Préférences à sauvegarder
     * @return Préférences sauvegardées avec ID
     */
    public Preference saveOrUpdate(Preference pref) {
        Preference existing = getByClientId(pref.getClientId());
        if (existing == null) {
            // Insertion nouvelle entrée
            return insert(pref);
        } else {
            // Mise à jour entrée existante
            pref.setId(existing.getId());
            return update(pref);
        }
    }

    /**
     * Insertion d'une nouvelle préférence
     * (package-private pour permettre le spy Mockito dans les tests)
     */
    Preference insert(Preference pref) {
        String sql = "INSERT INTO preferences (client_id, langue, programme) " +
                     "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pref.getClientId());
            ps.setString(2, pref.getLangue());
            ps.setString(3, pref.getProgramme());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    pref.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion préférences", e);
        }

        return pref;
    }

    /**
     * Mise à jour d'une préférence existante
     * (package-private pour permettre le spy Mockito dans les tests)
     */
    Preference update(Preference pref) {
        String sql = "UPDATE preferences " +
                     "SET langue = ?, programme = ? " +
                     "WHERE client_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pref.getLangue());
            ps.setString(2, pref.getProgramme());
            ps.setString(3, pref.getClientId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur mise à jour préférences", e);
        }

        return pref;
    }
}
