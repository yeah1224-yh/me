package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour CourseService (liste de cours)
 */
class CourseServiceTest {

    @SuppressWarnings("unchecked")
    @Test
    void getCourses_shouldCallHttpClientApiWithSigleFilter() {
        // ARRANGE
        HttpClientApi clientApi = mock(HttpClientApi.class);
        CourseService service = new CourseService(clientApi);

        // on stub la méthode generique get(URI, TypeReference<List<Course>>)
        when(clientApi.get(any(URI.class), any(TypeReference.class)))
                .thenReturn(List.of(new Course()));

        Map<String, String> params = Map.of("sigle", "IFT");

        // ACT
        List<Course> result = service.getCourses(params);

        // ASSERT
        assertEquals(1, result.size());

        // Capture de l'URI utilisé
        ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        verify(clientApi).get(uriCaptor.capture(), any(TypeReference.class));
        URI calledUri = uriCaptor.getValue();

        assertTrue(calledUri.toString().contains("sigle=IFT"));
    }
}
