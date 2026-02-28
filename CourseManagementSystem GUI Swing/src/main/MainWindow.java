package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CourseManager cm;

    public MainWindow() {
        cm = new CourseManager();
        setTitle("Global Quest Technologies");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 560);
        setMinimumSize(new Dimension(700, 480));
        setLocationRelativeTo(null);
        setBackground(Theme.BG);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = Theme.bgPanel(new BorderLayout());

        // ── Left brand panel ──
        JPanel brand = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(67, 56, 202),
                        0, getHeight(), new Color(109, 40, 217));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        brand.setOpaque(false);
        brand.setPreferredSize(new Dimension(300, 0));
        brand.setLayout(new GridBagLayout());

        JPanel brandBox = new JPanel();
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));
        brandBox.setOpaque(false);
        brandBox.setBorder(new EmptyBorder(0, 30, 0, 30));

        JLabel title = new JLabel("<html><div style='text-align:center'>Global Quest<br>Technologies</div></html>", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(14, 0, 0, 0));

        JLabel tagline = new JLabel("<html><div style='text-align:center;'>Empowering minds,<br>shaping futures.</div></html>", SwingConstants.CENTER);
        tagline.setFont(new Font("Georgia", Font.ITALIC, 13));
        tagline.setForeground(new Color(196, 181, 253));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setBorder(new EmptyBorder(12, 0, 0, 0));

        brandBox.add(title);
        brandBox.add(tagline);
        brand.add(brandBox);

        // ── Right login panel ──
        JPanel right = Theme.bgPanel(new GridBagLayout());
        right.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);
        box.setMaximumSize(new Dimension(320, 500));

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(Theme.F_TITLE);
        welcome.setForeground(Theme.TEXT);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Select your role to continue");
        sub.setFont(Theme.F_BODY);
        sub.setForeground(Theme.TEXT_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setBorder(new EmptyBorder(6, 0, 36, 0));

        JButton studentBtn   = makeRoleBtn("Student Login",   new Color(34, 197, 94, 30),  Theme.SUCCESS);
        JButton professorBtn = makeRoleBtn("Professor Login", new Color(99, 102, 241, 30), Theme.ACCENT);
        JButton adminBtn     = makeRoleBtn("Admin Login",     new Color(234, 179, 8, 30),  Theme.GOLD);

        studentBtn.addActionListener(e  -> new StudentLoginWindow(cm));
        professorBtn.addActionListener(e -> new ProfessorWindow(cm));
        adminBtn.addActionListener(e    -> new AdminWindow(cm));

        JLabel footer = new JLabel("© 2025 Global Quest Technologies", SwingConstants.CENTER);
        footer.setFont(Theme.F_SMALL);
        footer.setForeground(Theme.TEXT_MUT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setBorder(new EmptyBorder(36, 0, 0, 0));

        box.add(welcome);
        box.add(sub);
        box.add(studentBtn);
        box.add(Box.createVerticalStrut(12));
        box.add(professorBtn);
        box.add(Box.createVerticalStrut(12));
        box.add(adminBtn);
        box.add(footer);

        right.add(box);

        root.add(brand, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JButton makeRoleBtn(String text, Color bg, Color accent) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setForeground(accent);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(320, 52));
        b.setPreferredSize(new Dimension(320, 52));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBorder(new EmptyBorder(12, 20, 12, 20));
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
