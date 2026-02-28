package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class AdminWindow extends JFrame {

    private static final String ADMIN_PASS = "admin123";
    private CourseManager cm;
    private JPanel        contentArea;

    public AdminWindow(CourseManager cm) {
        this.cm = cm;
        setTitle("Admin Panel — GQT");
        setSize(1020, 660);
        setMinimumSize(new Dimension(860, 540));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        promptLogin();
    }

    private void promptLogin() {
        JPasswordField pass = new JPasswordField(16);
        pass.setFont(Theme.F_BODY);
        Object[] msg = {"Admin Password:", pass};
        int result = JOptionPane.showConfirmDialog(null, msg, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        if (!new String(pass.getPassword()).equals(ADMIN_PASS)) {
            JOptionPane.showMessageDialog(null, "Incorrect password.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = Theme.bgPanel(new BorderLayout());
        contentArea = Theme.bgPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(contentArea,   BorderLayout.CENTER);
        setContentPane(root);
        showPanel("courses");
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.SURFACE);
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));

        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(22, 20, 20, 20));
        JLabel brand = new JLabel("GQT");
        brand.setFont(new Font("Georgia", Font.BOLD, 18));
        brand.setForeground(Theme.ACCENT);
        JLabel roleTag = Theme.badge("Admin", Theme.GOLD);
        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        roleRow.setOpaque(false);
        roleRow.add(roleTag);
        top.add(brand, BorderLayout.NORTH);
        top.add(roleRow, BorderLayout.SOUTH);
        panel.add(top);

        panel.add(navItem("Manage Courses", "courses"));
        panel.add(navItem("View Students",  "students"));

        panel.add(Box.createVerticalGlue());

        JButton logout = Theme.dangerBtn("Back to Main");
        logout.setAlignmentX(LEFT_ALIGNMENT);
        logout.setBorder(new EmptyBorder(10, 20, 14, 20));
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        logout.addActionListener(e -> { dispose(); new MainWindow(); });
        panel.add(logout);
        return panel;
    }

    private JButton navItem(String label, String key) {
        JButton b = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                if (getModel().isRollover()) {
                    g.setColor(new Color(99, 102, 241, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
            }
        };
        b.setFont(Theme.F_BODY);
        b.setForeground(Theme.TEXT_SEC);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setBorder(new EmptyBorder(10, 24, 10, 24));
        b.addActionListener(e -> showPanel(key));
        return b;
    }

    private void showPanel(String key) {
        contentArea.removeAll();
        switch (key) {
            case "courses":  contentArea.add(buildCoursesPanel(),  BorderLayout.CENTER); break;
            case "students": contentArea.add(buildStudentsPanel(), BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Courses Panel ─────────────────────────────────────────────────────────

    private JPanel buildCoursesPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("Manage Courses");
        heading.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(heading, BorderLayout.NORTH);

        // Add form
        JPanel addForm = Theme.card(new FlowLayout(FlowLayout.LEFT, 12, 10));
        addForm.setBorder(new EmptyBorder(10, 14, 10, 14));

        JTextField nameField = Theme.field(22);
        JTextField feeField  = Theme.field(10);

        addForm.add(Theme.label("Course Name:"));
        addForm.add(nameField);
        addForm.add(Theme.label("Fee:"));
        addForm.add(feeField);

        JButton addBtn = Theme.successBtn("Add Course");
        addBtn.addActionListener(e -> {
            String name   = nameField.getText().trim();
            String feeStr = feeField.getText().trim();
            if (name.isEmpty() || feeStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill in both fields.", "Incomplete", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double fee;
            try { fee = Double.parseDouble(feeStr); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Fee must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cm.addCourse(name, fee);
            JOptionPane.showMessageDialog(this, "Course \"" + name + "\" added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showPanel("courses");  // refresh table immediately
        });
        addForm.add(addBtn);

        // Course table — read fresh from cm every time
        List<Course> all = cm.getCourses();
        String[] cols = {"#", "Course Name", "Fee (Rs.)"};
        Object[][] data = new Object[all.size()][3];
        for (int i = 0; i < all.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = all.get(i).getName();
            data[i][2] = String.format("%.0f", all.get(i).getFee());
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);

        JButton removeBtn = Theme.dangerBtn("Remove Selected Course");
        removeBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Course c = all.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Remove course \"" + c.getName() + "\"? This cannot be undone.",
                "Confirm Remove", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                cm.removeCourse(c);
                showPanel("courses");  // refresh table immediately
            }
        });

        JPanel bottom = Theme.bgPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(removeBtn);

        JPanel center = Theme.bgPanel(new BorderLayout(0, 12));
        center.setBorder(new EmptyBorder(12, 0, 0, 0));
        center.add(addForm, BorderLayout.NORTH);
        center.add(Theme.scrollPane(table), BorderLayout.CENTER);
        center.add(bottom, BorderLayout.SOUTH);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    // ── Students Panel ────────────────────────────────────────────────────────

    private JPanel buildStudentsPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("All Registered Students");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        List<Student> students = cm.getStudents();
        String[] cols = {"Username", "Name", "Email", "Courses Enrolled", "Courses Paid"};
        Object[][] data = new Object[students.size()][5];
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            data[i][0] = s.getUsername();
            data[i][1] = s.getName();
            data[i][2] = s.getEmail();
            data[i][3] = s.getRegisteredCourses().size();
            data[i][4] = s.getPaidCourses().size();
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);

        JLabel count = Theme.label("Total students: " + students.size());
        count.setBorder(new EmptyBorder(12, 0, 0, 0));

        panel.add(Theme.scrollPane(table), BorderLayout.CENTER);
        panel.add(count, BorderLayout.SOUTH);
        return panel;
    }
}
