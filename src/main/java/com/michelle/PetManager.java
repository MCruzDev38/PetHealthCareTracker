package com.michelle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * July 3,2026
 * PetManager.java
 *
 * This class manages the pet record list and controls the menu-driven
 * interface for the Pet Health Care Tracker program.
 */
public class PetManager {

    private final ArrayList<Pet> pets;
    private final HealthCalculator healthCalculator;
    private final Scanner scanner;

    /**
     * Method: PetManager
     * Parameters: none
     * Return: none
     * Purpose: Creates a PetManager object and prepares the pet list and scanner.
     */
    public PetManager() {
        pets = new ArrayList<>();
        healthCalculator = new HealthCalculator();
        scanner = new Scanner(System.in);

        loadDefaultPetFile();
    }

    /**
     * Method: loadDefaultPetFile
     * Parameters: none
     * Return: none
     * Purpose: Loads the default pet records file when the program starts.
     */

    private void loadDefaultPetFile() {
        if (loadPetFile("src/main/resources/pet_records.txt")) {
            System.out.println("Default pet records loaded.");
        } else {
            System.out.println("Default pet records file could not be loaded.");
        }
    }

    /**
     * Method: loadPetFile
     * Parameters: fileName
     * Return: boolean
     * Purpose: Loads pet records from a file and returns true if the file was found.
     */
    public boolean loadPetFile(String fileName) {
        File petFile = new File(fileName);

        if (!petFile.exists()) {
            return false;
        }

        try (Scanner fileScanner = new Scanner(petFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (!line.isEmpty()) {
                    createPetFromLine(line);
                }
            }

            return true;
        } catch (FileNotFoundException exception) {
            return false;
        }
    }

    /**
     * Method: startProgram
     * Parameters: none
     * Return: boolean
     * Purpose: Keeps the menu running until the user chooses to exit the program.
     */
    public boolean startProgram() {

        boolean running = true;

        while (running) {
            System.out.println(buildMenuText());
            int choice = readInt("Enter your choice: ");
            running = completeMenuChoice(choice);
        }

        scanner.close();
        return true;
    }

    /**
     * Method: buildMenuText
     * Parameters: none
     * Return: String
     * Purpose: Builds the menu text that is shown to the user.
     */
    private String buildMenuText() {
        return "\n===============================" +
                "\n   Pet Health Care Tracker" +
                "\n===============================" +
                "\n1. Read pet records from file" +
                "\n2. Display all pet records" +
                "\n3. Add a new pet record" +
                "\n4. Remove a pet record" +
                "\n5. Update a pet record" +
                "\n6. Generate pet health summary" +
                "\n7. Exit";
    }

    /**
     * Method: completeMenuChoice
     * Parameters: choice
     * Return: boolean
     * Purpose: Runs the feature selected by the user and returns whether the menu should continue.
     */
    private boolean completeMenuChoice(int choice) {

        String result;

        switch (choice) {
            case 1:
                result = loadPetsFromFile();
                break;
            case 2:
                result = displayPets();
                break;
            case 3:
                result = addPet();
                break;
            case 4:
                result = removePet();
                break;
            case 5:
                result = updatePet();
                break;
            case 6:
                result = generateHealthSummary();
                break;
            case 7:
                System.out.println("\nThank you for using Pet Health Care Tracker.");
                return false;
            default:
                result = "Invalid menu option. Please enter a number from 1 to 7.";
        }

        System.out.println(result);
        return true;
    }

