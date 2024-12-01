import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
                createMainFrame();
            }
        });
    }

    // Main frame of the application
    private static void createMainFrame() {
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
        JPanel managePage = createManagePage(CONTENT_BACKGROUND_COLOR);

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
                    for (Component component : radioPanel.getComponents()) {
                        if (component instanceof JRadioButton) {
                            if (((JRadioButton) component).isSelected()) {
                                studentData.add(((JRadioButton) component).getText());
                            }
                            // If there is no button selected, add an empty string to maintain array size
                            if (studentData.size() < 4) {
                                studentData.add("");
                            }
                        }
                    }
                }
            }
            // Validate user inputs
            String errorMessage = validateStudentData(studentData);
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
    private static String validateStudentData(List<String> studentData) {
        // Check if all fields are filled
        for (String field : studentData) {
            if (field.isEmpty()) {
                return "All fields must be filled!";
            }
        }

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

    // Manage page panel creation
    private static JPanel createManagePage(Color contentBackgroundColor) {
        JPanel managePage = new JPanel();
        managePage.setBackground(contentBackgroundColor);

        return managePage;
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
}
