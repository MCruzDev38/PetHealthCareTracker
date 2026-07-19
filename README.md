# Pet Health Care Tracker

## Program Description

Pet Health Care Tracker is a Java Swing desktop application that allows users to manage pet health records through a graphical user interface (GUI). The application stores pet records in a SQLite database, allowing users to load, display, add, update, remove, and summarize pet records while maintaining persistent data storage. The project demonstrates object-oriented programming principles, database integration, input validation, and Java Swing GUI development.

---

## Program Tools

- IntelliJ IDEA
- Java 17 (Developed and Tested)
- Java Swing
- Maven
- SQLite
- SQLite JDBC Driver

---

## Database

This application uses SQLite as its back-end database. A sample database named **pet_records.db** is included with the project and contains 20 sample pet records. Users can load the sample database or create a new SQLite database. If a selected database does not already exist, the application automatically creates the database and initializes the required table.

---

## Program Concepts

- Classes and Objects
- Encapsulation
- Constructors
- Methods and Method Calls
- ArrayLists
- Exception Handling
- Data Validation
- Date Validation
- Object-Oriented Design
- Java Swing GUI Development
- JTable
- JOptionPane Dialogs
- Layout Managers (BorderLayout, FlowLayout, GridBagLayout)
- Custom JPanel Components
- Maven Project Structure
- SQLite Database Integration
- JDBC Database Connectivity

---

## Program Features

- Load pet records from a SQLite database
- Display pet records in a JTable
- Add new pet records
- Update existing pet records
- Remove pet records
- Prevent duplicate Pet IDs
- Validate user input before saving records
- Validate numeric values and dates
- Generate pet health summaries
- Status bar feedback
- Confirmation dialogs
- Java Swing graphical interface
- Automatically create a new SQLite database if one does not already exist

---

## Unit Testing

The application includes JUnit 5 unit tests to verify the core functionality of the Pet Health Care Tracker.

The tests cover:

- Adding a pet record
- Removing a pet record
- Updating a pet record
- Loading pet records from the SQLite database
- Calculating a pet health score (high score)
- Calculating a pet health score (lower score)
- Successful operations
- Expected failures for invalid or non-existent records

All unit tests pass successfully before deployment.

---

## Repository Contents

- PetCareGUI.java – Main graphical user interface for the application.
- Pet.java – Represents individual pet objects.
- PetManager.java – Manages pet records and application operations.
- DatabaseManager.java – Handles SQLite database connections and database operations.
- HealthCalculator.java – Generates pet health summaries.
- RoundedPanel.java – Custom component used to create the rounded application title banner.
- pet_records.db – Sample SQLite database containing 20 sample pet records.
- pom.xml – Maven project configuration.

---

## How to Run

### Requirements

- Java 17 or newer (JDK)
- IntelliJ IDEA (recommended)
- Maven
- SQLite JDBC dependency (automatically downloaded through Maven)
- The provided **pet_records.db** sample database

### Running the Program

1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Allow Maven to download all project dependencies.
4. Run **PetCareGUI.java**.
5. Click **Load Records**.
6. Browse to and select the included **pet_records.db** file.
7. The application will display the records stored in the SQLite database.
8. Use the graphical interface to add, update, remove, display, and summarize pet records.
9. All changes are automatically saved to the SQLite database.
