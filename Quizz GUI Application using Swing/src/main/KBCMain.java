package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

public class KBCMain extends JFrame {

    // Colors - KBC themed
    private static final Color BG_DARK       = new Color(0, 0, 40);
    private static final Color BG_MID        = new Color(0, 10, 80);
    private static final Color GOLD          = new Color(255, 200, 0);
    private static final Color GOLD_DARK     = new Color(180, 130, 0);
    private static final Color OPTION_BLUE   = new Color(10, 30, 120);
    private static final Color OPTION_HOVER  = new Color(20, 60, 180);
    private static final Color OPTION_BORDER = new Color(100, 150, 255);
    private static final Color CORRECT_COLOR = new Color(0, 180, 0);
    private static final Color WRONG_COLOR   = new Color(200, 0, 0);
    private static final Color LOCKED_COLOR  = new Color(200, 140, 0);
    private static final Color TEXT_WHITE    = new Color(255, 255, 255);
    private static final Color TEXT_GOLD     = new Color(255, 215, 0);
    private static final Color SAFE_ZONE     = new Color(255, 100, 0);

    // Game state
    private QuestionBank qb;
    private int currentQ = 0;
    private int currentPrize = 0;
    private boolean audiencePollUsed = false;
    private boolean fiftyFiftyUsed = false;

    // UI Components
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Game screen
    private JLabel questionLabel;
    private OptionButton[] optionButtons = new OptionButton[4];
    private JLabel prizeLabel;
    private JLabel questionNumberLabel;
    private JButton lifelineApBtn;
    private JButton lifelineFfBtn;
    private JButton quitBtn;
    private JPanel prizeListPanel;
    private JLabel[] prizeListLabels;
    private JLabel statusLabel;
    private Timer revealTimer;

    // Prize milestones
    private static final int[] SAFE_ZONES = {3, 7}; // indices (0-based) = Q4, Q8
    private static final String[] PRIZE_STRINGS = {
        "₹1,000", "₹2,000", "₹3,000", "₹5,000", "₹10,000",
        "₹20,000", "₹40,000", "₹80,000", "₹1,60,000", "₹3,20,000",
        "₹6,40,000", "₹12,50,000", "₹25,00,000", "₹50,00,000", "₹75,00,000",
        "₹1,00,00,000"
    };

    public KBCMain() {
        setTitle("Kaun Banega Crorepati - Attack on Titan Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG_DARK);

        mainPanel.add(buildWelcomeScreen(), "WELCOME");
        mainPanel.add(buildGameScreen(), "GAME");
        mainPanel.add(buildResultScreen("", ""), "RESULT");

        add(mainPanel);
        cardLayout.show(mainPanel, "WELCOME");
        setVisible(true);
    }

    // ─── WELCOME SCREEN ──────────────────────────────────────────────────────────

    private JPanel buildWelcomeScreen() {
        JPanel panel = new GradientPanel(BG_DARK, BG_MID);
        panel.setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("KAUN BANEGA CROREPATI", SwingConstants.CENTER);
        title.setFont(new Font("Georgia", Font.BOLD, 36));
        title.setForeground(GOLD);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);



        JSeparator sep = new JSeparator();
        sep.setForeground(GOLD_DARK);
        sep.setMaximumSize(new Dimension(400, 2));

        JLabel info1 = new JLabel("16 Questions • 2 Lifelines • ₹1 Crore", SwingConstants.CENTER);
        info1.setFont(new Font("Arial", Font.PLAIN, 16));
        info1.setForeground(TEXT_WHITE);
        info1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel info2 = new JLabel("Safe zones at ₹5,000 and ₹1,60,000", SwingConstants.CENTER);
        info2.setFont(new Font("Arial", Font.PLAIN, 14));
        info2.setForeground(new Color(180, 180, 180));
        info2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startBtn = createGoldButton("START GAME");
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setMaximumSize(new Dimension(260, 55));
        startBtn.addActionListener(e -> startGame());

        JButton exitBtn = createOutlineButton("Exit");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setMaximumSize(new Dimension(140, 40));
        exitBtn.addActionListener(e -> System.exit(0));

        box.add(Box.createVerticalStrut(10));
        box.add(title);
        box.add(Box.createVerticalStrut(24));
        box.add(sep);
        box.add(Box.createVerticalStrut(24));
        box.add(info1);
        box.add(Box.createVerticalStrut(8));
        box.add(info2);
        box.add(Box.createVerticalStrut(36));
        box.add(startBtn);
        box.add(Box.createVerticalStrut(14));
        box.add(exitBtn);

        // Wrap box in rounded panel
        RoundedPanel wrapper = new RoundedPanel(20, new Color(255,255,255,20));
        wrapper.setLayout(new BorderLayout());
        wrapper.add(box, BorderLayout.CENTER);
        wrapper.setPreferredSize(new Dimension(500, 420));

        panel.add(wrapper);
        return panel;
    }

