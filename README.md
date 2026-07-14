# Pet Health Care Tracker

## Program Description

Pet Health Care Tracker is a Java Swing desktop application that allows users to manage pet health records through a graphical user interface (GUI). The application loads pet records from a text file and enables users to display, add, update, remove, and summarize pet records in an organized and user-friendly interface. The project demonstrates object-oriented programming principles, file handling, input validation, and Java Swing GUI development.

---

## Program Tools

- IntelliJ IDEA
- Java 17 (Developed and Tested)
- Java Swing
- Maven

---

## Program Concepts

- Classes and Objects
- Encapsulation
- Constructors
- Methods and Method Calls
- ArrayLists
- File Input and Output
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
- Executable JAR Deployment

---

## Program Features

- Load pet records from a text file
- Display pet records in a JTable
- Add new pet records
- Update existing pet records
- Remove pet records
- Prevent duplicate pet IDs
- Validate user input before storing records
- Validate numeric values and dates
- Generate pet health summaries
- Status bar feedback
- Confirmation dialogs
- Professional Java Swing graphical user interface

---

## Unit Testing

The application includes JUnit 5 unit tests to verify the core functionality of the Pet Health Care Tracker.

The tests cover:

- Adding a pet record
- Removing a pet record
- Updating a pet record
- Loading the default pet records file
- Calculating a pet health score (high score)
- Calculating a pet health score (lower score)
- Successful operations
- Expected failures for invalid or non-existent records

All unit tests pass successfully before deployment.

---

## Repository Contents

- **PetCareGUI.java** - Main graphical user interface for the application.
- **Pet.java** - Represents individual pet objects.
- **PetManager.java** - Manages pet records and application operations.
- **HealthCalculator.java** - Generates pet health summaries.
- **RoundedPanel.java** - Custom component used to create the rounded application title banner.
- **pet_records.txt** - Sample data file containing pet records.
- **pom.xml** - Maven project configuration.

---

## Graphical User Interface Highlights

- Custom pet-themed application logo
- Rounded title banner
- Color-themed buttons and interface
- Zebra-striped data table for improved readability
- Scrollable JTable for displaying pet records
- Status bar feedback
- File selection using JFileChooser
- Confirmation and validation dialogs
- Professional desktop application layout
- Clean and user-friendly interface

---

## How to Run

### Requirements

- Java 17 or newer (JDK or JRE)
- The provided `pet_records.txt` file

### Running the Program

1. Launch `PetHealthCareTracker.jar`.
2. Click **Load Records**.
3. Browse to and select the provided `pet_records.txt` file.
4. Use the graphical interface to display, add, update, remove, and summarize pet records.

The application can also be opened in IntelliJ IDEA for development and testing

---
