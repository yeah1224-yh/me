package com.diro.ift2255.service;

import com.diro.ift2255.model.Avis;
import com.diro.ift2255.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test d'intégration léger pour AvisService (filtrage par session)
 */
class AvisServiceTest {

    @BeforeEach
    void setup() throws Exception {
        DatabaseUtil.initDatabase();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("DELETE FROM avis");

            st.executeUpdate(
                "INSERT INTO avis (cours_id, auteur, texte, session, volume_travail, difficulte, note) " +
                "VALUES ('IFT2255', 'Alice', 'Avis A24', 'A24', 3, 4, 5)"
            );
            st.executeUpdate(
                "INSERT INTO avis (cours_id, auteur, texte, session, volume_travail, difficulte, note) " +
                "VALUES ('IFT2255', 'Bob', 'Avis H25', 'H25', 2, 3, 4)"
            );
        }
    }

    @Test
    void getAvisPourCours_shouldFilterBySession() {
        // ARRANGE
        AvisService service = new AvisService();

        // ACT
        List<Avis> avisA24 = service.getAvisPourCours(
            "IFT2255",
            Optional.of("A24"),
            Optional.empty(),
            Optional.empty()
        );

        // ASSERT
        assertEquals(1, avisA24.size());
        assertEquals("A24", avisA24.get(0).getSession());
        assertEquals("Alice", avisA24.get(0).getAuteur());
    }
}
