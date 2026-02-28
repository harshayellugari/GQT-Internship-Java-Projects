package main;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Theme {
    public static final Color BG         = new Color(13, 17, 28);
    public static final Color SURFACE    = new Color(22, 27, 42);
    public static final Color SURFACE2   = new Color(30, 36, 55);
    public static final Color ACCENT     = new Color(99, 102, 241);   // indigo
    public static final Color ACCENT2    = new Color(139, 92, 246);   // violet
    public static final Color SUCCESS    = new Color(34, 197, 94);
    public static final Color DANGER     = new Color(239, 68, 68);
    public static final Color WARNING    = new Color(234, 179, 8);
    public static final Color TEXT       = new Color(226, 232, 240);
    public static final Color TEXT_SEC   = new Color(148, 163, 184);
    public static final Color TEXT_MUT   = new Color(71, 85, 105);
    public static final Color BORDER     = new Color(40, 47, 68);
    public static final Color GOLD       = new Color(251, 191, 36);

    public static final Font F_TITLE  = new Font("Georgia",   Font.BOLD,  26);
    public static final Font F_H2     = new Font("Georgia",   Font.BOLD,  18);
    public static final Font F_H3     = new Font("SansSerif", Font.BOLD,  15);
    public static final Font F_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font F_SMALL  = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font F_BOLD   = new Font("SansSerif", Font.BOLD,  13);
    public static final Font F_MONO   = new Font("Monospaced",Font.PLAIN, 12);

    public static JButton primaryBtn(String text) {
        return styledBtn(text, ACCENT, Color.WHITE);
    }

    public static JButton successBtn(String text) {
        return styledBtn(text, SUCCESS, new Color(5, 30, 15));
    }

    public static JButton dangerBtn(String text) {
        return styledBtn(text, DANGER, Color.WHITE);
    }

    public static JButton secondaryBtn(String text) {
        return styledBtn(text, SURFACE2, TEXT);
    }

    private static JButton styledBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(F_BOLD);
        b.setForeground(fg);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        return b;
    }

    public static JTextField field(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(F_BODY);
        f.setForeground(TEXT);
        f.setBackground(SURFACE2);
        f.setCaretColor(ACCENT);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(7, 12, 7, 12)));
        return f;
    }

    public static JPasswordField passField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setFont(F_BODY);
        f.setForeground(TEXT);
        f.setBackground(SURFACE2);
        f.setCaretColor(ACCENT);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(7, 12, 7, 12)));
        return f;
    }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_BODY);
        l.setForeground(TEXT_SEC);
        return l;
    }

    public static void styleTable(JTable table) {
        table.setBackground(SURFACE);
        table.setForeground(TEXT);
        table.setFont(F_BODY);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(99, 102, 241, 80));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(SURFACE2);
        header.setForeground(TEXT_SEC);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBackground(r % 2 == 0 ? SURFACE : new Color(26, 32, 50));
                if (sel) setBackground(new Color(99, 102, 241, 80));
                setBorder(new EmptyBorder(0, 14, 0, 14));
                return this;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    public static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(new LineBorder(BORDER, 1, true));
        sp.getVerticalScrollBar().setBackground(SURFACE2);
        return sp;
    }

    public static JPanel card(LayoutManager lm) {
        JPanel p = new JPanel(lm) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static JPanel bgPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG);
        return p;
    }

    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_H2);
        l.setForeground(TEXT);
        return l;
    }

    public static JLabel badge(String text, Color bg) {
        JLabel l = new JLabel("  " + text + "  ") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setOpaque(false);
        l.setFont(F_SMALL);
        l.setForeground(Color.WHITE);
        return l;
    }
}
