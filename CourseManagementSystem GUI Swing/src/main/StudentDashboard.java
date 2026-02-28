package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboard extends JFrame {

    private CourseManager cm;
    private Student       student;
    private JPanel        contentArea;

    public StudentDashboard(CourseManager cm, Student student) {
        this.cm      = cm;
        this.student = student;
        setTitle("Student Dashboard — " + student.getName());
        setSize(1000, 660);
        setMinimumSize(new Dimension(850, 550));
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
        showPanel("all");
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────

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

        JLabel name = new JLabel(student.getName());
        name.setFont(Theme.F_BOLD);
        name.setForeground(Theme.TEXT);

        JLabel roleLabel = Theme.badge("Student", Theme.SUCCESS);
        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        roleRow.setOpaque(false);
        roleRow.add(roleLabel);

        top.add(brand, BorderLayout.NORTH);
        top.add(name,  BorderLayout.CENTER);
        top.add(roleRow, BorderLayout.SOUTH);
        panel.add(top);

        panel.add(navItem("All Courses",  "all"));
        panel.add(navItem("My Courses",   "mine"));
        panel.add(navItem("Apply Course", "apply"));
        panel.add(navItem("Pay Fees",     "pay"));

        panel.add(Box.createVerticalGlue());

        JButton logout = Theme.dangerBtn("Logout");
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

    // ── Always rebuild fresh on switch ────────────────────────────────────────

    private void showPanel(String key) {
        contentArea.removeAll();
        switch (key) {
            case "all":   contentArea.add(buildAllCoursesPanel(), BorderLayout.CENTER); break;
            case "mine":  contentArea.add(buildMyCoursesPanel(),  BorderLayout.CENTER); break;
            case "apply": contentArea.add(buildApplyPanel(),      BorderLayout.CENTER); break;
            case "pay":   contentArea.add(buildPayPanel(),        BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    // ── All Courses ───────────────────────────────────────────────────────────

    private JPanel buildAllCoursesPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("All Available Courses");
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
        return panel;
    }

    // ── My Courses ────────────────────────────────────────────────────────────

    private JPanel buildMyCoursesPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("My Registered Courses");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        List<Course> mine = student.getRegisteredCourses();
        if (mine.isEmpty()) {
            JLabel empty = new JLabel("You have not applied to any courses yet.", SwingConstants.CENTER);
            empty.setFont(Theme.F_BODY);
            empty.setForeground(Theme.TEXT_MUT);
            panel.add(empty, BorderLayout.CENTER);
            return panel;
        }

        String[] cols = {"Course Name", "Fee (Rs.)", "Status"};
        Object[][] data = new Object[mine.size()][3];
        for (int i = 0; i < mine.size(); i++) {
            Course c = mine.get(i);
            data[i][0] = c.getName();
            data[i][1] = String.format("%.0f", c.getFee());
            data[i][2] = student.hasPaid(c) ? "Paid" : "Pending";
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        panel.add(Theme.scrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // ── Apply ─────────────────────────────────────────────────────────────────

    private JPanel buildApplyPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("Apply for a Course");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        List<Course> all = cm.getCourses();
        String[] cols = {"#", "Course Name", "Fee (Rs.)", "Status"};
        Object[][] data = new Object[all.size()][4];
        for (int i = 0; i < all.size(); i++) {
            Course c = all.get(i);
            data[i][0] = i + 1;
            data[i][1] = c.getName();
            data[i][2] = String.format("%.0f", c.getFee());
            data[i][3] = student.isRegistered(c) ? "Applied" : "Available";
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        panel.add(Theme.scrollPane(table), BorderLayout.CENTER);

        JButton applyBtn = Theme.primaryBtn("Apply for Selected Course");
        applyBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course from the list.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Course c = all.get(row);
            if (student.isRegistered(c)) {
                JOptionPane.showMessageDialog(this, "You are already registered for \"" + c.getName() + "\".", "Already Applied", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                "Apply for \"" + c.getName() + "\"  (Fee: Rs. " + String.format("%.0f", c.getFee()) + ")?",
                "Confirm Application", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                student.register(c);
                JOptionPane.showMessageDialog(this,
                    "Successfully applied for \"" + c.getName() + "\".\nPlease pay the fee to confirm your seat.",
                    "Applied!", JOptionPane.INFORMATION_MESSAGE);
                showPanel("apply");
            }
        });

        JPanel bottom = Theme.bgPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBorder(new EmptyBorder(14, 0, 0, 0));
        bottom.add(applyBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // ── Pay ───────────────────────────────────────────────────────────────────

    private JPanel buildPayPanel() {
        JPanel panel = Theme.bgPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel heading = Theme.sectionTitle("Pay Course Fees");
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(heading, BorderLayout.NORTH);

        List<Course> unpaid = student.getRegisteredCourses().stream()
            .filter(c -> !student.hasPaid(c))
            .collect(Collectors.toList());

        if (unpaid.isEmpty()) {
            JLabel msg = new JLabel(
                student.getRegisteredCourses().isEmpty()
                    ? "You have not applied to any courses yet."
                    : "All your courses are paid. Nothing pending.",
                SwingConstants.CENTER);
            msg.setFont(Theme.F_H3);
            msg.setForeground(student.getRegisteredCourses().isEmpty() ? Theme.TEXT_MUT : Theme.SUCCESS);
            panel.add(msg, BorderLayout.CENTER);
            return panel;
        }

        String[] cols = {"Course Name", "Fee (Rs.)"};
        Object[][] data = new Object[unpaid.size()][2];
        for (int i = 0; i < unpaid.size(); i++) {
            data[i][0] = unpaid.get(i).getName();
            data[i][1] = String.format("%.0f", unpaid.get(i).getFee());
        }
        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        panel.add(Theme.scrollPane(table), BorderLayout.CENTER);

        JButton payBtn = Theme.successBtn("Pay Selected Course");
        payBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course to pay.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Course c = unpaid.get(row);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm payment of Rs. " + String.format("%.0f", c.getFee()) + " for \"" + c.getName() + "\"?",
                "Confirm Payment", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                student.pay(c);
                JOptionPane.showMessageDialog(this,
                    "Payment successful!\nCourse: " + c.getName() + "\nAmount: Rs. " + String.format("%.0f", c.getFee()),
                    "Payment Confirmed", JOptionPane.INFORMATION_MESSAGE);
                showPanel("pay");
            }
        });

        JPanel bottom = Theme.bgPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBorder(new EmptyBorder(14, 0, 0, 0));
        bottom.add(payBtn);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }
}
