package com.michelle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Manages pet records and controls the features
 * of the Pet Health Care Tracker application.
 *
 * This class coordinates pet record storage, database operations,
 * file loading, user input, record updates, and health summaries.
 *
 * @author Michelle Cruz
 */

public class PetManager {

    private final ArrayList<Pet> pets;
    private final HealthCalculator healthCalculator;
    private final Scanner scanner;
    private final DatabaseManager databaseManager;

    /**
     * Creates a PetManager and initializes the pet list,
     * health calculator, scanner, and database manager.
     */

    public PetManager() {
        pets = new ArrayList<>();
        healthCalculator = new HealthCalculator();
        scanner = new Scanner(System.in);
        databaseManager = new DatabaseManager();
    }

    /**
     * Loads the default pet records file when the program starts.
     */

    private void loadDefaultPetFile() {
        if (loadPetFile("src/main/resources/pet_records.txt")) {
            System.out.println("Default pet records loaded.");
        } else {
            System.out.println("Default pet records file could not be loaded.");
        }
    }

    /**
     * Loads pet records from the specified text file.
     * The current pet records are restored if the selected file
     * cannot be read or does not contain any valid records.
     *
     * @param fileName the path or name of the pet records file
     * @return {@code true} if valid pet records were loaded;
     * otherwise {@code false}
     */

    public boolean loadPetFile(String fileName) {
        File petFile = new File(fileName);

        if (!petFile.exists()) {
            return false;
        }

        // Save the current records in case the new file is invalid.
        ArrayList<Pet> originalPets = new ArrayList<>(pets);

        // Clear the list temporarily while testing the selected file.
        pets.clear();

        try (Scanner fileScanner = new Scanner(petFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (!line.isEmpty()) {
                    createPetFromLine(line);
                }
            }

            // If no valid pets were created, restore the old records.
            if (pets.isEmpty()) {
                pets.addAll(originalPets);
                return false;
            }

            return true;

        } catch (FileNotFoundException | RuntimeException exception) {
            // Restore the old records if reading or parsing fails.
            pets.clear();
            pets.addAll(originalPets);
            return false;
        }
    }

    /**
     * Connects the application to the specified SQLite database.
     * When the connection succeeds, the current pet list is replaced
     * with the records stored in the database.
     *
     * @param databasePath the path to the SQLite database file
     * @return {@code true} if the database connection is successful;
     * otherwise {@code false}
     */

    public boolean connectToDatabase(String databasePath) {
        if (databasePath == null || databasePath.trim().isEmpty()) {
            return false;
        }

        boolean connected = databaseManager.connect(databasePath);

        if (connected) {
            pets.clear();
            pets.addAll(databaseManager.getAllPets());
        }

        return connected;
    }

    /**
     * Returns a copy of the current pet record list.
     * Returning a copy prevents outside classes from directly
     * modifying the original list.
     *
     * @return an ArrayList containing the current pet records
     */

    public ArrayList<Pet> getPets() {
        return new ArrayList<>(pets);
    }

    /**
     * Removes all pet records from the current pet list.
     */

    public void clearPets() {
        pets.clear();
    }

    /**
     * Starts the console version of the application and keeps
     * displaying the main menu until the user chooses to exit.
     *
     * @return {@code true} after the program has finished running
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
     * Builds and returns the main menu displayed to the user.
     *
     * @return the formatted main menu text
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
     * Processes the user's menu selection and performs
     * the requested operation.
     *
     * @param choice the menu option selected by the user
     * @return {@code true} to continue displaying the menu;
     * otherwise {@code false} to exit the program
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
     * Prompts the user for a file name and loads pet
     * records from the selected text file.
     *
     * @return a message describing the results of the
     * file loading operation
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
     * Converts one comma-separated line from a text file
     * into a pet record.
     *
     * @param line the comma-separated line containing pet information
     * @return {@code true} if a valid pet record was created;
     * otherwise {@code false}
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

            if (petID <= 0 || age < 0 || ageMonths < 0 || ageMonths > 11
                    || weight <= 0 || petName.isEmpty() || species.isEmpty()) {
                return false;
            }

            if (findPetByID(petID) != null) {
                return false;
            }

            pets.add(new Pet(
                    petID,
                    petName,
                    species,
                    breed,
                    age,
                    ageMonths,
                    weight,
                    healthNotes,
                    medicationName,
                    lastVetVisit,
                    vaccinationRecords
            ));

            return true;

        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Displays all pet records currently stored
     * in the application.
     *
     * @return a formatted string containing all pet
     * records, or a message if no records exist
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
     * Collects information from the user and creates
     * a new pet record.
     *
     * @return a message explaining whether the pet record
     * was added successfully
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

        String healthNotes = readOptionalText(
                "Health Notes (Leave blank and press Enter if none): "
        );

        String medicationName = readOptionalText(
                "Medication Name (Leave blank and press Enter if none): "
        );

        String lastVetVisit = readOptionalDate(
                "Last Vet Visit (MM/DD/YY or MM/DD/YYYY) " +
                        "(Leave blank and press Enter if none): "
        );

        String vaccinationRecords = readOptionalText(
                "Vaccination Records (Leave blank and press Enter if none): "
        );

        Pet newPet = new Pet(
                petID,
                petName,
                species,
                breed,
                age,
                ageMonths,
                weight,
                healthNotes,
                medicationName,
                lastVetVisit,
                vaccinationRecords
        );

        if (addPetRecord(newPet)) {
            return "Pet record added successfully.";
        }

        return "Pet record could not be added.";
    }

    /**
     * Adds a pet record to the connected database and
     * the current pet list.
     *
     * The record is not added if the pet is null, the database
     * is not connected, or the Pet ID already exists.
     *
     * @param pet the pet record to add
     * @return {@code true} if the record was added successfully;
     * otherwise {@code false}
     */

