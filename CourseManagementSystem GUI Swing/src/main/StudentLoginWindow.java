package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StudentLoginWindow extends JFrame {

    private CourseManager cm;
    private JTextField    userField;
    private JPasswordField passField;

    public StudentLoginWindow(CourseManager cm) {
        this.cm = cm;
        setTitle("Student Login — GQT");
        setSize(440, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = Theme.bgPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel card = Theme.card(null);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 36, 32, 36));
        card.setPreferredSize(new Dimension(340, 320));

        JLabel title = new JLabel("Student Login", SwingConstants.CENTER);
        title.setFont(Theme.F_H2);
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(8, 0, 20, 0));

        userField = Theme.field(20);
        passField = Theme.passField(20);

        JButton loginBtn = Theme.successBtn("Login →");
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.addActionListener(e -> doLogin());

        JLabel hint = new JLabel("Demo: alice / alice123", SwingConstants.CENTER);
        hint.setFont(Theme.F_SMALL);
        hint.setForeground(Theme.TEXT_MUT);
        hint.setAlignmentX(CENTER_ALIGNMENT);
        hint.setBorder(new EmptyBorder(10, 0, 0, 0));

        card.add(title);
        card.add(formRow("Username", userField));
        card.add(Box.createVerticalStrut(10));
        card.add(formRow("Password", passField));
        card.add(Box.createVerticalStrut(18));
        card.add(loginBtn);
        card.add(hint);

        root.add(card);
        setContentPane(root);
    }

    private JPanel formRow(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel l = Theme.label(label);
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void doLogin() {
        String user = userField.getText().trim();
        String pass = new String(passField.getPassword()).trim();
        Student s = cm.authenticate(user, pass);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
            new StudentDashboard(cm, s);
        }
    }
}
