package com.michelle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Michelle Cruz
 * CEN 3024C - Software Development I
 * DatabaseManager.java
 *
 * This class manages the connection between the Pet Health Care Tracker
 * and a user-selected SQLite database. It also performs the database
 * operations needed to load, add, update, and remove pet records.
 */
public class DatabaseManager {

    private Connection connection;

    /**
     * Method: DatabaseManager
     * Parameters: none
     * Return: none
     * Purpose: Creates a DatabaseManager object without opening a connection.
     */
    public DatabaseManager() {
        connection = null;
    }

    /**
     * Method: connect
     * Parameters: databasePath
     * Return: boolean
     * Purpose: Connects to an existing SQLite database selected by the user.
     */
    public boolean connect(String databasePath) {

        if (databasePath == null || databasePath.trim().isEmpty()) {
            return false;
        }

        File databaseFile = new File(databasePath);

        try {
            closeConnection();

            String databaseURL = "jdbc:sqlite:" + databaseFile.getAbsolutePath();
            connection = DriverManager.getConnection(databaseURL);

            createPetsTable();
            return true;

        } catch (SQLException exception) {
            System.out.println(
                    "Database connection error: " + exception.getMessage()
            );

            connection = null;
            return false;
        }
    }

    /**
     * Method: isConnected
     * Parameters: none
     * Return: boolean
     * Purpose: Determines whether the application currently has an open
     * database connection.
     */
    public boolean isConnected() {

        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException exception) {
            return false;
        }
    }

    /**
     * Method: createPetsTable
     * Parameters: none
     * Return: boolean
     * Purpose: Creates the pets table if it does not already exist.
     */
    private boolean createPetsTable() {

        if (!isConnected()) {
            return false;
        }

        String sql =
                "CREATE TABLE IF NOT EXISTS pets (" +
                        "pet_id INTEGER PRIMARY KEY, " +
                        "pet_name TEXT NOT NULL, " +
                        "species TEXT NOT NULL, " +
                        "breed TEXT NOT NULL, " +
                        "age INTEGER NOT NULL, " +
                        "age_months INTEGER NOT NULL, " +
                        "weight REAL NOT NULL, " +
                        "health_notes TEXT, " +
                        "medication_name TEXT, " +
                        "last_vet_visit TEXT, " +
                        "vaccination_records TEXT" +
                        ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            return true;

        } catch (SQLException exception) {
            System.out.println(
                    "Table creation error: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Method: getAllPets
     * Parameters: none
     * Return: ArrayList<Pet>
     * Purpose: Retrieves every pet record currently stored in the database.
     */
    public ArrayList<Pet> getAllPets() {

        ArrayList<Pet> databasePets = new ArrayList<>();

        if (!isConnected()) {
            return databasePets;
        }

        String sql =
                "SELECT pet_id, pet_name, species, breed, age, age_months, " +
                        "weight, health_notes, medication_name, " +
                        "last_vet_visit, vaccination_records " +
                        "FROM pets ORDER BY pet_id";

        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {

                Pet pet = new Pet(
                        resultSet.getInt("pet_id"),
                        resultSet.getString("pet_name"),
                        resultSet.getString("species"),
                        resultSet.getString("breed"),
                        resultSet.getInt("age"),
                        resultSet.getInt("age_months"),
                        resultSet.getDouble("weight"),
                        resultSet.getString("health_notes"),
                        resultSet.getString("medication_name"),
                        resultSet.getString("last_vet_visit"),
                        resultSet.getString("vaccination_records")
                );

                databasePets.add(pet);
            }

        } catch (SQLException exception) {
            System.out.println(
                    "Database read error: " + exception.getMessage()
            );
        }

        return databasePets;
    }

    /**
     * Method: insertPet
     * Parameters: pet
     * Return: boolean
     * Purpose: Inserts one new pet record into the database.
     */
    public boolean insertPet(Pet pet) {

        if (!isConnected() || pet == null) {
            return false;
        }

        String sql =
                "INSERT INTO pets (" +
                        "pet_id, pet_name, species, breed, age, age_months, " +
                        "weight, health_notes, medication_name, " +
                        "last_vet_visit, vaccination_records" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setPetStatementValues(statement, pet);
            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Database insert error: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Method: updatePet
     * Parameters: pet, originalPetID
     * Return: boolean
     * Purpose: Updates an existing database record, including cases where
     * the Pet ID itself was changed.
     */
    public boolean updatePet(Pet pet, int originalPetID) {

        if (!isConnected() || pet == null || originalPetID <= 0) {
            return false;
        }

        String sql =
                "UPDATE pets SET " +
                        "pet_id = ?, " +
                        "pet_name = ?, " +
                        "species = ?, " +
                        "breed = ?, " +
                        "age = ?, " +
                        "age_months = ?, " +
                        "weight = ?, " +
                        "health_notes = ?, " +
                        "medication_name = ?, " +
                        "last_vet_visit = ?, " +
                        "vaccination_records = ? " +
                        "WHERE pet_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            setPetStatementValues(statement, pet);
            statement.setInt(12, originalPetID);

            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Database update error: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Method: deletePet
     * Parameters: petID
     * Return: boolean
     * Purpose: Deletes the pet record with the matching Pet ID.
     */
    public boolean deletePet(int petID) {

        if (!isConnected() || petID <= 0) {
            return false;
        }

        String sql = "DELETE FROM pets WHERE pet_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, petID);
            return statement.executeUpdate() == 1;

        } catch (SQLException exception) {
            System.out.println(
                    "Database deletion error: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Method: petIDExists
     * Parameters: petID
     * Return: boolean
     * Purpose: Determines whether a Pet ID already exists in the database.
     */
    public boolean petIDExists(int petID) {

        if (!isConnected() || petID <= 0) {
            return false;
        }

        String sql = "SELECT 1 FROM pets WHERE pet_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, petID);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException exception) {
            System.out.println(
                    "Database search error: " + exception.getMessage()
            );
            return false;
        }
    }

    /**
     * Method: setPetStatementValues
     * Parameters: statement, pet
     * Return: void
     * Purpose: Places the Pet object's values into an SQL prepared statement.
     */
    private void setPetStatementValues(
            PreparedStatement statement,
            Pet pet
    ) throws SQLException {

        statement.setInt(1, pet.getPetID());
        statement.setString(2, pet.getPetName());
        statement.setString(3, pet.getSpecies());
        statement.setString(4, pet.getBreed());
        statement.setInt(5, pet.getAge());
        statement.setInt(6, pet.getAgeMonths());
        statement.setDouble(7, pet.getWeight());
        statement.setString(8, pet.getHealthNotes());
        statement.setString(9, pet.getMedicationName());
        statement.setString(10, pet.getLastVetVisit());
        statement.setString(11, pet.getVaccinationRecords());
    }

    /**
     * Method: closeConnection
     * Parameters: none
     * Return: void
     * Purpose: Safely closes the current SQLite database connection.
     */
    public void closeConnection() {

        if (connection == null) {
            return;
        }

        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException exception) {
            System.out.println(
                    "Database closing error: " + exception.getMessage()
            );
        } finally {
            connection = null;
        }
    }
}