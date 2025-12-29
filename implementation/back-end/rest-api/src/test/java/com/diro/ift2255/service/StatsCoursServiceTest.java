package com.diro.ift2255.service;

import com.diro.ift2255.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration léger pour StatsCoursService
 */
class StatsCoursServiceTest {

    @BeforeEach
    void setup() throws Exception {
        // On initialise la vraie DB (avis.db) une fois, ce qui crée aussi stats_cours
        DatabaseUtil.initDatabase();

        // On nettoie et insère une ligne de test
        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM stats_cours");
            st.executeUpdate(
                "INSERT INTO stats_cours (sigle, nom, moyenne, score, participants, trimestres) " +
                "VALUES ('IFT2255', 'Intro test', 'A-', 90.0, 50, 4)"
            );
        }
    }

    @Test
    void getStatsForCourse_shouldReturnStats_whenRowExists() {
        // ARRANGE
        StatsCoursService service = new StatsCoursService();

        // ACT
        StatsCoursService.CoursStats stats = service.getStatsForCourse("IFT2255");

        // ASSERT
        assertNotNull(stats);
        assertEquals("IFT2255", stats.sigle);
        assertEquals("Intro test", stats.nom);
        assertEquals("A-", stats.moyenne);
        assertEquals(90.0, stats.score);
        assertEquals(50, stats.participants);
        assertEquals(4, stats.trimestres);
    }

    @Test
    void getStatsForCourse_shouldReturnNull_whenNoRow() {
        // ARRANGE
        StatsCoursService service = new StatsCoursService();

        // ACT
        StatsCoursService.CoursStats stats = service.getStatsForCourse("IFT9999");

        // ASSERT
        assertNull(stats);
    }
}
