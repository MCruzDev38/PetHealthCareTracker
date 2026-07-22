package com.michelle;

/**
 * Represents a pet in the Pet Health Care Tracker application.
 * This class stores identifying information, age, weight,
 * health notes, medication information, veterinary visit details,
 * and vaccination records for a pet.
 *
 * @author Michelle Cruz
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

    /**
     * Creates a new pet record with the provided identifying
     * and health-related information.
     *
     * @param petID unique identification number for the pet
     * @param petName name of the pet
     * @param species species of the pet
     * @param breed breed of the pet
     * @param age age of the pet in years
     * @param ageMonths additional age of the pet in months
     * @param weight weight of the pet
     * @param healthNotes notes about the pet's health
     * @param medicationName medication currently associated with the pet
     * @param lastVetVisit date of the pet's most recent veterinary visit
     * @param vaccinationRecords vaccination information for the pet
     */

    public Pet(int petID, String petName, String species, String breed,
               int age, int ageMonths, double weight, String healthNotes,
               String medicationName, String lastVetVisit,
               String vaccinationRecords) {

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

    /**
     * Returns the pet's unique identification number.
     *
     * @return the pet ID
     */

    public int getPetID() {
        return petID;
    }

    /**
     * Sets the pet's unique identification number.
     *
     * @param petID the new pet ID
     */

    public void setPetID(int petID) {
        this.petID = petID;
    }

    /**
     * Returns the pet's name.
     *
     * @return the pet name
     */

    public String getPetName() {
        return petName;
    }

    /**
     * Returns the pet's species.
     *
     * @return the pet species
     */

    public String getSpecies() {
        return species;
    }

    /**
     * Returns the pet's breed.
     *
     * @return the pet breed
     */

    public String getBreed() {
        return breed;
    }

    /**
     * Returns the pet's age in years.
     *
     * @return the age in years
     */

    public int getAge() {
        return age;
    }

    /**
     * Returns the additional age of the pet in months.
     *
     * @return the age in months
     */

    public int getAgeMonths() {
        return ageMonths;
    }

    /**
     * Sets the additional age of the pet in months.
     *
     * @param ageMonths the new age in months
     */

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    /**
     * Returns the pet's weight.
     *
     * @return the pet weight
     */

    public double getWeight() {
        return weight;
    }

    /**
     * Returns the pet's health notes.
     *
     * @return the health notes
     */

    public String getHealthNotes() {
        return healthNotes;
    }

    /**
     * Returns the pet's medication name.
     *
     * @return the medication name
     */

    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Returns the date of the pet's most recent veterinary visit.
     *
     * @return the last veterinary visit
     */

    public String getLastVetVisit() {
        return lastVetVisit;
    }

    /**
     * Returns the pet's vaccination records.
     *
     * @return the vaccination records
     */

    public String getVaccinationRecords() {
        return vaccinationRecords;
    }

    /**
     * Sets the pet's name.
     *
     * @param petName the new pet name
     */

    public void setPetName(String petName) {
        this.petName = petName;
    }

    /**
     * Sets the pet's species.
     *
     * @param species the new pet species
     */

    public void setSpecies(String species) {
        this.species = species;
    }

    /**
     * Sets the pet's breed.
     *
     * @param breed the new pet breed
     */

    public void setBreed(String breed) {
        this.breed = breed;
    }

    /**
     * Sets the pet's age in years.
     *
     * @param age the new age in years
     */

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Sets the pet's weight.
     *
     * @param weight the new pet weight
     */

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Sets the pet's health notes.
     *
     * @param healthNotes the new health notes
     */

    public void setHealthNotes(String healthNotes) {
        this.healthNotes = healthNotes;
    }

    /**
     * Sets the pet's medication name.
     *
     * @param medicationName the new medication name
     */

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    /**
     * Sets the date of the pet's most recent veterinary visit.
     *
     * @param lastVetVisit the new veterinary visit date
     */

    public void setLastVetVisit(String lastVetVisit) {
        this.lastVetVisit = lastVetVisit;
    }

    /**
     * Sets the pet's vaccination records.
     *
     * @param vaccinationRecords the new vaccination records
     */

    public void setVaccinationRecords(String vaccinationRecords) {
        this.vaccinationRecords = vaccinationRecords;
    }

    /**
     * Returns a formatted string containing the pet's complete record.
     *
     * @return the formatted pet information
     */

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