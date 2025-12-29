package com.diro.ift2255.service;

import com.diro.ift2255.model.Preference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PreferenceService (fonctionnalité Profil).
 * On utilise un spy pour contrôler getByClientId / insert / update
 * sans toucher à la vraie base SQLite.
 */
@ExtendWith(MockitoExtension.class)
class PreferenceServiceTest {

    /**
     * Cas 1 : aucune préférence existante pour ce clientId
     * -> saveOrUpdate doit appeler insert() et non update().
     */
    @Test
    void saveOrUpdate_shouldInsert_whenPreferenceDoesNotExist() {
        // ARRANGE
        PreferenceService service = Mockito.spy(new PreferenceService());
        Preference pref = new Preference(null, "client-123", "fr", "Baccalauréat en informatique");

        // Simule : aucune préférence existante
        doReturn(null).when(service).getByClientId("client-123");

        // On stub insert pour ne pas taper la vraie DB
        Preference inserted = new Preference(1, "client-123", "fr", "Baccalauréat en informatique");
        doReturn(inserted).when(service).insert(any(Preference.class));

        // ACT
        Preference result = service.saveOrUpdate(pref);

        // ASSERT
        assertEquals(1, result.getId());
        assertEquals("client-123", result.getClientId());
        assertEquals("fr", result.getLangue());
        assertEquals("Baccalauréat en informatique", result.getProgramme());

        // insert doit être appelé, pas update
        verify(service, times(1)).insert(any(Preference.class));
        verify(service, never()).update(any(Preference.class));
    }

    /**
     * Cas 2 : une préférence existe déjà pour ce clientId
     * -> saveOrUpdate doit appeler update() et non insert().
     */
    @Test
    void saveOrUpdate_shouldUpdate_whenPreferenceAlreadyExists() {
        // ARRANGE
        PreferenceService service = Mockito.spy(new PreferenceService());

        Preference existing = new Preference(5, "client-456", "fr", "Ancien programme");
        Preference pref = new Preference(null, "client-456", "en", "Nouveau programme");

        // Simule : une préférence existe déjà pour ce clientId
        doReturn(existing).when(service).getByClientId("client-456");

        // On stub update pour ne pas taper la vraie DB
        Preference updated = new Preference(5, "client-456", "en", "Nouveau programme");
        doReturn(updated).when(service).update(any(Preference.class));

        // ACT
        Preference result = service.saveOrUpdate(pref);

        // ASSERT
        assertEquals(5, result.getId()); // conserve l'ID existant
        assertEquals("client-456", result.getClientId());
        assertEquals("en", result.getLangue());
        assertEquals("Nouveau programme", result.getProgramme());

        // update doit être appelé, pas insert
        verify(service, times(1)).update(any(Preference.class));
        verify(service, never()).insert(any(Preference.class));
    }
}