    public boolean addPetRecord(Pet pet) {

        if (pet == null || !databaseManager.isConnected()) {
            return false;
        }

        if (databaseManager.petIDExists(pet.getPetID())) {
            return false;
        }

        if (!databaseManager.insertPet(pet)) {
            return false;
        }

        pets.add(pet);
        return true;
    }

    /**
     * Removes the pet with the specified ID from the connected
     * database and the current pet list.
     *
     * @param petID the unique ID of the pet to remove
     * @return {@code true} if the record was removed successfully;
     * otherwise {@code false}
     */

    public boolean removePetByID(int petID) {

        if (!databaseManager.isConnected()) {
            return false;
        }

        Pet pet = findPetByID(petID);

        if (pet == null) {
            return false;
        }

        if (!databaseManager.deletePet(petID)) {
            return false;
        }

        pets.remove(pet);
        return true;
    }

    /**
     * Allows the user to select and confirm the removal
     * of an existing pet record.
     *
     * @return a message explaining whether the record was removed
     * or the operation was canceled
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

        String confirmation =
                readRequiredText("Type YES to confirm removal: ");

        if (!confirmation.equalsIgnoreCase("YES")) {
            return "Pet record removal was canceled.";
        }

        if (removePetByID(pet.getPetID())) {
            return "Pet record removed successfully.";
        }

        return "Pet record removal failed.";
    }

    /**
     * Saves changes made to an existing pet record
     * in the connected database.
     *
     * @param pet the pet record containing the updated information
     * @return {@code true} if the record was updated successfully;
     * otherwise {@code false}
     */

    public boolean updatePetRecord(Pet pet) {

        if (pet == null || !databaseManager.isConnected()) {
            return false;
        }

        return databaseManager.updatePet(pet, pet.getPetID());
    }

    /**
     * Allows the user to select an existing pet record
     * and update one or more of its fields.
     *
     * @return a message explaining whether the pet record
     * was updated or the operation was canceled
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
     * Builds the menu used to select which pet
     * information should be updated.
     *
     * @return the formatted update menu text
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
     * Updates the field selected by the user.
     *
     * @param pet the pet record being updated
     * @param choice the number of the field selected by the user
     * @return {@code true} to continue displaying the update menu;
     * otherwise {@code false} to finish updating
     */

    private boolean updateSelectedField(Pet pet, int choice) {

        switch (choice) {
            case 1:
                int newPetID = readPositiveInt("New Pet ID: ");
                Pet existingPet = findPetByID(newPetID);

                if (existingPet != null && existingPet != pet) {
                    System.out.println(
                            "That Pet ID already exists. " +
                                    "The Pet ID was not changed."
                    );
                } else {
                    pet.setPetID(newPetID);
                    System.out.println("Pet ID updated.");
                }
                return true;

            case 2:
                pet.setPetName(
                        readRequiredText("New Pet Name: ")
                );
                System.out.println("Pet name updated.");
                return true;

            case 3:
                pet.setSpecies(
                        readRequiredText("New Species: ")
                );
                System.out.println("Species updated.");
                return true;

            case 4:
                pet.setBreed(
                        readRequiredText("New Breed: ")
                );
                System.out.println("Breed updated.");
                return true;

            case 5:
                int newAge = readValidAge("New Age (years): ");
                int newAgeMonths = readValidMonths(
                        "New Age (months 0-11): "
                );

                while (newAge == 0 && newAgeMonths == 0) {
                    System.out.println(
                            "A pet must be at least 1 month old."
                    );

                    newAge = readValidAge("New Age (years): ");
                    newAgeMonths = readValidMonths(
                            "New Age (months 0-11): "
                    );
                }

                pet.setAge(newAge);
                pet.setAgeMonths(newAgeMonths);

                System.out.println("Age updated.");
                return true;

            case 6:
                pet.setWeight(
                        readPositiveDouble("New Weight: ")
                );
                System.out.println("Weight updated.");
                return true;

            case 7:
                pet.setHealthNotes(
                        readOptionalText(
                                "Health Notes " +
                                        "(Leave blank and press Enter if none): "
                        )
                );
                System.out.println("Health notes updated.");
                return true;

            case 8:
                pet.setMedicationName(
                        readOptionalText(
                                "Medication Name " +
                                        "(Leave blank and press Enter if none): "
                        )
                );
                System.out.println("Medication name updated.");
                return true;

            case 9:
                pet.setLastVetVisit(
                        readOptionalDate(
                                "New Last Vet Visit " +
                                        "(MM/DD/YY or MM/DD/YYYY) " +
                                        "(Leave blank and press Enter if none): "
                        )
                );
                System.out.println("Last vet visit updated.");
                return true;

            case 10:
                pet.setVaccinationRecords(
                        readOptionalText(
                                "Vaccination Records " +
                                        "(Leave blank and press Enter if none): "
                        )
                );
                System.out.println("Vaccination records updated.");
                return true;

            case 11:
                return false;

            default:
                System.out.println(
                        "Invalid update option. " +
                                "Please choose a number from 1 to 11."
                );
                return true;
        }
    }

