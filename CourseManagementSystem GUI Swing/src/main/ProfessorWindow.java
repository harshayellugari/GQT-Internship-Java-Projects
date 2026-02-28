package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ProfessorWindow extends JFrame {

    private CourseManager cm;
    private JPanel        contentArea;
    private Professor     currentProf;

    private JTextField nameField, subjectField, emailField, expField;

    public ProfessorWindow(CourseManager cm) {
        this.cm = cm;
        setTitle("Professor Portal — GQT");
        setSize(980, 640);
        setMinimumSize(new Dimension(820, 520));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = Theme.bgPanel(new BorderLayout());
        contentArea = Theme.bgPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(contentArea,   BorderLayout.CENTER);
        setContentPane(root);
        showPanel("register");
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
        JLabel sub = new JLabel("Professor Portal");
        sub.setFont(Theme.F_SMALL);
        sub.setForeground(Theme.TEXT_SEC);
        top.add(brand, BorderLayout.NORTH);
        top.add(sub, BorderLayout.SOUTH);
        panel.add(top);

        panel.add(navItem("Register",    "register"));
        panel.add(navItem("All Courses", "courses"));

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
            case "register": contentArea.add(buildRegisterPanel(), BorderLayout.CENTER); break;
            case "courses":  contentArea.add(buildCoursesPanel(),  BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── Registration Panel ────────────────────────────────────────────────────

    private JPanel buildRegisterPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("Professor Registration");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        JPanel form = Theme.card(new GridBagLayout());
        form.setBorder(new EmptyBorder(24, 28, 24, 28));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 6, 7, 6);
        gbc.weightx = 1;

        nameField    = Theme.field(24);
        subjectField = Theme.field(24);
        emailField   = Theme.field(24);
        expField     = Theme.field(24);

        addRow(form, gbc, 0, "Full Name",                nameField);
        addRow(form, gbc, 1, "Subject / Specialisation", subjectField);
        addRow(form, gbc, 2, "Email Address",            emailField);
        addRow(form, gbc, 3, "Years of Experience",      expField);

        JButton submitBtn = Theme.primaryBtn("Register Professor");
        submitBtn.addActionListener(e -> doRegister());
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 6, 0, 6);
        form.add(submitBtn, gbc);

        // Live professor table below form
        JPanel tableSection = Theme.bgPanel(new BorderLayout());
        tableSection.setBorder(new EmptyBorder(22, 0, 0, 0));
        JLabel tblHeading = Theme.sectionTitle("Registered Professors");
        tblHeading.setFont(Theme.F_H3);
        tblHeading.setBorder(new EmptyBorder(0, 0, 10, 0));
        tableSection.add(tblHeading, BorderLayout.NORTH);
        tableSection.add(buildProfTable(), BorderLayout.CENTER);

        JPanel center = Theme.bgPanel(new BorderLayout(0, 0));
        center.add(form, BorderLayout.NORTH);
        center.add(tableSection, BorderLayout.CENTER);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane buildProfTable() {
        List<Professor> profs = cm.getProfessors();
        String[] cols = {"Name", "Subject", "Email", "Experience (yrs)"};
        Object[][] data = new Object[profs.size()][4];
        for (int i = 0; i < profs.size(); i++) {
            Professor p = profs.get(i);
            data[i][0] = p.getName();
            data[i][1] = p.getSubject();
            data[i][2] = p.getEmail();
            data[i][3] = p.getExperience();
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        return Theme.scrollPane(table);
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row; gbc.gridx = 0; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel l = Theme.label(label);
        l.setPreferredSize(new Dimension(200, 30));
        form.add(l, gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        form.add(field, gbc);
    }

    private void doRegister() {
        String name    = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String email   = emailField.getText().trim();
        String expStr  = expField.getText().trim();

        if (name.isEmpty() || subject.isEmpty() || email.isEmpty() || expStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int exp;
        try { exp = Integer.parseInt(expStr); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Experience must be a number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Professor p = new Professor(name, subject, email, exp);
        cm.addProfessor(p);
        currentProf = p;

        JOptionPane.showMessageDialog(this,
            "Registration Successful!\nWelcome, Prof. " + name + ".\nYou can now browse and apply to teach courses.",
            "Registered", JOptionPane.INFORMATION_MESSAGE);

        // Refresh register panel to show updated professor table
        showPanel("register");
    }

    // ── Courses Panel ─────────────────────────────────────────────────────────

    private JPanel buildCoursesPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("Available Courses to Teach");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        List<Course> all = cm.getCourses();
        String[] cols = {"#", "Course Name", "Fee (Rs.)"};
        Object[][] data = new Object[all.size()][3];
        for (int i = 0; i < all.size(); i++) {
            Course c = all.get(i);
            data[i][0] = i + 1;
            data[i][1] = c.getName();
            data[i][2] = String.format("%.0f", c.getFee());
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        panel.add(Theme.scrollPane(table), BorderLayout.CENTER);

        JButton applyBtn = Theme.primaryBtn("Apply to Teach Selected Course");
        applyBtn.addActionListener(e -> {
            if (currentProf == null) {
                JOptionPane.showMessageDialog(this, "Please register first before applying.", "Not Registered", JOptionPane.WARNING_MESSAGE);
                showPanel("register");
                return;
            }
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Course c = all.get(row);
            currentProf.applyToTeach(c);
            JOptionPane.showMessageDialog(this,
                "Application submitted!\nYou applied to teach: " + c.getName(),
                "Applied to Teach", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel bottom = Theme.bgPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBorder(new EmptyBorder(14, 0, 0, 0));
        bottom.add(applyBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
}
