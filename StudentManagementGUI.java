import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Student Class
class Student {
    private final String name;
    private final String rollNumber;
    private final String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Roll Number: " + rollNumber + ", Grade: " + grade;
    }
}

// StudentManagementSystem Class
class StudentManagementSystem {
    private final List<Student> students;
    private final String filename = "students.txt";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(String rollNumber) {
        students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        saveStudents();
    }

    public Student searchStudent(String rollNumber) {
        return students.stream()
                .filter(student -> student.getRollNumber().equals(rollNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    private void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            
        }
    }

    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            // Handle exception
        }
    }
}


public class StudentManagementGUI extends JFrame {
    private final StudentManagementSystem system;
    private final JTextArea outputArea;
    private final JTextField nameField;
    private final JTextField rollNumberField;
    private final JTextField gradeField;
    private final JButton addButton;
    private final JButton removeButton;
    private final JButton searchButton;
    private final JButton displayButton;

    public StudentManagementGUI() {
        system = new StudentManagementSystem();

        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        inputPanel.add(rollNumberField);

        inputPanel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        // Create buttons with smaller size
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        searchButton = new JButton("Search");
        displayButton = new JButton("Display");

        Dimension buttonSize = new Dimension(100, 30); 
        addButton.setPreferredSize(buttonSize);
        removeButton.setPreferredSize(buttonSize);
        searchButton.setPreferredSize(buttonSize);
        displayButton.setPreferredSize(buttonSize);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); 
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        removeButton.addActionListener(e -> removeStudent());
        searchButton.addActionListener(e -> searchStudent());
        displayButton.addActionListener(e -> displayAllStudents());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        String rollNumber = rollNumberField.getText();
        String grade = gradeField.getText();
        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Student student = new Student(name, rollNumber, grade);
        system.addStudent(student);
        clearFields();
        JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeStudent() {
        String rollNumber = rollNumberField.getText();
        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        system.removeStudent(rollNumber);
        clearFields();
        JOptionPane.showMessageDialog(this, "Student removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchStudent() {
        String rollNumber = rollNumberField.getText();
        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Roll Number must be provided.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Student student = system.searchStudent(rollNumber);
        if (student != null) {
            outputArea.setText(student.toString());
        } else {
            outputArea.setText("Student not found.");
        }
    }

    private void displayAllStudents() {
        outputArea.setText("");
        for (Student student : system.getAllStudents()) {
            outputArea.append(student.toString() + "\n");
        }
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}
