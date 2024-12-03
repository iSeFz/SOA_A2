import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class University {
    // Declare constants
    // Color palette
    private static final Color NAVBAR_COLOR = new Color(0x525B44);
    private static final Color SELECTED_BUTTON_COLOR = new Color(0x85A98F);
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(0x5A6C57);
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0x85A98F);
    private static final Color CONTENT_BACKGROUND_COLOR = new Color(0xD3F1DF);

    // Declare students array to store students data
    private static List<Student> students = new ArrayList<>();

    // Declare xmlParser object to load students data
    private static XMLParser xmlParser = new XMLParser();

    // Declare xmlWriter object to store students data
    private static XMLWriter xmlWriter = new XMLWriter();

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createMainFrame();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static JPanel managePage;

    // Main frame of the application
    private static void createMainFrame() throws Exception {
        students = xmlParser.loadStudents();

        // Create the main frame
        JFrame frame = new JFrame("University Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Create the navbar panel
        JPanel navbar = new JPanel();
        navbar.setBackground(NAVBAR_COLOR);
        navbar.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create content panels
        JPanel addPage = createAddPage(CONTENT_BACKGROUND_COLOR);
        managePage = createManagePage(CONTENT_BACKGROUND_COLOR);

        // Create buttons for the navbar
        JButton addButton = new JButton("Add Students");
        JButton manageButton = new JButton("Manage Students");

        // Style the buttons
        styleButton(addButton, SELECTED_BUTTON_COLOR, 300, 20);
        styleButton(manageButton, BUTTON_BACKGROUND_COLOR, 300, 20);

        // Add action listeners for navigation
        addButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(navbar, BorderLayout.NORTH);
            frame.add(addPage, BorderLayout.CENTER);
            styleButton(addButton, SELECTED_BUTTON_COLOR, 300, 20);
            styleButton(manageButton, BUTTON_BACKGROUND_COLOR, 300, 20);
            frame.revalidate();
            frame.repaint();
        });

        manageButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(navbar, BorderLayout.NORTH);
            frame.add(managePage, BorderLayout.CENTER);
            styleButton(manageButton, SELECTED_BUTTON_COLOR, 300, 20);
            styleButton(addButton, BUTTON_BACKGROUND_COLOR, 300, 20);
            frame.revalidate();
            frame.repaint();
            JScrollPane scrollPane = (JScrollPane) managePage.getComponent(2);
            JPanel studentsListPanel = (JPanel) scrollPane.getViewport().getView();
            loadAndDisplayStudents(studentsListPanel); // Reload the student list
        });

        // Add buttons to the navbar
        navbar.add(addButton);
        navbar.add(manageButton);

        // Set initial content
        frame.add(navbar, BorderLayout.NORTH);
        frame.add(addPage, BorderLayout.CENTER);

        // Set the frame to be visible
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setVisible(true);
    }

    // Add page panel creation
    private static JPanel createAddPage(Color contentBackgroundColor) {
        JPanel addPage = new JPanel();
        addPage.setBackground(contentBackgroundColor);
        addPage.setLayout(new GridBagLayout());

        // Create a wrapper panel to add margins
        JPanel contentWrapper = new JPanel(new GridLayout(8, 2, 10, 10));
        contentWrapper.setBackground(contentBackgroundColor);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Array contains the labels for the fields
        String[] fieldLabels = {
                "Student ID:", "First Name:", "Last Name:",
                "Gender:", "GPA:", "Level:", "Address:"
        };

        // Create button group to group the radio buttons
        // This is created to make sure only one radio button can be selected
        ButtonGroup genderGroup = new ButtonGroup();

        // Add labels and text fields to the panel
        for (int i = 0; i < 7; i++) {
            JLabel label = new JLabel(fieldLabels[i], SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            contentWrapper.add(label);
            // If this is the gender field, add radio buttons instead of the text field
            if (i == 3) {
                // Create a panel for radio buttons
                JPanel radioPanel = new JPanel();
                radioPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                radioPanel.setBackground(contentBackgroundColor);

                // Create actual radio buttons
                JRadioButton maleButton = new JRadioButton("Male");
                maleButton.setBackground(contentBackgroundColor);
                maleButton.setFont(new Font("Arial", Font.PLAIN, 20));
                JRadioButton femaleButton = new JRadioButton("Female");
                femaleButton.setBackground(contentBackgroundColor);
                femaleButton.setFont(new Font("Arial", Font.PLAIN, 20));

                // Add radio buttons to the button group
                genderGroup.add(maleButton);
                genderGroup.add(femaleButton);

                // Add the radio buttons to the panel
                radioPanel.add(maleButton);
                radioPanel.add(femaleButton);
                contentWrapper.add(radioPanel);
                continue;
            }
            JTextField textField = new JTextField();
            textField.setFont(new Font("Arial", Font.PLAIN, 18));
            textField.setHorizontalAlignment(JTextField.CENTER);
            contentWrapper.add(textField);
        }

        // Create buttons for confirming or stopping the process
        JButton stopButton = new JButton("Stop");
        JButton confirmButton = new JButton("Confirm");

        // Style the buttons
        styleButton(stopButton, BUTTON_BACKGROUND_COLOR, 200, 20);
        styleButton(confirmButton, BUTTON_BACKGROUND_COLOR, 200, 20);

        // Add action listener for stopping the process
        stopButton.addActionListener(e -> {
            // Clear contents of text fields
            Component[] components = contentWrapper.getComponents();
            for (Component component : components) {
                if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                }
            }
            // Clear radio buttons selection
            genderGroup.clearSelection();
        });

        // Add action listener for confirming the process
        confirmButton.addActionListener(e -> {
            // Load students data from the XML file
            try {
                students = xmlParser.loadStudents();
                // Check if the return is null, create an empty array
                if (students == null) {
                    students = new ArrayList<>();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error loading students data: " + ex.getMessage(),
                        "Error: Loading students failed!", JOptionPane.ERROR_MESSAGE);
            }
            // Resulting array will contain the text from the text fields
            List<String> studentData = new ArrayList<>();
            Component[] components = contentWrapper.getComponents();
            // Get the user input from the fields
            for (int i = 0; i < components.length; i++) {
                // Get the text from the text fields
                if (components[i] instanceof JTextField) {
                    studentData.add(((JTextField) components[i]).getText());
                }
                // Get the selected radio button
                if (components[i] instanceof JPanel) {
                    JPanel radioPanel = (JPanel) components[i];
                    boolean genderSelected = false;
                    for (Component component : radioPanel.getComponents()) {
                        if (component instanceof JRadioButton) {
                            if (((JRadioButton) component).isSelected()) {
                                studentData.add(((JRadioButton) component).getText());
                                genderSelected = true;
                                break;
                            }
                        }
                    }
                    // If no gender is selected, add an empty string
                    if (!genderSelected) {
                        studentData.add("");
                    }
                }
            }
            // Validate user inputs
            String errorMessage = validateStudentData(studentData, false);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(addPage, errorMessage, "Invalid Input !!", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                // Add the student to the students list
                Student student = new Student(studentData);
                students.add(student);
                // Store the added student data to the XML file
                try {
                    xmlWriter.storeStudentToXML(student);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addPage, "Error adding student: " + ex.getMessage(),
                            "Error: Adding student failed!", JOptionPane.ERROR_MESSAGE);
                }
                // Show success message if the student is stored successfully
                JOptionPane.showMessageDialog(addPage, "Student added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                // Clear all text fields to prepare for next inputs
                for (Component component : components) {
                    if (component instanceof JTextField) {
                        ((JTextField) component).setText("");
                    }
                }
                // Clear radio buttons selection
                genderGroup.clearSelection();
            }
        });

        // Add buttons to the panel
        contentWrapper.add(stopButton);
        contentWrapper.add(confirmButton);

        // Add the wrapper to the main addPage panel
        addPage.add(contentWrapper);

        return addPage;
    }

    // Validate user inputs
    private static String validateStudentData(List<String> studentData, boolean isUpdate) {
        // Check if all fields are filled
        for (String field : studentData) {
            if (field.isEmpty()) {
                return "All fields must be filled!";
            }
        }

        if (!isUpdate) {
            // Check if Student ID is a number
            if (!studentData.get(0).matches("[0-9]+")) {
                return "Student ID must be a number!";
            }

            // Check if Student ID is duplicate
            if (!students.isEmpty()) {
                for (Student student : students) {
                    if (student.getID().equals(studentData.get(0))) {
                        return "Student with the same ID already found!";
                    }
                }
            }
        }

        // Check if student's first name is only characters
        if (!studentData.get(1).matches("[a-zA-Z]+")) {
            return "First name must be only characters!";
        }

        // Check if student's last name is only characters
        if (!studentData.get(2).matches("[a-zA-Z]+")) {
            return "Last name must be only characters!";
        }

        // Check if GPA is a number
        if (!studentData.get(4).matches("[0-9]+(\\.[0-9]+)?")) {
            return "GPA must be a number!";
        }

        // Check if GPA is between 0 and 4
        if (Double.parseDouble(studentData.get(4)) < 0 || Double.parseDouble(studentData.get(4)) > 4) {
            return "GPA must be between 0 and 4!";
        }

        // Check if student's address is only characters
        if (!studentData.get(6).matches("[a-zA-Z]+")) {
            return "Address must be only characters!";
        }

        return null;
    }

    // Helper method to style buttons
    private static void styleButton(JButton button, Color backgroundColor, Integer width, Integer fontSize) {
        button.setBackground(backgroundColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setPreferredSize(new Dimension(width, 40));
    }

    private static JPanel createManagePage(Color contentBackgroundColor) {
        JPanel managePage = new JPanel();
        managePage.setBackground(contentBackgroundColor);
        managePage.setLayout(new BorderLayout());

        // Create the search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JLabel recordCountLabel = new JLabel("Records: " + students.size());
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(recordCountLabel);

        // Add action listener to the search button
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toLowerCase();
            List<Student> filteredStudents = searchStudents(searchText);
            JScrollPane scrollPane = (JScrollPane) managePage.getComponent(2);
            JPanel studentsListPanel = (JPanel) scrollPane.getViewport().getView();
            loadAndDisplayStudents(studentsListPanel, filteredStudents);
            recordCountLabel.setText("Records: " + filteredStudents.size());
        });

        // Create the sort bar
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] sortOptions = { "ID", "First Name", "Last Name", "Gender", "GPA", "Level" };
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        JButton sortAscButton = new JButton("Sort Ascending");
        JButton sortDescButton = new JButton("Sort Descending");
        sortPanel.add(new JLabel("Sort by:"));
        sortPanel.add(sortComboBox);
        sortPanel.add(sortAscButton);
        sortPanel.add(sortDescButton);

        // Add action listener to the sort ascending button
        sortAscButton.addActionListener(e -> {
            String selectedCriteria = (String) sortComboBox.getSelectedItem();
            sortStudents(selectedCriteria, true);
            JScrollPane scrollPane = (JScrollPane) managePage.getComponent(2);
            JPanel studentsListPanel = (JPanel) scrollPane.getViewport().getView();
            try {
                xmlWriter.clearXML();
                for (Student s : students) {
                    xmlWriter.storeStudentToXML(s); // Write remaining students back to the XML file
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            loadAndDisplayStudents(studentsListPanel);
        });

        // Add action listener to the sort descending button
        sortDescButton.addActionListener(e -> {
            String selectedCriteria = (String) sortComboBox.getSelectedItem();
            sortStudents(selectedCriteria, false);
            JScrollPane scrollPane = (JScrollPane) managePage.getComponent(2);
            JPanel studentsListPanel = (JPanel) scrollPane.getViewport().getView();
            try {
                xmlWriter.clearXML();
                for (Student s : students) {
                    xmlWriter.storeStudentToXML(s); // Write remaining students back to the XML file
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            loadAndDisplayStudents(studentsListPanel);
        });

        // Create the header panel
        JPanel headerPanel = new JPanel(new GridLayout(1, 8));
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        headerPanel.setBackground(CONTENT_BACKGROUND_COLOR);

        headerPanel.add(new JLabel("ID", SwingConstants.CENTER));
        headerPanel.add(new JLabel("First Name", SwingConstants.CENTER));
        headerPanel.add(new JLabel("Last Name", SwingConstants.CENTER));
        headerPanel.add(new JLabel("Gender", SwingConstants.CENTER));
        headerPanel.add(new JLabel("GPA", SwingConstants.CENTER));
        headerPanel.add(new JLabel("Level", SwingConstants.CENTER));
        headerPanel.add(new JLabel("Address", SwingConstants.CENTER));
        headerPanel.add(new JLabel("Actions", SwingConstants.CENTER));

        // Create the students list panel
        JPanel studentsListPanel = new JPanel();
        studentsListPanel.setLayout(new BoxLayout(studentsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(studentsListPanel);

        // Load and display students
        loadAndDisplayStudents(studentsListPanel);

        // Set maximum size for panels to ensure they only take up necessary space
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPanel.getPreferredSize().height));
        sortPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, sortPanel.getPreferredSize().height));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, scrollPane.getPreferredSize().height));

        // Create a panel to hold the search, sort, and header panels
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(searchPanel);
        topPanel.add(sortPanel);
        // topPanel.add(headerPanel);

        // // Add components to the managePage
        managePage.add(topPanel, BorderLayout.PAGE_START);
        managePage.add(new JSeparator(), BorderLayout.CENTER);
        managePage.add(scrollPane, BorderLayout.CENTER);

        return managePage;
    }

    // Method to search students based on the search text
    private static List<Student> searchStudents(String searchText) {
        if (searchText.isEmpty()) {
            return students;
        }
        List<Student> filteredStudents = new ArrayList<>();
        for (Student student : students) {
            if (student.getID().toLowerCase().contains(searchText) ||
                    student.getFirstName().toLowerCase().contains(searchText) ||
                    student.getLastName().toLowerCase().contains(searchText) ||
                    student.getGender().toLowerCase().contains(searchText) ||
                    student.getGPA().toString().toLowerCase().contains(searchText) ||
                    student.getLevel().toString().toLowerCase().contains(searchText) ||
                    student.getAddress().toLowerCase().contains(searchText)) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    // Load and display students
    private static void loadAndDisplayStudents(JPanel studentsListPanel, List<Student> studentsToDisplay) {
        studentsListPanel.removeAll();
        for (Student student : studentsToDisplay) {
            JPanel studentPanel = createStudentPanel(student);
            studentsListPanel.add(studentPanel);
        }
        studentsListPanel.revalidate();
        studentsListPanel.repaint();
    }

    // Method to sort students based on the selected criteria and order
    private static void sortStudents(String criteria, boolean ascending) {
        Comparator<Student> comparator;
        switch (criteria) {
            case "ID":
                comparator = Comparator.comparing(Student::getID);
                break;
            case "First Name":
                comparator = Comparator.comparing(Student::getFirstName);
                break;
            case "Last Name":
                comparator = Comparator.comparing(Student::getLastName);
                break;
            case "Gender":
                comparator = Comparator.comparing(Student::getGender);
                break;
            case "GPA":
                comparator = Comparator.comparing(Student::getGPA);
                break;
            case "Level":
                comparator = Comparator.comparing(Student::getLevel);
                break;
            default:
                return;
        }
        if (!ascending) {
            comparator = comparator.reversed();
        }
        students.sort(comparator);
    }

    // Create a panel for each student
    private static JPanel createStudentPanel(Student student) {
        JPanel studentPanel = new JPanel(new GridLayout(1, 8));
        studentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        studentPanel.add(new JLabel(student.getID()));
        studentPanel.add(new JLabel(student.getFirstName()));
        studentPanel.add(new JLabel(student.getLastName()));
        studentPanel.add(new JLabel(student.getGender()));
        studentPanel.add(new JLabel(student.getGPA().toString()));
        studentPanel.add(new JLabel(student.getLevel().toString()));
        studentPanel.add(new JLabel(student.getAddress()));

        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        updateButton.addActionListener(e -> showUpdateStudentDialog(student));
        deleteButton.addActionListener(e -> {
            deleteStudent(student);
            loadAndDisplayStudents((JPanel) studentPanel.getParent());
        });

        studentPanel.add(updateButton);
        studentPanel.add(deleteButton);

        return studentPanel;
    }

    // Show update student dialog
    private static void showUpdateStudentDialog(Student student) {
        JDialog updateDialog = new JDialog();
        updateDialog.setTitle("Update Student");
        updateDialog.setSize(400, 300);
        updateDialog.setLayout(new GridLayout(8, 2));

        updateDialog.add(new JLabel("ID:"));
        JTextField idField = new JTextField(student.getID());
        idField.setEditable(false);
        updateDialog.add(idField);

        updateDialog.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField(student.getFirstName());
        updateDialog.add(firstNameField);

        updateDialog.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField(student.getLastName());
        updateDialog.add(lastNameField);

        // updateDialog.add(new JLabel("Gender:"));
        // JTextField genderField = new JTextField(student.getGender());
        // updateDialog.add(genderField);

        updateDialog.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        if (student.getGender().equalsIgnoreCase("Male")) {
            maleButton.setSelected(true);
        } else if (student.getGender().equalsIgnoreCase("Female")) {
            femaleButton.setSelected(true);
        }
        updateDialog.add(genderPanel);

        updateDialog.add(new JLabel("GPA:"));
        JTextField gpaField = new JTextField(student.getGPA().toString());
        updateDialog.add(gpaField);

        updateDialog.add(new JLabel("Level:"));
        JTextField levelField = new JTextField(student.getLevel().toString());
        updateDialog.add(levelField);

        updateDialog.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(student.getAddress());
        updateDialog.add(addressField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            List<String> studentData = List.of(idField.getText(), firstNameField.getText(),
                    lastNameField.getText(), maleButton.isSelected() ? "Male" : "Female", gpaField.getText(), levelField.getText(),
                    addressField.getText());
            String errorMessage = validateStudentData(studentData, true);
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input !!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            student.setFirstName(firstNameField.getText());
            student.setLastName(lastNameField.getText());
            student.setGender(maleButton.isSelected() ? "Male" : "Female");
            student.setGPA(Double.parseDouble(gpaField.getText()));
            student.setLevel(Integer.parseInt(levelField.getText()));
            student.setAddress(addressField.getText());

            try {
                xmlWriter.clearXML();
                for (Student s : students) {
                    xmlWriter.storeStudentToXML(s); // Write remaining students back to the XML file
                }
                updateDialog.dispose();
                loadAndDisplayStudents((JPanel) ((JScrollPane) managePage.getComponent(2)).getViewport().getView());
                JOptionPane.showMessageDialog(null, "Student updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error updating student: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        updateDialog.add(saveButton);
        updateDialog.setVisible(true);
    }

    // Load and display students
    private static void loadAndDisplayStudents(JPanel studentsListPanel) {
        studentsListPanel.removeAll();
        try {
            if (students != null) {
                for (Student student : students) {
                    JPanel studentPanel = createStudentPanel(student);
                    studentsListPanel.add(studentPanel);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(studentsListPanel, "Error loading students: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        studentsListPanel.revalidate();
        studentsListPanel.repaint();
    }

    // Delete student
    private static void deleteStudent(Student student) {
        students.remove(student);
        try {
            xmlWriter.clearXML();
            for (Student s : students) {
                xmlWriter.storeStudentToXML(s); // Write remaining students back to the XML file
            }
            JOptionPane.showMessageDialog(null, "Student deleted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error deleting student: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
