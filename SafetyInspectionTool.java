import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SafetyInspectionTool extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField areaField;
    private JTextField issueField;
    private JComboBox<String> severityComboBox;

    public SafetyInspectionTool() {
        setTitle("Safety Inspection Workflow Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table to display checklist
        String[] columnNames = {"Area", "Issues Found", "Severity"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel for logging inspections
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new GridLayout(4, 2));

        logPanel.add(new JLabel("Area Inspected:"));
        areaField = new JTextField();
        logPanel.add(areaField);

        logPanel.add(new JLabel("Issues Found:"));
        issueField = new JTextField();
        logPanel.add(issueField);

        logPanel.add(new JLabel("Severity Level:"));
        severityComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        logPanel.add(severityComboBox);

        JButton logButton = new JButton("Log Inspection");
        logButton.addActionListener(new LogInspectionListener());
        logPanel.add(logButton);

        add(logPanel, BorderLayout.SOUTH);

        // Load Checklist button
        JButton loadButton = new JButton("Load Checklist");
        loadButton.addActionListener(new LoadChecklistListener());
        add(loadButton, BorderLayout.NORTH);

        // Save Checklist button
        JButton saveButton = new JButton("Save Checklist");
        saveButton.addActionListener(new SaveChecklistListener());
        add(saveButton, BorderLayout.EAST);
    }

    // Listener to log new inspection
    private class LogInspectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String area = areaField.getText();
            String issue = issueField.getText();
            String severity = (String) severityComboBox.getSelectedItem();

            if (!area.isEmpty() && !issue.isEmpty()) {
                model.addRow(new Object[]{area, issue, severity});
            } else {
                JOptionPane.showMessageDialog(SafetyInspectionTool.this, "Please fill all fields");
            }
        }
    }

    // Listener to load checklist from CSV
    private class LoadChecklistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(SafetyInspectionTool.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadChecklist(file);
            }
        }

        private void loadChecklist(File file) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    model.addRow(data);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Listener to save checklist to CSV
    private class SaveChecklistListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(SafetyInspectionTool.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveChecklist(file);
            }
        }

        private void saveChecklist(File file) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < model.getRowCount(); i++) {
                    String area = (String) model.getValueAt(i, 0);
                    String issue = (String) model.getValueAt(i, 1);
                    String severity = (String) model.getValueAt(i, 2);
                    bw.write(area + "," + issue + "," + severity);
                    bw.newLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SafetyInspectionTool tool = new SafetyInspectionTool();
            tool.setVisible(true);
        });
    }
}
