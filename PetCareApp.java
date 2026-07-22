package com.michelle;

/**
 * Starts the Pet Health Care Tracker application.
 *
 * This class creates a PetManager object and begins
 * the program.
 *
 * @author Michelle Cruz
 */
public class PetCareApp {

    /**
     * Starts the Pet Health Care Tracker application.
     *
     * @param args command-line arguments that are not used
     */
    public static void main(String[] args) {

        PetManager manager = new PetManager();
        manager.startProgram();

    }
}