package view;

import controller.StudentController;
import dao.DatabaseConnection;
import events.StudentEvent;
import events.StudentEventListener;
import model.Student;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main Application Window
 * Syllabus Unit 4 — Swing + MVC:
 *   JMenuBar, JSplitPane, JTabbedPane, JTable, JToolBar, JLabel, JTextField,
 *   BorderLayout, GridLayout, GridBagLayout, Custom Colors & Borders, Status Bar,
 *   JScrollPane, JScrollBar, Tooltips, Keyboard Mnemonics & Accelerators.
 *
 * Also implements StudentEventListener (Unit 2 Custom Events).
 */
public class MainFrame extends JFrame implements StudentEventListener {

    private final StudentController controller;

    // ── UI Components ─────────────────────────────────────────────────────────
    private StudentTableModel  tableModel;
    private JTable             studentTable;
    private JTextField         tfSearch;
    private JLabel             lblStatus;
    private JLabel             lblTotal, lblAvgCGPA;
    private JTabbedPane        tabbedPane;

    // ── Constructor ───────────────────────────────────────────────────────────
    public MainFrame() {
        this.controller = new StudentController();
        controller.addStudentEventListener(this);  // Register as bean listener

        setTitle("🎓  Student Management System — VIT");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { onExit(); }
        });

        buildUI();
        refreshTable();
        updateStats();
    }

    // ── UI Construction ───────────────────────────────────────────────────────
    private void buildUI() {
        // Look & Feel: FlatLaf or Nimbus fallback
        getContentPane().setBackground(new Color(248, 250, 252));

        setJMenuBar(buildMenuBar());

        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(new Color(248, 250, 252));

        contentPanel.add(buildToolbar(),   BorderLayout.NORTH);
        contentPanel.add(buildCenter(),    BorderLayout.CENTER);
        contentPanel.add(buildStatusBar(), BorderLayout.SOUTH);

        setContentPane(contentPanel);
    }

    // ── Menu Bar ─────────────────────────────────────────────────────────────
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(30, 58, 138));

        // File Menu (Mnemonic: Alt+F — Syllabus Unit 4: Keyboard Mnemonics)
        JMenu menuFile = createMenu("File", KeyEvent.VK_F);
        JMenuItem miAdd    = createMenuItem("Add Student",    KeyEvent.VK_N, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        JMenuItem miExport = createMenuItem("Export to CSV",  KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        JMenuItem miExit   = createMenuItem("Exit",           KeyEvent.VK_Q, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

        miAdd.addActionListener(e -> onAddStudent());
        miExport.addActionListener(e -> onExportCSV());
        miExit.addActionListener(e -> onExit());

        menuFile.add(miAdd); menuFile.add(miExport);
        menuFile.addSeparator(); menuFile.add(miExit);

        // Edit Menu
        JMenu menuEdit = createMenu("Edit", KeyEvent.VK_E);
        JMenuItem miEdit   = createMenuItem("Edit Selected",   KeyEvent.VK_E, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        JMenuItem miDelete = createMenuItem("Delete Selected", KeyEvent.VK_D, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        JMenuItem miRefresh= createMenuItem("Refresh",         KeyEvent.VK_R, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        miEdit.addActionListener(e -> onEditStudent());
        miDelete.addActionListener(e -> onDeleteStudent());
        miRefresh.addActionListener(e -> { refreshTable(); updateStats(); });
        menuEdit.add(miEdit); menuEdit.add(miDelete);
        menuEdit.addSeparator(); menuEdit.add(miRefresh);

        // Help Menu
        JMenu menuHelp = createMenu("Help", KeyEvent.VK_H);
        JMenuItem miAbout = createMenuItem("About", KeyEvent.VK_A, null);
        miAbout.addActionListener(e -> showAbout());
        menuHelp.add(miAbout);

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuHelp);
        return menuBar;
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setBackground(new Color(30, 58, 138));
        toolbar.setBorder(new EmptyBorder(8, 12, 8, 12));

        // Left side buttons
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        leftButtons.setOpaque(false);

        JButton btnAdd    = toolButton("➕ Add",    "Add new student (Ctrl+N)", new Color(34, 197, 94));
        JButton btnEdit   = toolButton("✏ Edit",   "Edit selected student (F2)", new Color(59, 130, 246));
        JButton btnDelete = toolButton("🗑 Delete", "Delete selected student (Del)", new Color(239, 68, 68));
        JButton btnRefresh= toolButton("↻ Refresh","Refresh table (F5)", new Color(100, 116, 139));

        btnAdd.addActionListener(e    -> onAddStudent());
        btnEdit.addActionListener(e   -> onEditStudent());
        btnDelete.addActionListener(e -> onDeleteStudent());
        btnRefresh.addActionListener(e-> { refreshTable(); updateStats(); });

        leftButtons.add(btnAdd);
        leftButtons.add(btnEdit);
        leftButtons.add(btnDelete);
        leftButtons.add(new JSeparator(SwingConstants.VERTICAL));
        leftButtons.add(btnRefresh);

        // Right side search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("🔍");
        lblSearch.setForeground(Color.WHITE);
        tfSearch = new JTextField(18);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfSearch.putClientProperty("JTextField.placeholderText", "Search by name, roll, course…");
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 130, 200)),
            new EmptyBorder(4, 8, 4, 8)
        ));

        JButton btnSearch = toolButton("Search", "Search records", new Color(99, 102, 241));
        JButton btnClear  = toolButton("Clear",  "Clear search",   new Color(100, 116, 139));

        btnSearch.addActionListener(e -> onSearch());
        btnClear.addActionListener(e  -> { tfSearch.setText(""); refreshTable(); });

        // Also search on Enter key
        tfSearch.addActionListener(e -> onSearch());

        searchPanel.add(lblSearch);
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);

        toolbar.add(leftButtons, BorderLayout.WEST);
        toolbar.add(searchPanel, BorderLayout.EAST);
        return toolbar;
    }

    // ── Center Panel (Tabbed) ─────────────────────────────────────────────────
    private JComponent buildCenter() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(248, 250, 252));

        // Tab 1: All Students (JTable)
        tabbedPane.addTab("📋  All Students", buildTablePanel());
        tabbedPane.addTab("📊  Statistics",   buildStatsPanel());

        return tabbedPane;
    }

    // ── Table Panel ───────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(8, 10, 8, 10));

        tableModel   = new StudentTableModel();
        studentTable = new JTable(tableModel);

        // Table styling
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(28);
        studentTable.setGridColor(new Color(229, 231, 235));
        studentTable.setSelectionBackground(new Color(219, 234, 254));
        studentTable.setSelectionForeground(new Color(30, 58, 138));
        studentTable.setShowHorizontalLines(true);
        studentTable.setShowVerticalLines(false);
        studentTable.setIntercellSpacing(new Dimension(0, 1));

        // Alternate row colors
        studentTable.setDefaultRenderer(Object.class, new AlternateRowRenderer());
        studentTable.setDefaultRenderer(Integer.class, new AlternateRowRenderer());

        // Column widths
        int[] widths = {40, 160, 110, 80, 70, 60, 170, 110, 40, 65};
        for (int i = 0; i < widths.length; i++) {
            studentTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Header styling (Syllabus: Layout Managers)
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(30, 58, 138));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        // Double-click to edit
        studentTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) onEditStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom info row
        JPanel infoBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoBar.setBackground(new Color(248, 250, 252));
        infoBar.add(new JLabel("💡 Double-click a row to edit  |  Press DEL to delete selected"));
        panel.add(infoBar, BorderLayout.SOUTH);

        return panel;
    }

    // ── Stats Panel ───────────────────────────────────────────────────────────
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill   = GridBagConstraints.BOTH;

        lblTotal   = createStatCard("0",    "Total Students",   new Color(219, 234, 254), new Color(30, 58, 138));
        lblAvgCGPA = createStatCard("0.00", "Average CGPA",     new Color(220, 252, 231), new Color(21, 128, 61));

        JPanel card1 = buildStatCard(lblTotal,   "Total Students",  "👥", new Color(219, 234, 254));
        JPanel card2 = buildStatCard(lblAvgCGPA, "Average CGPA",    "📈", new Color(220, 252, 231));

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 0.5;
        panel.add(card1, gbc);
        gbc.gridx = 1;
        panel.add(card2, gbc);

        // Filler
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 1;
        panel.add(new JPanel() {{ setOpaque(false); }}, gbc);

        return panel;
    }

    private JPanel buildStatCard(JLabel valueLabel, String title, String icon, Color bg) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(bg);
        card.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));

        JLabel lblIcon  = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(75, 85, 99));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(lblTitle);
        textPanel.add(valueLabel);

        card.add(lblIcon,   BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private JLabel createStatCard(String value, String title, Color bg, Color fg) {
        JLabel lbl = new JLabel(value);
        lbl.setForeground(fg);
        return lbl;
    }

    // ── Status Bar ────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(30, 58, 138));
        bar.setBorder(new EmptyBorder(4, 12, 4, 12));

        lblStatus = new JLabel("Ready");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.WHITE);

        JLabel lblApp = new JLabel("Student Management System v1.0 — VIT");
        lblApp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblApp.setForeground(new Color(148, 163, 255));

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(lblApp,    BorderLayout.EAST);
        return bar;
    }

    // ── Actions ───────────────────────────────────────────────────────────────
    private void onAddStudent() {
        StudentFormDialog dialog = new StudentFormDialog(this, "Add New Student", null);
        dialog.setVisible(true);
        Student s = dialog.getResultStudent();
        if (s != null) {
            boolean ok = controller.addStudent(s);
            if (!ok) showError("Failed to add student. Roll number may already exist.");
        }
    }

    private void onEditStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { showWarning("Please select a student to edit."); return; }
        int modelRow = studentTable.convertRowIndexToModel(row);
        Student existing = tableModel.getStudentAt(modelRow);

        StudentFormDialog dialog = new StudentFormDialog(this, "Edit Student Record", existing);
        dialog.setVisible(true);
        Student updated = dialog.getResultStudent();
        if (updated != null) {
            updated.setId(existing.getId());
            boolean ok = controller.updateStudent(updated);
            if (!ok) showError("Failed to update student.");
        }
    }

    private void onDeleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { showWarning("Please select a student to delete."); return; }
        int modelRow = studentTable.convertRowIndexToModel(row);
        Student s = tableModel.getStudentAt(modelRow);

        // Confirmation Dialog (Syllabus Unit 4: Dialog Boxes, Option Dialogs)
        int choice = JOptionPane.showOptionDialog(this,
            "Are you sure you want to delete:\n" + s.getName() + " (" + s.getRollNumber() + ")?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
            null, new String[]{"Delete", "Cancel"}, "Cancel");

        if (choice == 0) {
            boolean ok = controller.deleteStudent(s.getId());
            if (!ok) showError("Failed to delete student.");
        }
    }

    private void onSearch() {
        String kw = tfSearch.getText().trim();
        if (kw.isEmpty()) { refreshTable(); return; }
        List<Student> results = controller.searchStudents(kw);
        tableModel.setStudents(results);
        setStatus("Search: " + results.size() + " result(s) for '" + kw + "'");
    }

    private void onExportCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("students_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fc.getSelectedFile();
                java.io.PrintWriter pw = new java.io.PrintWriter(file);
                pw.println("ID,Name,Roll Number,Course,Branch,CGPA,Email,Phone,Age,Gender");
                for (Student s : controller.getAllStudents()) {
                    pw.printf("%d,%s,%s,%s,%s,%.2f,%s,%s,%d,%s%n",
                        s.getId(), s.getName(), s.getRollNumber(), s.getCourse(),
                        s.getBranch(), s.getCgpa(), s.getEmail(), s.getPhone(),
                        s.getAge(), s.getGender());
                }
                pw.close();
                JOptionPane.showMessageDialog(this, "Exported to: " + file.getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                showError("Export failed: " + ex.getMessage());
            }
        }
    }

    private void showAbout() {
        // Syllabus Unit 4: Creating Dialogs
        JOptionPane.showMessageDialog(this,
            "<html><center><b>Student Management System</b><br>v1.0<br><br>"
            + "Built with: Java Swing + JDBC + JavaBeans + MVC<br><br>"
            + "Covers Syllabus Units 2, 3, 4<br>"
            + "<i>VIT University — Java Programming Course</i></center></html>",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onExit() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            DatabaseConnection.closeConnection();
            System.exit(0);
        }
    }

    // ── Helper: refresh table from DB ─────────────────────────────────────────
    private void refreshTable() {
        List<Student> all = controller.getAllStudents();
        tableModel.setStudents(all);
        setStatus("Loaded " + all.size() + " student record(s).");
    }

    private void updateStats() {
        lblTotal.setText(String.valueOf(controller.getTotalCount()));
        lblAvgCGPA.setText(String.format("%.2f", controller.getAverageCGPA()));
    }

    private void setStatus(String msg) { lblStatus.setText(msg); }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    // ── StudentEventListener (Unit 2 — Custom Events) ─────────────────────────
    @Override
    public void studentChanged(StudentEvent event) {
        refreshTable();
        updateStats();
        switch (event.getEventType()) {
            case ADDED   -> setStatus("✅ Student added: " + event.getStudent().getName());
            case UPDATED -> setStatus("✏ Student updated: " + event.getStudent().getName());
            case DELETED -> setStatus("🗑 Student deleted: " + event.getStudent().getName());
            default      -> setStatus("Ready");
        }
    }

    // ── Toolbar button factory ────────────────────────────────────────────────
    private JButton toolButton(String text, String tooltip, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setToolTipText(tooltip);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    // ── Menu helpers ──────────────────────────────────────────────────────────
    private JMenu createMenu(String title, int mnemonic) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menu.setForeground(Color.WHITE);
        menu.setOpaque(false);
        return menu;
    }

    private JMenuItem createMenuItem(String title, int mnemonic, KeyStroke accelerator) {
        JMenuItem item = new JMenuItem(title);
        item.setMnemonic(mnemonic);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (accelerator != null) item.setAccelerator(accelerator);
        return item;
    }

    // ── Alternate row color renderer (Syllabus Unit 4: custom rendering) ─────
    private static class AlternateRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 8, 0, 8));
            if (!isSelected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                setForeground(new Color(31, 41, 55));
            }
            return this;
        }
    }
}
