package com.diro.ift2255.service;

import com.diro.ift2255.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsCoursService {
    
    public CoursStats getStatsForCourse(String sigle) {
        System.out.println("[StatsCoursService] Recherche stats pour: " + sigle);
        
        String sql = "SELECT sigle, nom, moyenne, score, participants, trimestres " +
                     "FROM stats_cours WHERE UPPER(sigle) = UPPER(?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, sigle.trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("[StatsCoursService] Stats trouvées pour: " + sigle);
                    
                    String moyenne = rs.getString("moyenne");
                    Double score = rs.getObject("score") != null ? rs.getDouble("score") : null;
                    Integer participants = rs.getObject("participants") != null ? rs.getInt("participants") : null;
                    Integer trimestres = rs.getObject("trimestres") != null ? rs.getInt("trimestres") : null;
                    
                    return new CoursStats(
                        rs.getString("sigle"),
                        rs.getString("nom"),
                        moyenne,
                        score,
                        participants,
                        trimestres
                    );
                } else {
                    System.out.println("[StatsCoursService] Aucune stats pour: " + sigle);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[StatsCoursService] Erreur SQL: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lecture stats pour " + sigle, e);
        }
        
        return null;
    }
    
    public static class CoursStats {
        public final String sigle;
        public final String nom;
        public final String moyenne;
        public final Double score;
        public final Integer participants;
        public final Integer trimestres;
        
        public CoursStats(String sigle, String nom, String moyenne,
                         Double score, Integer participants, Integer trimestres) {
            this.sigle = sigle;
            this.nom = nom;
            this.moyenne = moyenne;
            this.score = score;
            this.participants = participants;
            this.trimestres = trimestres;
        }
    }
}