    /**
     * Generates a health summary for an existing pet record.
     *
     * @return a formatted health summary, or a message if no pet
     * records are available or the operation is canceled
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
                                        : pet.getAge() + " years " +
                                        pet.getAgeMonths() + " months")
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
                        getHealthAssessment(
                                healthCalculator.calculateHealthScore(pet)
                        );
    }

    /**
     * Searches the current pet list for a record
     * with the specified Pet ID.
     *
     * @param petID the unique ID of the pet to find
     * @return the matching pet record, or {@code null} if no
     * matching record is found
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
     * Prompts the user to enter the ID of an existing pet.
     * The user may enter zero to cancel the operation.
     *
     * @param prompt the message displayed before requesting the Pet ID
     * @return the matching pet record, or {@code null} if the
     * user cancels
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

            System.out.println(
                    "No pet record was found with that ID. Please try again."
            );
        }
    }

    /**
     * Reads a whole number entered by the user.
     * Invalid input is rejected without closing the program.
     *
     * @param prompt the message displayed before requesting input
     * @return the whole number entered by the user
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
     * Reads a whole number greater than zero.
     *
     * @param prompt the message displayed before requesting input
     * @return a whole number greater than zero
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
     * Reads a whole number that is zero or greater.
     *
     * @param prompt the message displayed before requesting input
     * @return a whole number that is zero or greater
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
     * Reads a pet age between 0 and 100 years.
     *
     * @param prompt the message displayed before requesting the age
     * @return a valid pet age between zero and 100
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
     * Reads a valid month value between zero and 11.
     *
     * @param prompt the message displayed before requesting the months
     * @return a valid month value between zero and 11
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
     * Reads a decimal number greater than zero.
     *
     * @param prompt the message displayed before requesting input
     * @return a decimal number greater than zero
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

                System.out.println(
                        "Please enter a number greater than zero."
                );

            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Reads required text and prevents blank input
     * from being accepted.
     *
     * @param prompt the message displayed before requesting input
     * @return the nonblank text entered by the user
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

    /**
     * Reads required text containing at least one letter.
     *
     * @param prompt the message displayed before requesting input
     * @return valid text containing at least one letter
     */

    private String readTextWithLetter(String prompt) {

        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty() &&
                    value.matches(".*[a-zA-Z].*")) {
                return value;
            }

            System.out.println(
                    "Please enter a valid response that includes " +
                            "at least one letter."
            );
        }
    }

    /**
     * Reads optional text entered by the user.
     *
     * @param prompt the message displayed before requesting input
     * @return the entered text, or {@code "None"} if the
     * field is left blank
     */

    private String readOptionalText(String prompt) {

        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        if (value.isEmpty()) {
            return "None";
        }

        return value;
    }

    /**
     * Reads and validates a required date.
     *
     * @param prompt the message displayed before requesting the date
     * @return a valid date entered using MM/DD/YY or MM/DD/YYYY
     */

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

            System.out.println(
                    "Please enter a valid date using " +
                            "MM/DD/YY or MM/DD/YYYY."
            );
        }
    }

    /**
     * Reads and validates an optional date.
     *
     * @param prompt the message displayed before requesting the date
     * @return a valid date entered using MM/DD/YY or MM/DD/YYYY,
     * or {@code "None"} if the field is left blank
     */

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

            System.out.println(
                    "Please enter a valid date using MM/DD/YY or " +
                            "MM/DD/YYYY, or leave blank if none."
            );
        }
    }

    /**
     * Returns a written assessment based on a pet's
     * calculated health score.
     *
     * @param score the pet's calculated health score
     * @return the health assessment that matches the score
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

