package view;

import model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Student Add/Edit Dialog
 * Syllabus Unit 4 — Swing: Text Fields, Password Fields, Combo Boxes, Radio Buttons,
 * Borders, Dialog Boxes, Data Exchange, GridLayout, GridBagLayout
 */
public class StudentFormDialog extends JDialog {

    private final boolean isEditMode;
    private Student resultStudent = null;

    // ── Form Fields ───────────────────────────────────────────────────────────
    private JTextField    tfName, tfRoll, tfEmail, tfPhone, tfAge, tfCgpa;
    private JComboBox<String> cbCourse, cbBranch, cbGender;

    public StudentFormDialog(Frame parent, String title, Student existingStudent) {
        super(parent, title, true);  // modal dialog
        this.isEditMode = (existingStudent != null);
        buildUI();
        if (isEditMode) populateFields(existingStudent);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        mainPanel.setBackground(new Color(245, 247, 250));

        // ── Title ──────────────────────────────────────────────────────────
        JLabel lblTitle = new JLabel(isEditMode ? "✏  Edit Student Record" : "➕  Add New Student");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 58, 138));
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        // ── Form Grid ──────────────────────────────────────────────────────
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        String[] courses  = {"B.Tech", "M.Tech", "MBA", "MCA", "B.Sc", "M.Sc"};
        String[] branches = {"CSE", "ECE", "EEE", "MECH", "CIVIL", "IT", "AIDS", "AIML"};
        String[] genders  = {"Male", "Female", "Other"};

        cbCourse = new JComboBox<>(courses);
        cbBranch = new JComboBox<>(branches);
        cbGender = new JComboBox<>(genders);

        tfName  = new JTextField(18);
        tfRoll  = new JTextField(18);
        tfEmail = new JTextField(18);
        tfPhone = new JTextField(18);
        tfAge   = new JTextField(18);
        tfCgpa  = new JTextField(18);

        styleCombo(cbCourse);
        styleCombo(cbBranch);
        styleCombo(cbGender);

        Object[][] rows = {
            {"Full Name *",   tfName},
            {"Roll Number *", tfRoll},
            {"Course *",      cbCourse},
            {"Branch *",      cbBranch},
            {"CGPA *",        tfCgpa},
            {"Email",         tfEmail},
            {"Phone",         tfPhone},
            {"Age *",         tfAge},
            {"Gender",        cbGender},
        };

        for (int i = 0; i < rows.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            gbc.weightx = 0;
            JLabel lbl = new JLabel((String) rows[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setForeground(new Color(55, 65, 81));
            formPanel.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
            Component comp = (Component) rows[i][1];
            if (comp instanceof JTextField tf) styleTextField(tf);
            formPanel.add(comp, gbc);
            gbc.fill = GridBagConstraints.NONE;
        }

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ── Buttons ────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPanel.setBackground(new Color(245, 247, 250));

        JButton btnCancel = createButton("Cancel", new Color(107, 114, 128), Color.WHITE);
        JButton btnSave   = createButton(isEditMode ? "Update" : "Save", new Color(37, 99, 235), Color.WHITE);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(e -> onSave());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void onSave() {
        try {
            String name  = tfName.getText().trim();
            String roll  = tfRoll.getText().trim();
            String email = tfEmail.getText().trim();
            String phone = tfPhone.getText().trim();
            String cgpaStr = tfCgpa.getText().trim();
            String ageStr  = tfAge.getText().trim();

            if (name.isEmpty() || roll.isEmpty() || cgpaStr.isEmpty() || ageStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields (*).",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double cgpa = Double.parseDouble(cgpaStr);
            int    age  = Integer.parseInt(ageStr);

            if (cgpa < 0 || cgpa > 10) {
                JOptionPane.showMessageDialog(this, "CGPA must be between 0.0 and 10.0",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            resultStudent = new Student(
                name, roll,
                (String) cbCourse.getSelectedItem(),
                (String) cbBranch.getSelectedItem(),
                cgpa, email, phone, age,
                (String) cbGender.getSelectedItem()
            );
            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "CGPA and Age must be valid numbers.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void populateFields(Student s) {
        tfName.setText(s.getName());
        tfRoll.setText(s.getRollNumber());
        tfEmail.setText(s.getEmail() != null ? s.getEmail() : "");
        tfPhone.setText(s.getPhone() != null ? s.getPhone() : "");
        tfAge.setText(String.valueOf(s.getAge()));
        tfCgpa.setText(String.valueOf(s.getCgpa()));
        cbCourse.setSelectedItem(s.getCourse());
        cbBranch.setSelectedItem(s.getBranch());
        cbGender.setSelectedItem(s.getGender());
    }

    public Student getResultStudent() { return resultStudent; }

    // ── Styling helpers ────────────────────────────────────────────────────────
    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 34));
        return btn;
    }
}
