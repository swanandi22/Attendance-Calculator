import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class AttendanceCalculatorGUI extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JTextField totalClassesField;
    private JTextField attendedClassesField;
    private JTextField minimumPercentageField;
    private JTextField targetPercentageField;
    
    private JLabel resultLabel;
    private JTextArea detailsArea;
    
    private AttendanceManager attendanceManager;
    
    public AttendanceCalculatorGUI() {
        attendanceManager = new AttendanceManager();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Attendance Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JPanel studentPanel = createStudentPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(studentPanel, gbc);
        
        JPanel calculationPanel = createCalculationPanel();
        gbc.gridy = 1;
        mainPanel.add(calculationPanel, gbc);
        
        JPanel buttonPanel = createButtonPanel();
        gbc.gridy = 2;
        mainPanel.add(buttonPanel, gbc);
        
        JPanel resultsPanel = createResultsPanel();
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(resultsPanel, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        panel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Attended Classes:"), gbc);
        gbc.gridx = 1;
        attendedClassesField = new JTextField(15);
        panel.add(attendedClassesField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Total Classes:"), gbc);
        gbc.gridx = 1;
        totalClassesField = new JTextField(15);
        panel.add(totalClassesField, gbc);
        
        return panel;
    }
    
    private JPanel createCalculationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Calculation Parameters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Minimum % for Exam:"), gbc);
        gbc.gridx = 1;
        minimumPercentageField = new JTextField("75", 10);
        panel.add(minimumPercentageField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Target %:"), gbc);
        gbc.gridx = 1;
        targetPercentageField = new JTextField("80", 10);
        panel.add(targetPercentageField, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton calculateButton = new JButton("Calculate Attendance");
        calculateButton.addActionListener(new CalculateButtonListener());
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearFields());
        
        panel.add(calculateButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));
        
        resultLabel = new JLabel("Enter student data and click Calculate");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        detailsArea = new JTextArea(10, 40);
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(detailsArea);
        
        panel.add(resultLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        totalClassesField.setText("");
        attendedClassesField.setText("");
        minimumPercentageField.setText("75");
        targetPercentageField.setText("80");
        resultLabel.setText("Enter student data and click Calculate");
        detailsArea.setText("");
    }
    
    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();
                int totalClasses = Integer.parseInt(totalClassesField.getText().trim());
                int attendedClasses = Integer.parseInt(attendedClassesField.getText().trim());
                double minimumPercentage = Double.parseDouble(minimumPercentageField.getText().trim());
                double targetPercentage = Double.parseDouble(targetPercentageField.getText().trim());
                
                if (!attendanceManager.validateStudentData(name, id, totalClasses, attendedClasses)) {
                    JOptionPane.showMessageDialog(AttendanceCalculatorGUI.this,
                            attendanceManager.getValidationMessage(),
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Student student = new Student(name, id);
                student.setTotalClasses(totalClasses);
                student.setAttendedClasses(attendedClasses);
                
                double attendancePercentage = attendanceManager.calculateAttendancePercentage(student);
                boolean eligibleForExam = attendanceManager.isEligibleForExam(student, minimumPercentage);
                int requiredClasses = attendanceManager.getRequiredClassesToAttend(student, targetPercentage);
                String status = attendanceManager.getAttendanceStatus(student);
                
                resultLabel.setText(String.format("Attendance: %.2f%% - Status: %s", 
                        attendancePercentage, status));
                
                StringBuilder details = new StringBuilder();
                details.append("=== ATTENDANCE REPORT ===\n\n");
                details.append(String.format("Student: %s (%s)\n", student.getName(), student.getId()));
                details.append(String.format("Role: %s\n\n", student.getRole()));
                
                details.append("=== ATTENDANCE DETAILS ===\n");
                details.append(String.format("Total Classes: %d\n", totalClasses));
                details.append(String.format("Attended Classes: %d\n", attendedClasses));
                details.append(String.format("Missed Classes: %d\n", totalClasses - attendedClasses));
                details.append(String.format("Attendance Percentage: %.2f%%\n", attendancePercentage));
                details.append(String.format("Attendance Status: %s\n\n", status));
                
                details.append("=== EXAM ELIGIBILITY ===\n");
                details.append(String.format("Minimum Required: %.1f%%\n", minimumPercentage));
                details.append(String.format("Eligible for Exam: %s\n\n", eligibleForExam ? "YES" : "NO"));
                
                details.append("=== TARGET ANALYSIS ===\n");
                details.append(String.format("Target Percentage: %.1f%%\n", targetPercentage));
                
                if (requiredClasses == 0) {
                    details.append("Target already achieved!\n");
                } else if (requiredClasses > 0) {
                    details.append(String.format("Classes needed to reach target: %d\n", requiredClasses));
                } else {
                    details.append("Target percentage not achievable with current data.\n");
                }
                
                detailsArea.setText(details.toString());
                
                if (attendancePercentage >= minimumPercentage+5) {
                    resultLabel.setForeground(Color.GREEN.darker());
                } else if (attendancePercentage >= minimumPercentage) {
                    resultLabel.setForeground(Color.ORANGE.darker());
                } else {
                    resultLabel.setForeground(Color.RED);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AttendanceCalculatorGUI.this,
                        "Please enter valid numbers for all numeric fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AttendanceCalculatorGUI().setVisible(true);
        });
    }
}