    // ─── GAME SCREEN ─────────────────────────────────────────────────────────────

    private JPanel buildGameScreen() {
        JPanel panel = new GradientPanel(BG_DARK, BG_MID);
        panel.setLayout(new BorderLayout(0, 0));

        // TOP BAR
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(10, 20, 0, 20));

        questionNumberLabel = new JLabel("Question 1 of 16", SwingConstants.LEFT);
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionNumberLabel.setForeground(new Color(180, 180, 220));

        prizeLabel = new JLabel("Prize: ₹0", SwingConstants.RIGHT);
        prizeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        prizeLabel.setForeground(GOLD);

        topBar.add(questionNumberLabel, BorderLayout.WEST);
        topBar.add(prizeLabel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // CENTER: question + options
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 20, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Question box
        RoundedPanel qBox = new RoundedPanel(16, new Color(5, 20, 100, 200));
        qBox.setLayout(new BorderLayout());
        qBox.setBorder(new EmptyBorder(20, 24, 20, 24));
        qBox.setBorderColor(OPTION_BORDER);

        questionLabel = new JLabel("<html><div style='text-align:center;'>Question</div></html>", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setForeground(TEXT_WHITE);
        qBox.add(questionLabel, BorderLayout.CENTER);
        centerPanel.add(qBox, gbc);

        // Status label (feedback)
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 8, 0);
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setForeground(GOLD);
        centerPanel.add(statusLabel, gbc);

        // Options (2x2 grid)
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 0, 0);
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 14, 14));
        optionsPanel.setOpaque(false);
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            optionButtons[i] = createOptionButton(labels[i], "");
            optionButtons[i].addActionListener(e -> handleAnswer(idx));
            optionsPanel.add(optionButtons[i]);
        }
        centerPanel.add(optionsPanel, gbc);

        // LIFELINES + QUIT row
        gbc.gridy = 3; gbc.insets = new Insets(16, 0, 0, 0);
        JPanel lifelineRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        lifelineRow.setOpaque(false);

        lifelineApBtn = createLifelineButton("Audience Poll");
        lifelineApBtn.addActionListener(e -> useAudiencePoll());
        lifelineFfBtn = createLifelineButton("50:50");
        lifelineFfBtn.addActionListener(e -> useFiftyFifty());
        quitBtn = createOutlineButton("Quit & Take Money");
        quitBtn.addActionListener(e -> quitGame());

        lifelineRow.add(lifelineApBtn);
        lifelineRow.add(lifelineFfBtn);
        lifelineRow.add(Box.createHorizontalStrut(20));
        lifelineRow.add(quitBtn);
        centerPanel.add(lifelineRow, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        // RIGHT: prize ladder
        JPanel rightPanel = buildPrizeLadder();
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildPrizeLadder() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(10, 0, 10, 16));
        outer.setPreferredSize(new Dimension(170, 0));

        JLabel ladderTitle = new JLabel("Prize Ladder", SwingConstants.CENTER);
        ladderTitle.setFont(new Font("Arial", Font.BOLD, 13));
        ladderTitle.setForeground(GOLD);
        ladderTitle.setBorder(new EmptyBorder(0, 0, 6, 0));

        prizeListPanel = new JPanel();
        prizeListPanel.setLayout(new BoxLayout(prizeListPanel, BoxLayout.Y_AXIS));
        prizeListPanel.setOpaque(false);
        prizeListLabels = new JLabel[16];

        // Build from top (Q16 at top)
        for (int i = 15; i >= 0; i--) {
            JLabel lbl = new JLabel("  Q" + (i + 1) + "  " + PRIZE_STRINGS[i], SwingConstants.LEFT);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
            lbl.setOpaque(true);
            lbl.setBorder(new EmptyBorder(2, 6, 2, 6));

            boolean isSafe = (i == 3 || i == 7);
            if (isSafe) {
                lbl.setForeground(SAFE_ZONE);
                lbl.setBackground(new Color(60, 20, 0));
            } else {
                lbl.setForeground(new Color(180, 180, 220));
                lbl.setBackground(new Color(0, 5, 40));
            }
            lbl.setMaximumSize(new Dimension(200, 22));
            prizeListLabels[i] = lbl;
            prizeListPanel.add(lbl);
        }

        JScrollPane scroll = new JScrollPane(prizeListPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        outer.add(ladderTitle, BorderLayout.NORTH);
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    // ─── RESULT SCREEN ───────────────────────────────────────────────────────────

    private JPanel buildResultScreen(String heading, String subtext) {
        JPanel panel = new GradientPanel(BG_DARK, BG_MID);
        panel.setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setOpaque(false);
        box.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel h = new JLabel(heading.isEmpty() ? " " : heading, SwingConstants.CENTER);
        h.setName("heading");
        h.setFont(new Font("Georgia", Font.BOLD, 32));
        h.setForeground(GOLD);
        h.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel s = new JLabel(subtext.isEmpty() ? " " : subtext, SwingConstants.CENTER);
        s.setName("subtext");
        s.setFont(new Font("Arial", Font.PLAIN, 20));
        s.setForeground(TEXT_WHITE);
        s.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playAgain = createGoldButton("Play Again");
        playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgain.setMaximumSize(new Dimension(220, 50));
        playAgain.addActionListener(e -> {
            startGame();
            cardLayout.show(mainPanel, "GAME");
        });

        JButton menuBtn = createOutlineButton("Main Menu");
        menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuBtn.setMaximumSize(new Dimension(160, 40));
        menuBtn.addActionListener(e -> cardLayout.show(mainPanel, "WELCOME"));

        box.add(h);
        box.add(Box.createVerticalStrut(16));
        box.add(s);
        box.add(Box.createVerticalStrut(40));
        box.add(playAgain);
        box.add(Box.createVerticalStrut(12));
        box.add(menuBtn);

        RoundedPanel wrapper = new RoundedPanel(20, new Color(255,255,255,20));
        wrapper.setLayout(new BorderLayout());
        wrapper.add(box, BorderLayout.CENTER);
        wrapper.setPreferredSize(new Dimension(500, 340));

        panel.add(wrapper);
        return panel;
    }

    private void showResult(String heading, String subtext) {
        // Rebuild result panel with new text
        mainPanel.remove(mainPanel.getComponentCount() - 1);
        mainPanel.add(buildResultScreen(heading, subtext), "RESULT");
        cardLayout.show(mainPanel, "RESULT");
    }

    // ─── GAME LOGIC ──────────────────────────────────────────────────────────────

    private void startGame() {
        qb = new QuestionBank();
        currentQ = 0;
        currentPrize = 0;
        audiencePollUsed = false;
        fiftyFiftyUsed = false;

        // Reset lifeline buttons
        lifelineApBtn.setEnabled(true);
        lifelineApBtn.setBackground(OPTION_BLUE);
        lifelineFfBtn.setEnabled(true);
        lifelineFfBtn.setBackground(OPTION_BLUE);

        // Reset prize ladder
        for (int i = 0; i < 16; i++) {
            boolean isSafe = (i == 3 || i == 7);
            prizeListLabels[i].setForeground(isSafe ? SAFE_ZONE : new Color(180, 180, 220));
            prizeListLabels[i].setBackground(isSafe ? new Color(60, 20, 0) : new Color(0, 5, 40));
            prizeListLabels[i].setFont(new Font("Monospaced", Font.PLAIN, 11));
        }

        cardLayout.show(mainPanel, "GAME");
        loadQuestion();
    }

    private void loadQuestion() {
        Question q = qb.qns[currentQ];
        questionNumberLabel.setText("Question " + (currentQ + 1) + " of 16");
        prizeLabel.setText("Prize: ₹" + formatPrize(currentPrize));
        statusLabel.setText(" ");

        // Update question text
        questionLabel.setText("<html><div style='text-align:center;'>" + q.qn + "</div></html>");

        // Update options
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            String opt = q.options[i];
            if (opt == null || opt.isEmpty()) {
                optionButtons[i].setText("");
                optionButtons[i].setEnabled(false);
                optionButtons[i].setBackground(new Color(10, 10, 50));
            } else {
                optionButtons[i].setText("<html><b>" + labels[i] + ".</b>  " + opt + "</html>");
                optionButtons[i].setEnabled(true);
                optionButtons[i].setBackground(OPTION_BLUE);
                optionButtons[i].setBorderColor(OPTION_BORDER);
            }
        }

        // Highlight current on ladder
        for (int i = 0; i < 16; i++) {
            boolean isSafe = (i == 3 || i == 7);
            if (i == currentQ) {
                prizeListLabels[i].setForeground(Color.BLACK);
                prizeListLabels[i].setBackground(GOLD);
                prizeListLabels[i].setFont(new Font("Monospaced", Font.BOLD, 11));
            } else if (i < currentQ) {
                prizeListLabels[i].setForeground(new Color(80, 80, 100));
                prizeListLabels[i].setBackground(new Color(0, 5, 40));
            } else {
                prizeListLabels[i].setForeground(isSafe ? SAFE_ZONE : new Color(180, 180, 220));
                prizeListLabels[i].setBackground(isSafe ? new Color(60, 20, 0) : new Color(0, 5, 40));
                prizeListLabels[i].setFont(new Font("Monospaced", Font.PLAIN, 11));
            }
        }
    }

    private void handleAnswer(int idx) {
        disableAllOptions();
        Question q = qb.qns[currentQ];
        char selected = (char)('a' + idx);

        // Highlight selected
        optionButtons[idx].setBackground(LOCKED_COLOR);
        optionButtons[idx].setBorderColor(LOCKED_COLOR.darker());

        // Delay reveal
        revealTimer = new Timer(1200, e -> {
            revealTimer.stop();
            revealAnswer(idx, selected, q);
        });
        revealTimer.start();
    }

    private void revealAnswer(int selectedIdx, char selected, Question q) {
        int correctIdx = q.answer - 'a';

        // Color correct green
        optionButtons[correctIdx].setBackground(CORRECT_COLOR);
        optionButtons[correctIdx].setBorderColor(CORRECT_COLOR.darker());

        if (selected == q.answer) {
            statusLabel.setText("✅ Correct!");
            statusLabel.setForeground(CORRECT_COLOR);
            currentPrize = q.prize;
            prizeLabel.setText("Prize: ₹" + formatPrize(currentPrize));

            // Mark ladder
            prizeListLabels[currentQ].setForeground(Color.BLACK);
            prizeListLabels[currentQ].setBackground(CORRECT_COLOR);

            Timer nextTimer = new Timer(1500, e2 -> {
                currentQ++;
                if (currentQ >= 16) {
                    showResult("🎉 Congratulations!", "You won ₹1,00,00,000! You are a Crorepati!");
                } else {
                    loadQuestion();
                }
            });
            nextTimer.setRepeats(false);
            nextTimer.start();
        } else {
            optionButtons[selectedIdx].setBackground(WRONG_COLOR);
            optionButtons[selectedIdx].setBorderColor(WRONG_COLOR.darker());
            statusLabel.setText("❌ Wrong Answer!");
            statusLabel.setForeground(WRONG_COLOR);

            int won = getSafeZonePrize();
            Timer gameOverTimer = new Timer(2000, e2 -> {
                String prize = won > 0 ? "You take home ₹" + formatPrize(won) : "You go home empty-handed.";
                showResult("Game Over!", "Wrong answer. " + prize);
            });
            gameOverTimer.setRepeats(false);
            gameOverTimer.start();
        }
    }

    private void useAudiencePoll() {
        if (audiencePollUsed) return;
        audiencePollUsed = true;
        lifelineApBtn.setEnabled(false);
        lifelineApBtn.setBackground(new Color(40, 40, 60));

        Question q = qb.qns[currentQ];
        int ans = q.answer - 'a';
        String[] pcts = new String[4];
        pcts[ans] = "65%";
        String[] others = {"9%", "11%", "15%"};
        int j = 0;
        for (int i = 0; i < 4; i++) {
            if (i != ans) pcts[i] = others[j++];
        }

        StringBuilder msg = new StringBuilder("<html><b>Audience Poll Results:</b><br><br>");
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            if (qb.qns[currentQ].options[i] != null && !qb.qns[currentQ].options[i].isEmpty()) {
                msg.append(labels[i]).append(": ").append(pcts[i]).append("<br>");
            }
        }
        msg.append("</html>");

        JOptionPane.showMessageDialog(this, msg.toString(), "Audience Poll", JOptionPane.INFORMATION_MESSAGE);
    }

    private void useFiftyFifty() {
        if (fiftyFiftyUsed) return;
        fiftyFiftyUsed = true;
        lifelineFfBtn.setEnabled(false);
        lifelineFfBtn.setBackground(new Color(40, 40, 60));

        Question q = qb.qns[currentQ];
        int ans = q.answer - 'a';
        int cnt = 0;
        String[] labels = {"A", "B", "C", "D"};
        for (int i = 0; i < 4 && cnt < 2; i++) {
            if (i != ans && q.options[i] != null && !q.options[i].isEmpty()) {
                q.options[i] = "";
                cnt++;
            }
        }
        // Reload display
        for (int i = 0; i < 4; i++) {
            if (q.options[i] == null || q.options[i].isEmpty()) {
                optionButtons[i].setText("");
                optionButtons[i].setEnabled(false);
                optionButtons[i].setBackground(new Color(10, 10, 50));
            }
        }
    }

    private void quitGame() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit and take ₹" + formatPrize(currentPrize) + "?",
                "Quit Game", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            showResult("You Quit!", "You take home ₹" + formatPrize(currentPrize) + ". Well played!");
        }
    }

    private void disableAllOptions() {
        for (OptionButton b : optionButtons) b.setEnabled(false);
        lifelineApBtn.setEnabled(false);
        lifelineFfBtn.setEnabled(false);
        quitBtn.setEnabled(false);
    }

    private int getSafeZonePrize() {
        // Safe zones at index 3 (5000) and 7 (80000)
        if (currentQ > 7) return qb.qns[7].prize;
        if (currentQ > 3) return qb.qns[3].prize;
        return 0;
    }

    private String formatPrize(int prize) {
        return NumberFormat.getNumberInstance(new Locale("en", "IN")).format(prize);
    }

    // ─── BUTTON FACTORIES ────────────────────────────────────────────────────────

    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 220, 50), 0, getHeight(), new Color(200, 140, 0));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 240, 100, 80));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(new Color(30, 20, 0));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 50));
        return btn;
    }

    private JButton createOutlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(new Color(180, 180, 220));
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setContentAreaFilled(false);
        btn.setBorder(new LineBorder(new Color(100, 100, 160), 1, true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));
        return btn;
    }

    private JButton createLifelineButton(String text) {
        OptionButton btn = new OptionButton(text, OPTION_BLUE, OPTION_BORDER);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(GOLD);
        btn.setPreferredSize(new Dimension(150, 40));
        return btn;
    }

    private OptionButton createOptionButton(String label, String text) {
        OptionButton btn = new OptionButton(text, OPTION_BLUE, OPTION_BORDER);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(TEXT_WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setPreferredSize(new Dimension(300, 56));
        return btn;
    }

    // ─── CUSTOM COMPONENTS ───────────────────────────────────────────────────────

    static class OptionButton extends JButton {
        private Color bg;
        private Color borderColor;

        public OptionButton(String text, Color bg, Color borderColor) {
            super(text);
            this.bg = bg;
            this.borderColor = borderColor;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void setBorderColor(Color c) { this.borderColor = c; repaint(); }

        @Override
        public void setBackground(Color c) { this.bg = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private Color borderColor = new Color(100, 120, 200, 100);

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bgColor = bg;
            setOpaque(false);
        }

        public void setBorderColor(Color c) { this.borderColor = c; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class GradientPanel extends JPanel {
        private Color c1, c2;
        public GradientPanel(Color c1, Color c2) { this.c1 = c1; this.c2 = c2; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, c1, 0, getHeight(), c2));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─── MAIN ────────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(KBCMain::new);
    }
}
