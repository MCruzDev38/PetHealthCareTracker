package com.michelle;

/**
 * Calculates a health score for a pet based on its age, weight,
 * health notes, vaccination records, and most recent veterinary visit.
 *
 * @author Michelle Cruz
 */

public class HealthCalculator {

    /**
     * Calculates an overall health score for the specified pet.
     * The score is determined using the pet's age, weight,
     * health notes, vaccination records, and most recent
     * veterinary visit.
     *
     * @param pet the pet whose health information will be evaluated
     * @return the calculated health score on a scale from 0 to 100
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