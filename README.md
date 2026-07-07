# PetHealthCareTracker

## Program Description

This project is a Java-based Pet Health Care Tracker. The application manages pet health records by loading information from a text file and allows users to add, update, remove, display, and summarize pet records through a menu-driven interface.

---

## Program Tools

- IntelliJ IDEA
- Java 25 (Developed and Tested)
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
- User Input with Scanner
- Loops and Conditional Statements
- String Formatting
- Object-Oriented Design
- UML-Based Program Development
- Maven Project Structure
- Executable JAR Deployment

---

## Program Features

- Load pet records from a text file
- Display all pet records
- Add new pet records
- Update existing pet records
- Remove pet records
- Prevent duplicate pet IDs
- Validate pet information before storing records
- Generate pet health summaries
- Menu-driven user interface
- Executable JAR deployment

---

## Repository Contents

- **Main.java** - Controls program execution and user interaction.
- **Pet.java** - Represents individual pet objects.
- **PetManager.java** - Manages pet records and application operations.
- **PetCareApp.java** - Contains the main menu and application workflow.
- **HealthCalculator.java** - Generates pet health summaries.
- **pet_records.txt** - Sample data file containing pet records.
- **pom.xml** - Maven project configuration.

---

## How to Run

### Requirements

- Java Runtime Environment (JRE) or Java Development Kit (JDK) installed
- The executable `PetHealthCareTracker.jar` file
- The `pet_records.txt` file in the same folder as the JAR

### Running the Program

1. Open a command prompt or terminal.
2. Navigate to the folder containing the executable JAR.
3. Run:

```bash
java -jar PetHealthCareTracker.jar
```

4. When prompted, enter:

```text
pet_records.txt
```

5. Use the menu options to load, display, add, update, remove, or summarize pet records.
