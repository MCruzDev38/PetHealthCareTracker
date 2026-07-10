package com.michelle;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * July 3,2026
 * Pet.java
 * This class creates a pet object and stores the health record information
 * used by the Pet Health and Care Tracker program.
 */
public class Pet {
    private int petID;
    private String petName;
    private String species;
    private String breed;
    private int age;
    private int ageMonths;
    private double weight;
    private String healthNotes;
    private String medicationName;
    private String lastVetVisit;
    private String vaccinationRecords;

    public Pet(int petID, String petName, String species, String breed, int age, int ageMonths, double weight,
               String healthNotes, String medicationName, String lastVetVisit, String vaccinationRecords) {
        this.petID = petID;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.age = age;
        this.ageMonths = ageMonths;
        this.weight = weight;
        this.healthNotes = healthNotes;
        this.medicationName = medicationName;
        this.lastVetVisit = lastVetVisit;
        this.vaccinationRecords = vaccinationRecords;
    }

    public int getPetID() {
        return petID;
    }

    public void setPetID(int petID) {
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public double getWeight() {
        return weight;
    }

    public String getHealthNotes() {
        return healthNotes;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public String getLastVetVisit() {
        return lastVetVisit;
    }

    public String getVaccinationRecords() {
        return vaccinationRecords;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHealthNotes(String healthNotes) {
        this.healthNotes = healthNotes;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public void setLastVetVisit(String lastVetVisit) {
        this.lastVetVisit = lastVetVisit;
    }

    public void setVaccinationRecords(String vaccinationRecords) {
        this.vaccinationRecords = vaccinationRecords;
    }

    @Override
    public String toString() {
        return "Pet ID: " + petID +
                "\nPet Name: " + petName +
                "\nSpecies: " + species +
                "\nBreed: " + breed +
                "\nAge: " +
                (age == 0
                        ? ageMonths + " months"
                        : (ageMonths == 0
                        ? age + " years"
                        : age + " years " + ageMonths + " months")) +
                "\nWeight: " + weight +
                "\nHealth Notes: " + healthNotes +
                "\nMedication Name: " + medicationName +
                "\nLast Vet Visit: " + lastVetVisit +
                "\nVaccination Records: " + vaccinationRecords;
    }
}
