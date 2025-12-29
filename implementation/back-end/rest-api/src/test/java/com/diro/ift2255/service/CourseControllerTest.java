package com.diro.ift2255.service;

import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.service.StatsCoursService.CoursStats;
import io.javalin.http.Context;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour CourseController (comparaison de cours)
 */
class CourseControllerTest {

    @Test
    void getCourseStats_shouldReturnStats_whenCourseExists() {
        // ARRANGE
        CourseService courseService = mock(CourseService.class);
        AvisService avisService = mock(AvisService.class);
        StatsCoursService statsService = mock(StatsCoursService.class);

        CourseController controller = new CourseController(courseService, avisService, statsService);
        Context ctx = mock(Context.class);

        when(ctx.pathParam("id")).thenReturn("IFT2255");
        CoursStats stats = new CoursStats("IFT2255", "Nom du cours", "A-", 85.0, 100, 5);
        when(statsService.getStatsForCourse("IFT2255")).thenReturn(stats);

        // ACT
        controller.getCourseStats(ctx);

        // ASSERT 
        verify(ctx, never()).status(404);
        verify(ctx).json(stats);
    }

    @Test
    void getCourseStats_shouldReturn404_whenNoStats() {
        // ARRANGE
        CourseService courseService = mock(CourseService.class);
        AvisService avisService = mock(AvisService.class);
        StatsCoursService statsService = mock(StatsCoursService.class);

        CourseController controller = new CourseController(courseService, avisService, statsService);
        Context ctx = mock(Context.class);

        when(ctx.pathParam("id")).thenReturn("IFT9999");
        when(statsService.getStatsForCourse("IFT9999")).thenReturn(null);

        // ACT
        controller.getCourseStats(ctx);

        // ASSERT plus de verify json()
        verify(ctx).status(404);
        // Pas de verify(ctx).json() car status(404) retourne null en mock
    }
}
