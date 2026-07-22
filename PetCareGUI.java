package com.michelle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Provides the graphical user interface for the
 * Pet Health Care Tracker application.
 *
 * This class creates the main application window, displays pet
 * records, and allows the user to load, add, update, remove,
 * and review pet health information.
 *
 * @author Michelle Cruz
 */

public class PetCareGUI extends JFrame {

    private final PetManager petManager;

    private DefaultTableModel tableModel;
    private JTable petTable;
    private final JLabel statusLabel;

    /**
     * Creates and displays the main Pet Health Care Tracker window.
     * The window includes the navigation buttons, pet records table,
     * and application status area.
     */

    public PetCareGUI() {
        petManager = new PetManager();

        setTitle("Pet Health Care Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 700);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(new EmptyBorder(6, 8, 6, 8));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        add(mainPanel);

        refreshPetTable();
    }

    /**
     * Creates the header area containing the pet image,
     * application title, and navigation buttons.
     *
     * @return the completed header panel
     */

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));

        // Pet image
        JLabel imageLabel = new JLabel();

        java.net.URL imageURL = getClass().getResource("/pet_header.png");

        if (imageURL != null) {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imageURL);

            Image scaledImage = icon.getImage().getScaledInstance(
                    230,
                    -1,
                    Image.SCALE_SMOOTH
            );

            imageLabel.setIcon(new ImageIcon(scaledImage));
        }

        JLabel titleLabel = new JLabel(
                "Pet Health Care Tracker",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(new Color(33, 66, 120));

        RoundedPanel titlePanel =
                new RoundedPanel(new Color(225, 238, 252), 30);

        titlePanel.setLayout(new GridBagLayout());

        titlePanel.setBorder(
                BorderFactory.createEmptyBorder(6, 20, 6, 20)
        );

        titlePanel.setPreferredSize(new Dimension(360, 50));

        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 5)
        );

        JButton loadButton = new JButton("Load Records");
        JButton displayButton = new JButton("Display Records");
        JButton addButton = new JButton("Add Pet");
        JButton updateButton = new JButton("Update Pet");
        JButton removeButton = new JButton("Remove Pet");
        JButton healthButton = new JButton("Health Summary");
        JButton exitButton = new JButton("Exit");

        JButton[] buttons = {
                loadButton,
                displayButton,
                addButton,
                updateButton,
                removeButton,
                healthButton,
                exitButton
        };

        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 12));
            button.setPreferredSize(new Dimension(120, 32));
            button.setBackground(new Color(70, 130, 180));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);
        }

        healthButton.setPreferredSize(new Dimension(155, 32));
        displayButton.setPreferredSize(new Dimension(145, 32));

        loadButton.addActionListener(event -> loadRecords());

        displayButton.addActionListener(event -> refreshPetTable());

        addButton.addActionListener(
                (ActionEvent event) -> showAddPetDialog()
        );

        updateButton.addActionListener(
                event -> showUpdatePetDialog()
        );

        removeButton.addActionListener(
                event -> removeSelectedPet()
        );

        healthButton.addActionListener(
                event -> showHealthSummary()
        );

        exitButton.addActionListener(
                event -> exitApplication()
        );

        buttonPanel.add(loadButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(healthButton);
        buttonPanel.add(exitButton);

        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel spacer = new JLabel();
        spacer.setPreferredSize(imageLabel.getPreferredSize());

        topPanel.add(imageLabel, BorderLayout.WEST);

        JPanel titleWrapper = new JPanel(new GridBagLayout());
        titleWrapper.setOpaque(false);
        titleWrapper.add(titlePanel);

        topPanel.add(titleWrapper, BorderLayout.CENTER);
        topPanel.add(spacer, BorderLayout.EAST);

        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(buttonPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    /**
     * Creates the table used to display pet records.
     *
     * @return a scroll pane containing the pet records table
     */

    private JScrollPane createTablePanel() {
        String[] columnNames = {
                "Pet ID",
                "Name",
                "Species",
                "Breed",
                "Age",
                "Weight",
                "Health Notes",
                "Medication",
                "Last Vet Visit",
                "Vaccinations"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {

            /**
             * Prevents the user from editing cells directly
             * inside the pet records table.
             *
             * @param row the row containing the selected cell
             * @param column the column containing the selected cell
             * @return {@code false} because table cells are not editable
             */

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        petTable = new JTable(tableModel);

        petTable.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    /**
                     * Prepares a table cell for display and applies
                     * alternating background colors to the table rows.
                     *
                     * @param table the table containing the cell
                     * @param value the value displayed in the cell
                     * @param isSelected whether the cell is selected
                     * @param hasFocus whether the cell currently has focus
                     * @param row the row containing the cell
                     * @param column the column containing the cell
                     * @return the component used to display the table cell
                     */

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        Component c =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );

                        if (!isSelected) {
                            if (row % 2 == 0) {
                                c.setBackground(Color.WHITE);
                            } else {
                                c.setBackground(
                                        new Color(245, 248, 252)
                                );
                            }
                        }

                        return c;
                    }
                }
        );

        petTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        petTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        petTable.setRowHeight(26);
        petTable.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        petTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        petTable.getTableHeader().setBackground(
                new Color(220, 235, 250)
        );

        petTable.getTableHeader().setForeground(
                new Color(30, 60, 100)
        );

        petTable.setSelectionBackground(
                new Color(190, 215, 240)
        );

        petTable.setSelectionForeground(Color.BLACK);
        petTable.setGridColor(new Color(210, 210, 210));
        petTable.setShowGrid(true);
        petTable.setFillsViewportHeight(true);

        petTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(65);

        petTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(120);

        petTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(90);

        petTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        petTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(115);

        petTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(75);

        petTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(210);

        petTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(150);

        petTable.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(110);

        petTable.getColumnModel()
                .getColumn(9)
                .setPreferredWidth(350);

        DefaultTableCellRenderer centerRenderer =
                (DefaultTableCellRenderer)
                        petTable.getTableHeader()
                                .getDefaultRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(petTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Pet Records")
        );

        return scrollPane;
    }

    /**
     * Determines whether an optional date is blank or follows
     * the MM/DD/YY or MM/DD/YYYY format.
     *
     * @param dateText the date text to validate
     * @return {@code true} if the date is blank or valid;
     * otherwise {@code false}
     */

    private boolean isValidOptionalDate(String dateText) {

        if (dateText == null || dateText.trim().isEmpty()) {
            return true;
        }

        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("M/d/uu")
                        .withResolverStyle(ResolverStyle.STRICT),

                DateTimeFormatter.ofPattern("M/d/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT)
        };

        for (DateTimeFormatter formatter : formats) {
            try {
                LocalDate.parse(dateText.trim(), formatter);
                return true;

            } catch (DateTimeParseException ignored) {
            }
        }

        return false;
    }

    /**
     * Opens a file chooser and loads pet records
     * from the selected SQLite database.
     */

    private void loadRecords() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Pet Database");

        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            statusLabel.setText("Database selection canceled.");
            return;
        }

        File databaseFile = chooser.getSelectedFile();

        if (petManager.connectToDatabase(
                databaseFile.getAbsolutePath())) {

            refreshPetTable();

            statusLabel.setText(
                    "Loaded database: " + databaseFile.getName()
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to connect to the selected database.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );

            statusLabel.setText("Database connection failed.");
        }
    }

    /**
     * Refreshes the pet records table using the
     * current records stored by the PetManager.
     */

    private void refreshPetTable() {

        tableModel.setRowCount(0);

        ArrayList<Pet> pets = petManager.getPets();

        for (Pet pet : pets) {

            String ageDisplay;

            if (pet.getAge() == 0) {
                ageDisplay = pet.getAgeMonths() + " months";
            } else if (pet.getAgeMonths() == 0) {
                ageDisplay = pet.getAge() + " years";
            } else {
                ageDisplay =
                        pet.getAge() + " years "
                                + pet.getAgeMonths() + " months";
            }

            tableModel.addRow(new Object[]{
                    pet.getPetID(),
                    pet.getPetName(),
                    pet.getSpecies(),
                    pet.getBreed(),
                    ageDisplay,
                    pet.getWeight(),
                    pet.getHealthNotes(),
                    pet.getMedicationName(),
                    pet.getLastVetVisit(),
                    pet.getVaccinationRecords()
            });
        }

        statusLabel.setText(
                pets.size() + " pet record(s) displayed."
        );
    }

    /**
     * Formats a pet's age using years and months.
     *
     * @param pet the pet whose age will be formatted
     * @return the pet's age written in years, months, or both
     */
    private String formatAge(Pet pet) {
        if (pet.getAge() == 0) {
            return pet.getAgeMonths() + " months";
        }

        if (pet.getAgeMonths() == 0) {
            return pet.getAge() + " years";
        }

        return pet.getAge() + " years " +
                pet.getAgeMonths() + " months";
    }

    /**
     * Asks the user to confirm before closing the application.
     */

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    /**
     * Displays a form that allows the user to enter
     * and add a new pet record.
     */

    private void showAddPetDialog() {

        JTextField petIDField = new JTextField();
        JTextField petNameField = new JTextField();
        JTextField speciesField = new JTextField();
        JTextField breedField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField ageMonthsField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField healthNotesField = new JTextField();
        JTextField medicationField = new JTextField();
        JTextField lastVetVisitField = new JTextField();
        JTextField vaccinationsField = new JTextField();

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));

        formPanel.add(new JLabel("Pet ID:"));
        formPanel.add(petIDField);

        formPanel.add(new JLabel("Pet Name:"));
        formPanel.add(petNameField);

        formPanel.add(new JLabel("Species:"));
        formPanel.add(speciesField);

        formPanel.add(new JLabel("Breed:"));
        formPanel.add(breedField);

        formPanel.add(new JLabel("Age (years):"));
        formPanel.add(ageField);

        formPanel.add(new JLabel("Age (months 0-11):"));
        formPanel.add(ageMonthsField);

        formPanel.add(new JLabel("Weight (pounds):"));
        formPanel.add(weightField);

        formPanel.add(new JLabel("Health Notes:"));
        formPanel.add(healthNotesField);

        formPanel.add(new JLabel("Medication:"));
        formPanel.add(medicationField);

        formPanel.add(new JLabel("Last Vet Visit (MM/DD/YYYY):"));
        formPanel.add(lastVetVisitField);

        formPanel.add(new JLabel("Vaccinations:"));
        formPanel.add(vaccinationsField);

        while (true) {

            int option = JOptionPane.showConfirmDialog(
                    this,
                    formPanel,
                    "Add Pet Record",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option != JOptionPane.OK_OPTION) {
                statusLabel.setText("Add Pet was canceled.");
                return;
            }

            try {
                String petIDText = petIDField.getText().trim();
                String petName = petNameField.getText().trim();
                String species = speciesField.getText().trim();
                String breed = breedField.getText().trim();
                String ageText = ageField.getText().trim();
                String ageMonthsText = ageMonthsField.getText().trim();
                String weightText = weightField.getText().trim();

                if (petIDText.isEmpty()
                        || petName.isEmpty()
                        || species.isEmpty()
                        || breed.isEmpty()
                        || ageText.isEmpty()
                        || ageMonthsText.isEmpty()
                        || weightText.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Pet ID, name, species, breed, age, age months, and weight are required.",
                            "Missing Information",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                int petID = Integer.parseInt(petIDText);
                int age = Integer.parseInt(ageText);
                int ageMonths = Integer.parseInt(ageMonthsText);
                double weight = Double.parseDouble(weightText);

                if (petID <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Pet ID must be a positive whole number.",
                            "Invalid Pet ID",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (petManager.findPetByID(petID) != null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "A pet with that ID already exists. Please enter a unique Pet ID.",
                            "Duplicate Pet ID",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (age < 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Age cannot be negative.",
                            "Invalid Age",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (ageMonths < 0 || ageMonths > 11) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Age in months must be between 0 and 11.",
                            "Invalid Age",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (age == 0 && ageMonths == 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "A pet must be at least 1 month old.",
                            "Invalid Age",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (weight <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Weight must be greater than zero.",
                            "Invalid Weight",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                if (!petName.matches(".*[A-Za-z].*")
                        || !species.matches(".*[A-Za-z].*")
                        || !breed.matches(".*[A-Za-z].*")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Pet name, species, and breed must contain letters.",
                            "Invalid Text",
                            JOptionPane.ERROR_MESSAGE
                    );

                    continue;
                }

                String healthNotes = healthNotesField.getText().trim();
                String medicationName = medicationField.getText().trim();
                String lastVetVisit = lastVetVisitField.getText().trim();
                String vaccinationRecords = vaccinationsField.getText().trim();

                if (!isValidOptionalDate(lastVetVisit)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Enter a valid date using MM/DD/YY or MM/DD/YYYY, or leave it blank.",
                            "Invalid Date",
                            JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

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

                if (petManager.addPetRecord(newPet)) {
                    refreshPetTable();

                    JOptionPane.showMessageDialog(
                            this,
                            "Pet record added successfully.",
                            "Pet Added",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    statusLabel.setText(
                            "Pet record added successfully: "
                                    + petName
                                    + " (ID "
                                    + petID
                                    + ")."
                    );

                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "The pet record could not be added.",
                        "Add Pet Failed",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pet ID, age, age months, and weight must contain valid numbers.",
                        "Invalid Number",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

        /**
         * Displays a form that allows the user to
         * update the selected pet record.
         */

        private void showUpdatePetDialog() {

            int selectedRow = petTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a pet record from the table first.",
                        "No Pet Selected",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int petID = Integer.parseInt(
                    tableModel.getValueAt(selectedRow, 0).toString()
            );

            Pet pet = petManager.findPetByID(petID);

            if (pet == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "The selected pet record could not be found.",
                        "Pet Not Found",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JTextField petNameField = new JTextField(pet.getPetName());
            JTextField speciesField = new JTextField(pet.getSpecies());
            JTextField breedField = new JTextField(pet.getBreed());
            JTextField ageField = new JTextField(String.valueOf(pet.getAge()));
            JTextField ageMonthsField =
                    new JTextField(String.valueOf(pet.getAgeMonths()));
            JTextField weightField =
                    new JTextField(String.valueOf(pet.getWeight()));
            JTextField healthNotesField =
                    new JTextField(pet.getHealthNotes());
            JTextField medicationField =
                    new JTextField(pet.getMedicationName());
            JTextField lastVetVisitField =
                    new JTextField(pet.getLastVetVisit());
            JTextField vaccinationsField =
                    new JTextField(pet.getVaccinationRecords());

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));

            formPanel.add(new JLabel("Pet ID:"));
            formPanel.add(new JLabel(String.valueOf(pet.getPetID())));

            formPanel.add(new JLabel("Pet Name:"));
            formPanel.add(petNameField);

            formPanel.add(new JLabel("Species:"));
            formPanel.add(speciesField);

            formPanel.add(new JLabel("Breed:"));
            formPanel.add(breedField);

            formPanel.add(new JLabel("Age (years):"));
            formPanel.add(ageField);

            formPanel.add(new JLabel("Age (months 0-11):"));
            formPanel.add(ageMonthsField);

            formPanel.add(new JLabel("Weight (pounds):"));
            formPanel.add(weightField);

            formPanel.add(new JLabel("Health Notes:"));
            formPanel.add(healthNotesField);

            formPanel.add(new JLabel("Medication:"));
            formPanel.add(medicationField);

            formPanel.add(new JLabel("Last Vet Visit (MM/DD/YYYY):"));
            formPanel.add(lastVetVisitField);

            formPanel.add(new JLabel("Vaccinations:"));
            formPanel.add(vaccinationsField);

            while (true) {

                int option = JOptionPane.showConfirmDialog(
                        this,
                        formPanel,
                        "Update Pet Record",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (option != JOptionPane.OK_OPTION) {
                    statusLabel.setText("Pet update was canceled.");
                    return;
                }

                try {
                    String petName = petNameField.getText().trim();
                    String species = speciesField.getText().trim();
                    String breed = breedField.getText().trim();

                    int age = Integer.parseInt(ageField.getText().trim());
                    int ageMonths =
                            Integer.parseInt(ageMonthsField.getText().trim());
                    double weight =
                            Double.parseDouble(weightField.getText().trim());

                    if (petName.isEmpty()
                            || species.isEmpty()
                            || breed.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Pet name, species, and breed are required.",
                                "Missing Information",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    if (age < 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Age cannot be negative.",
                                "Invalid Age",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    if (ageMonths < 0 || ageMonths > 11) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Age months must be between 0 and 11.",
                                "Invalid Age Months",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    if (age == 0 && ageMonths == 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "A pet must be at least 1 month old.",
                                "Invalid Age",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    if (weight <= 0) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Weight must be greater than zero.",
                                "Invalid Weight",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    String lastVetVisit = lastVetVisitField.getText().trim();

                    if (!isValidOptionalDate(lastVetVisit)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "Enter a valid date using MM/DD/YY or MM/DD/YYYY, or leave it blank.",
                                "Invalid Date",
                                JOptionPane.ERROR_MESSAGE
                        );
                        continue;
                    }

                    pet.setPetName(petName);
                    pet.setSpecies(species);
                    pet.setBreed(breed);
                    pet.setAge(age);
                    pet.setAgeMonths(ageMonths);
                    pet.setWeight(weight);
                    pet.setHealthNotes(
                            healthNotesField.getText().trim()
                    );
                    pet.setMedicationName(
                            medicationField.getText().trim()
                    );
                    pet.setLastVetVisit(
                            lastVetVisitField.getText().trim()
                    );
                    pet.setVaccinationRecords(
                            vaccinationsField.getText().trim()
                    );

                    if (!petManager.updatePetRecord(pet)) {
                        JOptionPane.showMessageDialog(
                                this,
                                "The pet record could not be updated in the database.",
                                "Update Failed",
                                JOptionPane.ERROR_MESSAGE
                        );

                        statusLabel.setText("Pet update failed.");
                        return;
                    }

                    refreshPetTable();

                    JOptionPane.showMessageDialog(
                            this,
                            "Pet record updated successfully.",
                            "Pet Updated",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    statusLabel.setText(
                            "Pet ID " + petID + " was updated successfully."
                    );

                    return;

                } catch (NumberFormatException exception) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Age, age months, and weight must contain valid numbers.",
                            "Invalid Number",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }

    /**
     * Removes the pet record selected in the table
     * after the user confirms the removal.
     */

    private void removeSelectedPet() {

        int selectedRow = petTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a pet record from the table first.",
                    "No Pet Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int petID = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        String petName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove "
                        + petName
                        + " (ID "
                        + petID
                        + ")?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            statusLabel.setText("Pet removal was canceled.");
            return;
        }

        boolean removed = petManager.removePetByID(petID);

        if (removed) {
            refreshPetTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Pet record removed successfully.",
                    "Pet Removed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            statusLabel.setText(
                    "Pet record removed successfully: "
                            + petName
                            + " (ID "
                            + petID
                            + ")."
            );

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "The selected pet record could not be removed.",
                    "Removal Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            statusLabel.setText("Pet removal failed.");
        }
    }

    /**
     * Displays a health summary and calculated health score
     * for the pet selected in the table.
     */

    private void showHealthSummary() {

        int selectedRow = petTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a pet record from the table first.",
                    "No Pet Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int petID = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        Pet pet = petManager.findPetByID(petID);

        if (pet == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "The selected pet record could not be found.",
                    "Pet Not Found",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        HealthCalculator calculator = new HealthCalculator();
        int healthScore = calculator.calculateHealthScore(pet);

        String overallAssessment = getHealthAssessment(healthScore);

        String summary =
                "PET HEALTH SUMMARY\n" +
                        "==============================\n\n" +
                        "Pet ID: " + pet.getPetID() + "\n" +
                        "Name: " + pet.getPetName() + "\n" +
                        "Species: " + pet.getSpecies() + "\n" +
                        "Breed: " + pet.getBreed() + "\n" +
                        "Age: " + formatAge(pet) + "\n" +
                        "Weight: " + pet.getWeight() + " pounds\n\n" +

                        "Health Notes:\n" +
                        formatOptionalValue(pet.getHealthNotes()) + "\n\n" +

                        "Medication:\n" +
                        formatOptionalValue(pet.getMedicationName()) + "\n\n" +

                        "Last Vet Visit:\n" +
                        formatOptionalValue(pet.getLastVetVisit()) + "\n\n" +

                        "Vaccination Records:\n" +
                        formatOptionalValue(pet.getVaccinationRecords()) + "\n\n" +

                        "==============================\n" +
                        "Health Score: " + healthScore + " / 100\n" +
                        "Overall Assessment: " + overallAssessment;

        JTextArea summaryArea = new JTextArea(summary);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summaryArea.setRows(22);
        summaryArea.setColumns(45);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setCaretPosition(0);

        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);

        JOptionPane.showMessageDialog(
                this,
                summaryScrollPane,
                "Health Summary - " + pet.getPetName(),
                JOptionPane.INFORMATION_MESSAGE
        );

        statusLabel.setText(
                "Health summary generated for "
                        + pet.getPetName()
                        + " (ID "
                        + petID
                        + ")."
        );
    }

    /**
     * Returns a written health assessment based on
     * the pet's calculated health score.
     *
     * @param score the calculated health score
     * @return a written description of the pet's overall health
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

    /**
     * Returns a value for display or the word "None"
     * when the optional value is blank.
     *
     * @param value the optional value to format
     * @return the original value or "None" if it is blank
     */
    private String formatOptionalValue(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "None";
        }

        return value;
    }

    /**
     * Starts the Pet Health Care Tracker graphical user interface.
     *
     * @param args command-line arguments that are not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PetCareGUI gui = new PetCareGUI();
            gui.setVisible(true);
        });
    }
}