    /**
     * Method: loadPetsFromFile
     * Parameters: none
     * Return: String
     * Purpose: Loads pet records from a text file entered by the user.
     */
    private String loadPetsFromFile() {

        System.out.print("\nEnter the file name for the pet records: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            fileName = "pet_records.txt";
        }

        String filePath = fileName;
        File petFile = new File(filePath);
        int loadedCount = 0;
        int skippedCount = 0;

        try (Scanner fileScanner = new Scanner(petFile)) {

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    skippedCount++;
                } else if (createPetFromLine(line)) {
                    loadedCount++;
                } else {
                    skippedCount++;
                }
            }

        } catch (FileNotFoundException exception) {
            return "The file could not be found. Please check the file path and try again.";
        }

        return "File load complete. Records added: " + loadedCount +
                ". Records skipped: " + skippedCount + ".";
    }

    /**
     * Method: createPetFromLine
     * Parameters: line
     * Return: boolean
     * Purpose: Converts one comma-separated line from the text file into a pet record.
     */
    private boolean createPetFromLine(String line) {

        String[] fields = line.split(",", -1);

        if (fields.length != 11) {
            return false;
        }

        try {
            int petID = Integer.parseInt(fields[0].trim());
            String petName = fields[1].trim();
            String species = fields[2].trim();
            String breed = fields[3].trim();
            int age = Integer.parseInt(fields[4].trim());
            int ageMonths = Integer.parseInt(fields[5].trim());

            double weight = Double.parseDouble(fields[6].trim());
            String healthNotes = fields[7].trim();
            String medicationName = fields[8].trim();
            String lastVetVisit = fields[9].trim();
            String vaccinationRecords = fields[10].trim();

            if (petID <= 0 || age < 0 || ageMonths < 0 || ageMonths > 11 || weight <= 0 || petName.isEmpty() || species.isEmpty()) {
                return false;
            }

            if (findPetByID(petID) != null) {
                return false;
            }

            pets.add(new Pet(petID, petName, species, breed, age, ageMonths, weight,
                    healthNotes, medicationName, lastVetVisit, vaccinationRecords));
            return true;

        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Method: displayPets
     * Parameters: none
     * Return: String
     * Purpose: Displays all pet records currently stored in the program.
     */
    private String displayPets() {

        if (pets.isEmpty()) {
            return "\nNo pet records are currently available.";
        }

        StringBuilder records = new StringBuilder("\n--- Pet Records ---\n");

        for (Pet pet : pets) {
            records.append(pet).append("\n--------------------\n");
        }

        return records.toString();
    }

    /**
     * Method: addPet
     * Parameters: none
     * Return: String
     * Purpose: Allows the user to manually add one new pet record.
     */
    private String addPet() {

        System.out.println("\n--- Add New Pet ---");

        int petID = readPositiveInt("Pet ID: ");

        if (findPetByID(petID) != null) {
            return "A pet with that ID already exists. Please use a unique Pet ID.";
        }

        String petName = readTextWithLetter("Pet Name: ");
        String species = readTextWithLetter("Species: ");
        String breed = readTextWithLetter("Breed: ");

        int age = readValidAge("Age (years): ");
        int ageMonths = readValidMonths("Age (months 0-11): ");

        while (age == 0 && ageMonths == 0) {
            System.out.println("A pet must be at least 1 month old. ");
            age = readValidAge("Age (years): ");
            ageMonths = readValidMonths("Age (months 0-11): ");
            }

        double weight = readPositiveDouble("Weight (pounds): ");

        String healthNotes = readOptionalText("Health Notes (Leave blank and press Enter if none): ");
        String medicationName = readOptionalText("Medication Name (Leave blank and press Enter if none): ");
        String lastVetVisit = readOptionalDate(
                "Last Vet Visit (MM/DD/YY or MM/DD/YYYY) (Leave blank and press Enter if none): ");
        String vaccinationRecords = readOptionalText("Vaccination Records (Leave blank and press Enter if none): ");
        Pet newPet = new Pet(petID, petName, species, breed, age, ageMonths, weight,
                healthNotes, medicationName, lastVetVisit, vaccinationRecords);

        if (addPetRecord(newPet)) {
            return "Pet record added successfully.";
        }

        return "Pet record could not be added.";
    }

    /**
     * Method: addPetRecord
     * Parameters: pet
     * Return: boolean
     * Purpose: Adds a pet record if the pet is valid and the ID is unique.
     */
    public boolean addPetRecord(Pet pet) {
        if (pet == null) {
            return false;
        }

        if (findPetByID(pet.getPetID()) != null) {
            return false;
        }

        pets.add(pet);
        return true;
    }

    /**
     * Method: removePetByID
     * Parameters: petID
     * Return: boolean
     * Purpose: Removes a pet by its ID.
     */
    public boolean removePetByID(int petID) {

        Pet pet = findPetByID(petID);

        if (pet == null) {
            return false;
        }

        pets.remove(pet);
        return true;
    }

    public boolean updatePetName(int petID, String newName) {
        Pet pet = findPetByID(petID);

        if (pet == null || newName == null || newName.trim().isEmpty()) {
            return false;
        }

        pet.setPetName(newName);
        return true;
    }

    /**
     * Method: removePet
     * Parameters: none
     * Return: String
     * Purpose: Removes a pet record using the pet's unique ID.
     */
    private String removePet() {

        if (pets.isEmpty()) {
            return "\nNo pet records are available to remove.";
        }

        System.out.println("\n--- Remove Pet Record ---");
        Pet pet = readExistingPet("Enter the Pet ID to remove");

        if (pet == null) {
            return "Pet record removal was canceled.";
        }

        System.out.println("\nRecord selected for removal:");
        System.out.println(pet);
        String confirmation = readRequiredText("Type YES to confirm removal: ");

        if (!confirmation.equalsIgnoreCase("YES")) {
            return "Pet record removal was canceled.";
        }

        if (removePetByID(pet.getPetID())) {
            return "Pet record removed successfully.";
        }

        return "Pet record removal failed.";
    }

    /**
     * Method: updatePet
     * Parameters: none
     * Return: String
     * Purpose: Updates any field of an existing pet record.
     */
    private String updatePet() {

        if (pets.isEmpty()) {
            return "\nNo pet records are available to update.";
        }

        System.out.println("\n--- Update Pet Record ---");
        Pet pet = readExistingPet("Enter the Pet ID to update");

        if (pet == null) {
            return "Pet record update was canceled.";
        }

        boolean updating = true;

        while (updating) {
            System.out.println(buildUpdateMenuText());
            int choice = readInt("Enter the field number to update: ");
            updating = updateSelectedField(pet, choice);
        }

        return "Pet record updated successfully.";
    }

    /**
     * Method: buildUpdateMenuText
     * Parameters: none
     * Return: String
     * Purpose: Builds the menu used to update a selected pet record.
     */
    private String buildUpdateMenuText() {
        return "\nChoose a field to update:" +
                "\n1. Pet ID" +
                "\n2. Pet Name" +
                "\n3. Species" +
                "\n4. Breed" +
                "\n5. Age" +
                "\n6. Weight" +
                "\n7. Health Notes" +
                "\n8. Medication Name" +
                "\n9. Last Vet Visit" +
                "\n10. Vaccination Records" +
                "\n11. Finish updating";
    }

    /**
     * Method: updateSelectedField
     * Parameters: pet, choice
     * Return: boolean
     * Purpose: Updates one selected field and returns whether the update menu should continue.
     */
    private boolean updateSelectedField(Pet pet, int choice) {

        switch (choice) {
            case 1:
                int newPetID = readPositiveInt("New Pet ID: ");
                Pet existingPet = findPetByID(newPetID);

                if (existingPet != null && existingPet != pet) {
                    System.out.println("That Pet ID already exists. The Pet ID was not changed.");
                } else {
                    pet.setPetID(newPetID);
                    System.out.println("Pet ID updated.");
                }
                return true;
            case 2:
                pet.setPetName(readRequiredText("New Pet Name: "));
                System.out.println("Pet name updated.");
                return true;
            case 3:
                pet.setSpecies(readRequiredText("New Species: "));
                System.out.println("Species updated.");
                return true;
            case 4:
                pet.setBreed(readRequiredText("New Breed: "));
                System.out.println("Breed updated.");
                return true;
            case 5:
                int newAge = readValidAge("New Age (years): ");
                int newAgeMonths = readValidMonths("New Age (months 0-11): ");

                while (newAge == 0 && newAgeMonths == 0) {
                    System.out.println("A pet must be at least 1 month old.");
                    newAge = readValidAge("New Age (years): ");
                    newAgeMonths = readValidMonths("New Age (months 0-11): ");
                }

                pet.setAge(newAge);
                pet.setAgeMonths(newAgeMonths);

                System.out.println("Age updated.");
                return true;
            case 6:
                pet.setWeight(readPositiveDouble("New Weight: "));
                System.out.println("Weight updated.");
                return true;
            case 7:
                pet.setHealthNotes(readOptionalText("Health Notes (Leave blank and press Enter if none): "));
                System.out.println("Health notes updated.");
                return true;
            case 8:
                pet.setMedicationName(readOptionalText("Medication Name (Leave blank and press Enter if none): "));
                System.out.println("Medication name updated.");
                return true;
            case 9:
                pet.setLastVetVisit(readOptionalDate("New Last Vet Visit (MM/DD/YY or MM/DD/YYYY) (Leave blank and press Enter if none): "));
                System.out.println("Last vet visit updated.");
                return true;
            case 10:
                pet.setVaccinationRecords(readOptionalText("Vaccination Records (Leave blank and press Enter if none): "));
                System.out.println("Vaccination records updated.");
                return true;
            case 11:
                return false;
            default:
                System.out.println("Invalid update option. Please choose a number from 1 to 11.");
                return true;
        }
    }

    /**
     * Method: calculateHealthScore
     * Parameters: none
     * Return: String
     * Purpose: Calculates and displays a health score for an existing pet.
     */
    private String generateHealthSummary() {

            if (pets.isEmpty()) {
                return "There are no pet records available.";
            }

            Pet pet = readExistingPet("Enter the Pet ID");

            if (pet == null) {
                return "Health summary generation was canceled.";
            }

            return
                    "\n==============================\n" +
                            "      PET HEALTH SUMMARY\n" +
                            "==============================\n" +
                            "Pet ID: " + pet.getPetID() + "\n" +
                            "Name: " + pet.getPetName() + "\n" +
                            "Species: " + pet.getSpecies() + "\n" +
                            "Breed: " + pet.getBreed() + "\n\n" +
                            "Age: " +
                            (
                                    pet.getAge() == 0
                                            ? pet.getAgeMonths() + " months"
                                            : (pet.getAgeMonths() == 0
                                            ? pet.getAge() + " years"
                                            : pet.getAge() + " years " + pet.getAgeMonths() + " months")
                            ) + "\n" +
                            "Weight: " + pet.getWeight() + " lbs\n\n" +

                            "Health Notes:\n" +
                            pet.getHealthNotes() + "\n\n" +

                            "Medication:\n" +
                            pet.getMedicationName() + "\n\n" +

                            "Last Vet Visit:\n" +
                            pet.getLastVetVisit() + "\n\n" +

                            "Vaccination Records:\n" +
                            pet.getVaccinationRecords() + "\n\n" +

                            "Health Score: " +
                            healthCalculator.calculateHealthScore(pet) +
                            " / 100\n\n" +
                            "Overall Assessment: " +
                            getHealthAssessment(healthCalculator.calculateHealthScore(pet));
        }

    /**
     * Method: findPetByID
     * Parameters: petID
     * Return: Pet
     * Purpose: Searches the pet list for a record with the matching ID.
     */
    public Pet findPetByID(int petID) {

        for (Pet pet : pets) {
            if (pet.getPetID() == petID) {
                return pet;
            }
        }

        return null;
    }

    /**
     * Method: readExistingPet
     * Parameters: prompt
     * Return: Pet
     * Purpose: Gets an existing pet record by ID and allows the user to cancel by entering 0.
     */
    private Pet readExistingPet(String prompt) {

        while (true) {
            int petID = readInt(prompt + " or enter 0 to cancel: ");

            if (petID == 0) {
                return null;
            }

            Pet pet = findPetByID(petID);

            if (pet != null) {
                return pet;
            }

            System.out.println("No pet record was found with that ID. Please try again.");
        }
    }

    /**
     * Method: readInt
     * Parameters: prompt
     * Return: int
     * Purpose: Reads an integer without allowing invalid input to crash the program.
     */
    private int readInt(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    /**
     * Method: readPositiveInt
     * Parameters: prompt
     * Return: int
     * Purpose: Reads an integer greater than zero.
     */
    private int readPositiveInt(String prompt) {

        while (true) {
            int value = readInt(prompt);

            if (value > 0) {
                return value;
            }

            System.out.println("Please enter a number greater than zero.");
        }
    }

    /**
     * Method: readNonNegativeInt
     * Parameters: prompt
     * Return: int
     * Purpose: Reads an integer that is zero or greater.
     */
    private int readNonNegativeInt(String prompt) {

        while (true) {
            int value = readInt(prompt);

            if (value >= 0) {
                return value;
            }

            System.out.println("Please enter zero or a positive number.");
        }
    }

    /**
     * Method: readValidAge
     * Parameters: prompt
     * Return: int
     * Purpose: Reads a valid pet age between 0 and 100 years.
     */
    private int readValidAge(String prompt) {

        while (true) {

            int age = readInt(prompt);

            if (age >= 0 && age <= 100) {
                return age;
            }

            System.out.println("Please enter an age between 0 and 100.");
        }
    }

    /**
     * Method: readValidMonths
     * Parameters: prompt
     * Return: int
     * Purpose: Reads a valid month value between 0 and 11.
     */
    private int readValidMonths(String prompt) {

        while (true) {

            int months = readInt(prompt);

            if (months >= 0 && months <= 11) {
                return months;
            }

            System.out.println("Please enter a value between 0 and 11.");
        }
    }

    /**
     * Method: readPositiveDouble
     * Parameters: prompt
     * Return: double
     * Purpose: Reads a decimal number greater than zero.
     */
    private double readPositiveDouble(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (value > 0) {
                    return value;
                }

                System.out.println("Please enter a number greater than zero.");

            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Method: readRequiredText
     * Parameters: prompt
     * Return: String
     * Purpose: Reads text and prevents blank input from being accepted.
     */
    private String readRequiredText(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be blank.");
        }
    }

    private String readTextWithLetter(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty() && value.matches(".*[a-zA-Z].*")) {
                return value;
            }

            System.out.println("Please enter a valid response that includes at least one letter.");
        }
    }

    private String readOptionalText(String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            return "None";
        }

        return value;
    }

    private String readValidDate(String prompt) {

        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy")
        };

        while (true) {

            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            for (DateTimeFormatter formatter : formats) {
                try {
                    LocalDate.parse(input, formatter);
                    return input;
                } catch (DateTimeParseException ignored) {
                }
            }

            System.out.println("Please enter a valid date using MM/DD/YY or MM/DD/YYYY.");
        }
    }

    private String readOptionalDate(String prompt) {
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy")
        };

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return "None";
            }

            for (DateTimeFormatter formatter : formats) {
                try {
                    LocalDate.parse(input, formatter);
                    return input;
                } catch (DateTimeParseException ignored) {
                }
            }

            System.out.println("Please enter a valid date using MM/DD/YY or MM/DD/YYYY, or leave blank if none.");
        }
    }

    /**
     * Method: getHealthAssessment
     * Parameters: score
     * Return: String
     * Purpose: Returns a written assessment based on the health score.
     */
    private String getHealthAssessment(int score) {

        if (score >= 90) {
            return "Excellent overall health.";
        } else if (score >= 75) {
            return "Good overall health.";
        } else if (score >= 60) {
            return "Fair health. Monitor diet and exercise.";
        } else {
            return "Needs attention. Consider a veterinary checkup.";
        }
    }
}
