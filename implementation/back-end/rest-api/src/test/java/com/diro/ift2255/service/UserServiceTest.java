package com.diro.ift2255.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;

/**
 * Tests unitaires pour UserService
 * (fonctionnalité supplémentaire, hors des 4 écrans principaux)
 */
class UserServiceTest {

    private UserService userService;
    private long testStartTime;

    @BeforeAll
    static void printHeader() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("UserService Tests");
        System.out.println("=".repeat(80));
    }

    @BeforeEach
    void setup(TestInfo testInfo) {
        userService = new UserService();
        testStartTime = System.currentTimeMillis();

        System.out.println("\nTEST: " + testInfo.getDisplayName());
        System.out.println("    ├─ Method: " + testInfo.getTestMethod().get().getName());
        System.out.println("    ├─ Assertions:");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        long duration = System.currentTimeMillis() - testStartTime;
        System.out.println("    └─ Duration: " + duration + " ms");
    }

    @AfterAll
    static void printFooter() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPLETED: UserService Tests");
        System.out.println("=".repeat(80) + "\n");
    }

    // ------------------------------------------------------------------------
    // TESTS
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Get all users should return 2 mock users")
    void testGetAllUsers() {
        List<com.diro.ift2255.model.User> users = userService.getAllUsers();

        try {
            assertEquals(2, users.size(), "Should have 2 mock users");
            OK("Retrieved " + users.size() + " users as expected");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get user by ID should return the correct user when found")
    void testGetUserByIdFound() {
        int userId = 1; // dans le constructeur: ID 1 = Alice

        Optional<com.diro.ift2255.model.User> user = userService.getUserById(userId);

        try {
            assertTrue(user.isPresent(), "User with ID 1 should exist");
            OK("User with ID 1 exists", false);

            assertEquals("Alice", user.get().getName(), "User name should be Alice");
            OK("Retrieved user: " + user.get().getName());
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get user by ID should return empty when user is not found")
    void testGetUserByIdNotFound() {
        // ARRANGE
        int nonexistentUserId = 999;

        // ACT
        Optional<com.diro.ift2255.model.User> user = userService.getUserById(nonexistentUserId);

        // ASSERT
        try {
            // ✅ Comportement réel : Optional.empty()
            assertFalse(user.isPresent(), "Optional should be empty when user is not found");
            OK("No user found as expected (Optional.empty())");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    // ------------------------------------------------------------------------
    // Helpers de logging (comme dans ta version)
    // ------------------------------------------------------------------------

    private void printMessage(String message, boolean isOk, boolean isLast) {
        String symbol = isLast ? "└─" : "├─";
        String status = isOk ? "[PASS]" : "[FAIL]";
        System.out.println("    │   " + symbol + " " + status + " " + message);
    }

    private void OK(String message) {
        printMessage(message, true, true);
    }

    private void OK(String message, boolean isLast) {
        printMessage(message, true, isLast);
    }

    private void Err(String message) {
        printMessage(message, false, true);
    }

    @SuppressWarnings("unused")
    private void Err(String message, boolean isLast) {
        printMessage(message, false, isLast);
    }
}
