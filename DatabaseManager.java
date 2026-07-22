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
 * Manages the SQLite database connection and performs database
 * operations for the Pet Health Care Tracker application.
 * This class is responsible for connecting to the database,
 * creating the pets table when needed, and performing
 * create, read, update, and delete (CRUD) operations on pet records.
 *
 * @author Michelle Cruz
 */

public class DatabaseManager {

    private Connection connection;

    /**
     * Creates a DatabaseManager object without opening
     * a database connection.
     */

    public DatabaseManager() {
        connection = null;
    }

    /**
     * Connects to the specified SQLite database.
     * If the database does not already contain the pets table,
     * it is created automatically.
     *
     * @param databasePath the path to the SQLite database file
     * @return {@code true} if the connection is successful;
     * otherwise {@code false}
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
     * Determines whether the application currently has
     * an active database connection.
     *
     * @return {@code true} if the database is connected;
     * otherwise {@code false}
     */

    public boolean isConnected() {

        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException exception) {
            return false;
        }
    }

    /**
     * Creates the pets table if it does not already exist.
     *
     * @return {@code true} if the table exists or is created
     * successfully; otherwise {@code false}
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
     * Retrieves all pet records stored in the database.
     *
     * @return an ArrayList containing all pet records
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
     * Inserts a new pet record into the database.
     *
     * @param pet the pet to insert
     * @return {@code true} if the pet was inserted successfully;
     * otherwise {@code false}
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
     * Updates an existing pet record in the database.
     *
     * @param pet the updated pet information
     * @param originalPetID the original Pet ID before any changes were made
     * @return {@code true} if the record was updated successfully;
     * otherwise {@code false}
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
     * Deletes a pet record from the database.
     *
     * @param petID the unique ID of the pet to delete
     * @return {@code true} if the record was deleted successfully;
     * otherwise {@code false}
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
     * Determines whether the specified Pet ID already exists
     * in the database.
     *
     * @param petID the Pet ID to search for
     * @return {@code true} if the Pet ID exists;
     * otherwise {@code false}
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
     * Places a pet's values into a prepared SQL statement.
     *
     * @param statement the prepared statement to populate
     * @param pet the pet whose values will be stored
     * @throws SQLException if an error occurs while setting the
     *         prepared statement values
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
     * Closes the current database connection if one is open.
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