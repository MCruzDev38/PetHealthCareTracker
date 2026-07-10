package com.michelle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * July 7, 2026
 * PetManagerTest.java
 *
 * This class contains JUnit tests used to verify the
 * functionality of the Pet Health Care Tracker application.
 */

public class PetManagerTest {

    @Test
    public void addPetRecord_ShouldReturnTrue_WhenPetIsValid() {

        PetManager manager = new PetManager();

        Pet pet = new Pet(
                9999,
                "Buddy",
                "Dog",
                "French Bulldog",
                2,
                6,
                28.5,
                "",
                "",
                "",
                ""
        );

        boolean result = manager.addPetRecord(pet);

        assertTrue(result);

    }


    @Test
    public void addPetRecord_ShouldReturnFalse_WhenPetIDAlreadyExists() {

        PetManager manager = new PetManager();

        Pet pet1 = new Pet(
                9998,
                "Buddy",
                "Dog",
                "French Bulldog",
                2,
                6,
                28.5,
                "",
                "",
                "",
                ""
        );

        Pet pet2 = new Pet(
                9998,
                "Max",
                "Dog",
                "Labrador",
                4,
                0,
                65.0,
                "",
                "",
                "",
                ""
        );

        assertTrue(manager.addPetRecord(pet1));

        assertFalse(manager.addPetRecord(pet2));

    }

    @Test
    public void removePetByID_ShouldReturnTrue_WhenPetExists() {
        PetManager manager = new PetManager();

        Pet pet = new Pet(9997, "Bella", "Dog", "Poodle", 3, 0, 22.0,
                "", "", "", "");

        manager.addPetRecord(pet);

        boolean result = manager.removePetByID(9997);

        assertTrue(result);
    }

    @Test
    public void removePetByID_ShouldReturnFalse_WhenPetDoesNotExist() {
        PetManager manager = new PetManager();

        boolean result = manager.removePetByID(88888);

        assertFalse(result);
    }
    @Test
    public void updatePetName_ShouldReturnTrue_WhenPetExists() {
        PetManager manager = new PetManager();

        Pet pet = new Pet(9996, "Bella", "Dog", "Poodle", 3, 0, 22.0,
                "", "", "", "");

        manager.addPetRecord(pet);

        boolean result = manager.updatePetName(9996, "Luna");

        assertTrue(result);
        assertEquals("Luna", pet.getPetName());
    }

    @Test
    public void updatePetName_ShouldReturnFalse_WhenPetDoesNotExist() {
        PetManager manager = new PetManager();

        boolean result = manager.updatePetName(77777, "Luna");

        assertFalse(result);
    }

    @Test
    public void loadPetFile_ShouldReturnTrue_WhenFileExists() {
        PetManager manager = new PetManager();

        boolean result = manager.loadPetFile("src/main/resources/pet_records.txt");

        assertTrue(result);
    }

    @Test
    public void loadPetFile_ShouldReturnFalse_WhenFileDoesNotExist() {
        PetManager manager = new PetManager();

        boolean result = manager.loadPetFile("src/main/resources/missing_file.txt");

        assertFalse(result);
    }

    @Test
    public void calculateHealthScore_ShouldReturn100_WhenPetIsHealthy() {

        Pet pet = new Pet(
                9998,
                "Buddy",
                "Dog",
                "French Bulldog",
                2,
                0,
                30.0,
                "Healthy",
                "None",
                "January 2026",
                "Current"
        );

        HealthCalculator calculator = new HealthCalculator();

        int score = calculator.calculateHealthScore(pet);

        assertEquals(100, score);
    }

    @Test
    public void calculateHealthScore_ShouldReturnLowerScore_WhenPetHasHealthIssues() {

        Pet pet = new Pet(
                9999,
                "Max",
                "Dog",
                "French Bulldog",
                10,
                0,
                45.0,
                "Severe allergy",
                "Apoquel",
                "None",
                "None"
        );

        HealthCalculator calculator = new HealthCalculator();

        int score = calculator.calculateHealthScore(pet);

        assertTrue(score < 100);
    }
}

