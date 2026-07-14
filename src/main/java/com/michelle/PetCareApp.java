package com.michelle;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * July 3,2026
 * PetCareApp.java
 *
 * This class contains the main method for the Pet Health Care Tracker.
 * The program allows users to load3, view, create, update, and remove
 * pet health records through a menu-driven interface. It also provides
 * a custom health calculation feature while validating all user input
 * to prevent the program from crashing.
 */
public class PetCareApp {

    /**
     * Method: main
     * Parameters: String[] args
     * Return: none
     * Purpose: Starts the Pet Health Care Tracker application.
     */
    public static void main(String[] args) {

         PetManager manager = new PetManager();
        manager.startProgram();

    }
}