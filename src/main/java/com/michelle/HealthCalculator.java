package com.michelle;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * July 3,2026
 * HealthCalculator.java
 * This class calculates a pet health score based on age, weight, health notes, vaccination records
 * and the most recent veterinary visit.
 */
public class HealthCalculator {

    /**
     * Method: calculateHealthScore
     * Parameters: pet - Pet object containing the health information
     * Return: int
     * Purpose: Calculates an overall pet health score out of 100 based on age, weight, health notes, vaccination records
     * and the most recent veterinary visit.
     */
    public int calculateHealthScore(Pet pet) {

        int score = 0;

        // Age (20 points)
        if (pet.getAge() <= 3) {
            score += 20;
        } else if (pet.getAge() <= 7) {
            score += 15;
        } else {
            score += 10;
        }

        // Weight (20 points)
        double weight = pet.getWeight();

        if (weight >= 5 && weight <= 60) {
            score += 20;
        } else if ((weight >= 3 && weight < 5) || (weight > 60 && weight <= 80)) {
            score += 15;
        } else {
            score += 10;
        }

        // Last Vet Visit (20 points)
        String visit = pet.getLastVetVisit().toLowerCase();

        if (!visit.contains("none")) {
            score += 20;
        } else {
            score += 10;
        }

        // Vaccination Records (20 points)
        String vaccines = pet.getVaccinationRecords().toLowerCase();

        if (!vaccines.contains("none")) {
            score += 20;
        } else {
            score += 10;
        }

        // Health Notes (20 points)
        String notes = pet.getHealthNotes().toLowerCase();

        if (notes.contains("healthy") || notes.contains("good")) {
            score += 20;
        } else if (notes.contains("allergy")
                || notes.contains("itch")
                || notes.contains("minor")) {
            score += 15;
        } else {
            score += 10;
        }

        return score;
    }
}