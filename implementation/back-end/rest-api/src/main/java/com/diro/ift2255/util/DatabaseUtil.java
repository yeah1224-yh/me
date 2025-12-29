// src/main/java/com/diro/ift2255/util/DatabaseUtil.java
package com.diro.ift2255.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitaire pour la gestion de la base de données SQLite
 * Initialisation des tables + import CSV des statistiques
 */
public class DatabaseUtil {

    /* URL de connexion SQLite (fichier avis.db) */
    private static final String DB_URL = "jdbc:sqlite:avis.db";

    /**
     * Retourne une connexion à la base de données
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initialise la base de données (création des tables si absentes)
     */
    public static void initDatabase() {
        String createAvisTable = "CREATE TABLE IF NOT EXISTS avis (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cours_id TEXT NOT NULL," +
            "auteur TEXT," +
            "texte TEXT," +
            "session TEXT," +
            "volume_travail INTEGER," +
            "difficulte INTEGER," +
            "note INTEGER" +
        ");";

        String createPreferencesTable = "CREATE TABLE IF NOT EXISTS preferences (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "client_id TEXT NOT NULL UNIQUE," +
            "langue TEXT," +
            "programme TEXT" +
        ");";

        String createStatsTable = "CREATE TABLE IF NOT EXISTS stats_cours (" +
            "sigle TEXT PRIMARY KEY," +
            "nom TEXT," +
            "moyenne TEXT," +
            "score REAL," +
            "participants INTEGER," +
            "trimestres INTEGER" +
        ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createAvisTable);
            stmt.execute(createPreferencesTable);
            stmt.execute(createStatsTable);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur init database", e);
        }
    }

    /**
     * Gestion intelligente des statistiques :
     * - Si DB inexistante → créer + importer tout
     * - Si DB existe → vérifier ≥ 99 lignes dans stats_cours
     *   → OK → rien faire
     *   → < 99 → DELETE + réimport
     */
    public static void importStatsFromCsvIfEmpty() {
        try {
            // 1. Vérifier si stats_cours existe et compter les lignes
            int lineCount = 0;
            String countSql = "SELECT COUNT(*) FROM stats_cours";
            
            try (Connection conn = getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(countSql)) {

                if (rs.next()) {
                    lineCount = rs.getInt(1);
                }
            }

            // 2. Décision selon le nombre de lignes
            if (lineCount >= 99) {
                System.out.println("Import stats_cours ignoré: " + lineCount + " lignes OK (≥ 99)");
                return;
            }

            // 3. < 99 lignes → Nettoyer et réimporter
            System.out.println("Import stats_cours lancé: seulement " + lineCount + " lignes (< 99)");
            
            try (Connection conn = getConnection();
                 Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM stats_cours");
            }

            // 4. Import CSV complet
            doImportCsv();

        } catch (SQLException e) {
            // 5. Erreur SQL = DB inexistante → créer + importer
            System.out.println("Base de données inexistante → création + import complet");
            initDatabase();
            doImportCsv();
        }
    }

    /**
     * Import CSV (méthode privée réutilisable)
     */
    private static void doImportCsv() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT OR IGNORE INTO stats_cours " +
                 "(sigle, nom, moyenne, score, participants, trimestres) " +
                 "VALUES (?, ?, ?, ?, ?, ?)"
             )) {

            InputStream in = DatabaseUtil.class.getResourceAsStream("/data/historique_cours_prog_117510.csv");
            if (in == null) {
                throw new RuntimeException("Fichier CSV /data/historique_cours_prog_117510.csv introuvable");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                boolean first = true;
                int importedCount = 0;

                while ((line = reader.readLine()) != null) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    if (line.trim().isEmpty()) continue;

                    try {
                        String[] parts = parseCsvLine(line);
                        if (parts.length < 6) {
                            continue;
                        }

                        String sigle = parts[0].trim().toUpperCase().replace("\"", "");
                        String nom = parts[1].trim().replace("\"", "");
                        String moyenne = parseMoyenneString(parts[2]);
                        Double score = parseDoubleOrNull(parts[3]);
                        Integer participants = parseIntOrNull(parts[4]);
                        Integer trimestres = parseIntOrNull(parts[5]);

                        ps.setString(1, sigle);
                        ps.setString(2, nom);
                        ps.setString(3, moyenne);
                        ps.setObject(4, score, Types.REAL);
                        ps.setObject(5, participants, Types.INTEGER);
                        ps.setObject(6, trimestres, Types.INTEGER);

                        ps.addBatch();
                        importedCount++;

                    } catch (Exception e) {
                        // ignore ligne invalide
                    }
                }

                ps.executeBatch();
                System.out.println("Import stats_cours terminé: " + importedCount + " lignes importées.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur import CSV", e);
        }
    }

    // --------------------------------------------------------------------
    // Méthodes utilitaires de parsing CSV
    // --------------------------------------------------------------------

    private static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    private static String parseMoyenneString(String s) {
        if (s == null || s.trim().isEmpty() || s.trim().equals("-")) {
            return null;
        }
        return s.trim().replace("\"", "");
    }

    private static Double parseDoubleOrNull(String s) {
        if (s == null || s.trim().isEmpty() || s.trim().equals("-")) {
            return null;
        }
        try {
            return Double.parseDouble(s.trim().replace(",", ".").replace(" ", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseIntOrNull(String s) {
        if (s == null || s.trim().isEmpty() || s.trim().equals("-")) {
            return null;
        }
        try {
            return Integer.parseInt(s.trim().replace(" ", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
