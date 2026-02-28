package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ErenShoppee extends JFrame {

    // ─── PALETTE ──────────────────────────────────────────────────────────────
    private static final Color BG           = new Color(15, 15, 20);
    private static final Color SURFACE      = new Color(24, 24, 32);
    private static final Color SURFACE2     = new Color(32, 32, 44);
    private static final Color ACCENT       = new Color(255, 80, 30);       // Orange-red brand
    private static final Color ACCENT_LIGHT = new Color(255, 120, 70);
    private static final Color ACCENT_DIM   = new Color(120, 40, 15);
    private static final Color TEXT_PRI     = new Color(240, 238, 235);
    private static final Color TEXT_SEC     = new Color(160, 155, 150);
    private static final Color TEXT_MUT     = new Color(90, 88, 85);
    private static final Color BORDER_COL   = new Color(45, 45, 60);
    private static final Color SUCCESS      = new Color(50, 200, 100);
    private static final Color TAG_BG       = new Color(255, 80, 30, 40);
    private static final Font  FONT_TITLE   = new Font("Georgia", Font.BOLD, 28);
    private static final Font  FONT_H2      = new Font("Georgia", Font.BOLD, 18);
    private static final Font  FONT_BODY    = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font  FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font  FONT_BOLD    = new Font("SansSerif", Font.BOLD, 14);
    private static final Font  FONT_PRICE   = new Font("Georgia", Font.BOLD, 16);

    // ─── DATA ────────────────────────────────────────────────────────────────
    private Category[] categories;
    private Cart cart = new Cart();

    // ─── NAVIGATION STATE ────────────────────────────────────────────────────
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel cartBadge;

    // ─── CATEGORY ICONS ──────────────────────────────────────────────────────
    private static final String[] CAT_ICONS = {"📱", "👗", "🏠", "📚", "⚽"};
    private static final String[] CAT_DESC  = {
        "Phones, Laptops & Gadgets",
        "Clothing, Footwear & Apparel",
        "Furniture, Decor & Appliances",
        "Novels, Textbooks & More",
        "Fitness & Outdoor Equipment"
    };

    public ErenShoppee() {
        setTitle("ErenShoppee");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setBackground(BG);

        categories = DataLoader.loadCategories();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG);

        mainPanel.add(buildHomeScreen(), "HOME");
        mainPanel.add(buildCartScreen(), "CART");
        mainPanel.add(buildCheckoutScreen(), "CHECKOUT");
        mainPanel.add(buildSuccessScreen(), "SUCCESS");

        add(mainPanel);
        cardLayout.show(mainPanel, "HOME");
        setVisible(true);
    }

    // ─── TOP NAV BAR ─────────────────────────────────────────────────────────

    private JPanel buildTopNav(String title, boolean showBack, Runnable backAction) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(SURFACE);
        nav.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COL),
            new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        if (showBack) {
            JButton back = flatButton("← Back");
            back.setFont(FONT_BODY);
            back.setForeground(ACCENT_LIGHT);
            back.addActionListener(e -> { if (backAction != null) backAction.run(); });
            left.add(back);
        }

        JLabel logo = new JLabel("EREN");
        logo.setFont(new Font("Georgia", Font.BOLD, 20));
        logo.setForeground(ACCENT);
        JLabel logoSub = new JLabel("SHOPPEE");
        logoSub.setFont(new Font("Georgia", Font.PLAIN, 16));
        logoSub.setForeground(TEXT_SEC);
        left.add(logo);
        left.add(logoSub);

        nav.add(left, BorderLayout.WEST);

        if (title != null && !title.isEmpty()) {
            JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
            titleLbl.setFont(FONT_H2);
            titleLbl.setForeground(TEXT_PRI);
            nav.add(titleLbl, BorderLayout.CENTER);
        }

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton cartBtn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_DIM);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        cartBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
        cartBtn.setContentAreaFilled(false);
        cartBtn.setBorderPainted(false);
        cartBtn.setFocusPainted(false);
        cartBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cartBtn.setPreferredSize(new Dimension(100, 34));

        JLabel cartIcon = new JLabel("🛒");
        cartIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cartBadge = new JLabel("0");
        cartBadge.setFont(FONT_BOLD);
        cartBadge.setForeground(TEXT_PRI);
        cartBtn.add(cartIcon);
        cartBtn.add(cartBadge);
        cartBtn.addActionListener(e -> showCart());

        right.add(cartBtn);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    // ─── HOME SCREEN ─────────────────────────────────────────────────────────

    private JPanel buildHomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.add(buildTopNav(null, false, null), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Hero
        JPanel hero = new JPanel(new BorderLayout());
        hero.setOpaque(false);
        hero.setBorder(new EmptyBorder(0, 0, 30, 0));
        JLabel heroTitle = new JLabel("Shop Everything You Need");
        heroTitle.setFont(FONT_TITLE);
        heroTitle.setForeground(TEXT_PRI);
        JLabel heroSub = new JLabel("Explore our curated collection across 5 categories");
        heroSub.setFont(FONT_BODY);
        heroSub.setForeground(TEXT_SEC);
        heroSub.setBorder(new EmptyBorder(6, 0, 0, 0));
        hero.add(heroTitle, BorderLayout.NORTH);
        hero.add(heroSub, BorderLayout.CENTER);
        content.add(hero, BorderLayout.NORTH);

        // Category grid
        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        for (int i = 0; i < categories.length; i++) {
            grid.add(buildCategoryCard(categories[i], i));
        }
        // Checkout card
        JPanel checkoutCard = buildActionCard("🧾", "View Cart & Checkout", "Review items and place order", ACCENT);
        checkoutCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showCart(); }
        });
        grid.add(checkoutCard);

        content.add(grid, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCategoryCard(Category cat, int idx) {
        JPanel card = new HoverPanel(SURFACE, SURFACE2);
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(20, 22, 20, 22)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel icon = new JLabel(CAT_ICONS[idx]);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        JLabel name = new JLabel(cat.name);
        name.setFont(FONT_H2);
        name.setForeground(TEXT_PRI);
        name.setBorder(new EmptyBorder(10, 0, 4, 0));
        JLabel desc = new JLabel(CAT_DESC[idx]);
        desc.setFont(FONT_SMALL);
        desc.setForeground(TEXT_SEC);
        JLabel arrow = new JLabel("Explore →");
        arrow.setFont(FONT_SMALL);
        arrow.setForeground(ACCENT_LIGHT);
        arrow.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(name);
        textPanel.add(desc);
        textPanel.add(arrow);

        card.add(icon, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showSubCategories(cat); }
        });
        return card;
    }

    private JPanel buildActionCard(String icon, String title, String sub, Color accentColor) {
        JPanel card = new HoverPanel(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20),
                                      new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
            new LineBorder(accentColor, 1, true),
            new EmptyBorder(20, 22, 20, 22)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 32));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(FONT_H2);
        titleLbl.setForeground(accentColor);
        titleLbl.setBorder(new EmptyBorder(10, 0, 4, 0));
        JLabel subLbl = new JLabel(sub);
        subLbl.setFont(FONT_SMALL);
        subLbl.setForeground(TEXT_SEC);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLbl);
        textPanel.add(subLbl);

        card.add(iconLbl, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    // ─── SUBCATEGORY SCREEN ───────────────────────────────────────────────────

    private void showSubCategories(Category category) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.add(buildTopNav(category.name, true, () -> cardLayout.show(mainPanel, "HOME")), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel heading = new JLabel("Select a Category");
        heading.setFont(FONT_TITLE);
        heading.setForeground(TEXT_PRI);
        heading.setBorder(new EmptyBorder(0, 0, 24, 0));
        content.add(heading, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);
        for (SubCategory sub : category.subCategories) {
            JPanel card = buildSubCategoryCard(sub, category);
            grid.add(card);
        }
        content.add(grid, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);

        String key = "SUBCAT_" + category.name;
        mainPanel.add(panel, key);
        cardLayout.show(mainPanel, key);
    }

    private JPanel buildSubCategoryCard(SubCategory sub, Category parent) {
        JPanel card = new HoverPanel(SURFACE, SURFACE2);
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(24, 26, 24, 26)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel name = new JLabel(sub.name);
        name.setFont(FONT_H2);
        name.setForeground(TEXT_PRI);

        JLabel count = new JLabel(sub.products.length + " Products");
        count.setFont(FONT_SMALL);
        count.setForeground(TEXT_SEC);
        count.setBorder(new EmptyBorder(6, 0, 0, 0));

        JLabel arrow = new JLabel("View all →");
        arrow.setFont(FONT_SMALL);
        arrow.setForeground(ACCENT_LIGHT);
        arrow.setBorder(new EmptyBorder(16, 0, 0, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(name);
        textPanel.add(count);
        textPanel.add(arrow);

        card.add(textPanel, BorderLayout.CENTER);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showProducts(sub, parent); }
        });
        return card;
    }

    // ─── PRODUCT LIST SCREEN ─────────────────────────────────────────────────

    private void showProducts(SubCategory sub, Category parent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.add(buildTopNav(sub.name, true, () -> showSubCategories(parent)), BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(buildProductGrid(sub));
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getVerticalScrollBar().setBackground(SURFACE);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.setBorder(new EmptyBorder(20, 30, 20, 30));
        wrapper.add(scroll, BorderLayout.CENTER);

        panel.add(wrapper, BorderLayout.CENTER);

        String key = "PRODUCTS_" + sub.name;
        mainPanel.add(panel, key);
        cardLayout.show(mainPanel, key);
    }

    private JPanel buildProductGrid(SubCategory sub) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel heading = new JLabel(sub.name);
        heading.setFont(FONT_TITLE);
        heading.setForeground(TEXT_PRI);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setBorder(new EmptyBorder(0, 0, 20, 0));
        container.add(heading);

        for (Product p : sub.products) {
            container.add(buildProductRow(p, sub));
            container.add(Box.createVerticalStrut(10));
        }
        return container;
    }

    private JPanel buildProductRow(Product p, SubCategory sub) {
        HoverPanel row = new HoverPanel(SURFACE, SURFACE2);
        row.setLayout(new BorderLayout(16, 0));
        row.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(16, 20, 16, 20)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel info = new JPanel(new BorderLayout(0, 4));
        info.setOpaque(false);

        JLabel name = new JLabel(p.name);
        name.setFont(FONT_BOLD);
        name.setForeground(TEXT_PRI);

        JLabel brand = new JLabel(p.brand + "  ·  In Stock: " + p.stock);
        brand.setFont(FONT_SMALL);
        brand.setForeground(TEXT_SEC);

        info.add(name, BorderLayout.NORTH);
        info.add(brand, BorderLayout.SOUTH);

        JLabel price = new JLabel(formatPrice(p.price));
        price.setFont(FONT_PRICE);
        price.setForeground(ACCENT_LIGHT);

        row.add(info, BorderLayout.CENTER);
        row.add(price, BorderLayout.EAST);

        row.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showProductDetail(p, sub); }
        });
        return row;
    }

    // ─── PRODUCT DETAIL SCREEN ───────────────────────────────────────────────

    private void showProductDetail(Product p, SubCategory sub) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.add(buildTopNav("Product Details", true, () -> showProducts(sub, findParent(sub))), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(40, 60, 40, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        // Product card
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(SURFACE);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(28, 32, 28, 32)
        ));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel nameLabel = new JLabel("<html><div style='width:500px'>" + p.name + "</div></html>");
        nameLabel.setFont(FONT_TITLE);
        nameLabel.setForeground(TEXT_PRI);
        JLabel brandBadge = new JLabel("  " + p.brand + "  ");
        brandBadge.setFont(FONT_SMALL);
        brandBadge.setForeground(ACCENT_LIGHT);
        brandBadge.setBackground(ACCENT_DIM);
        brandBadge.setOpaque(true);
        brandBadge.setBorder(new EmptyBorder(3, 8, 3, 8));
        header.add(nameLabel, BorderLayout.CENTER);
        header.add(brandBadge, BorderLayout.EAST);

        // Description
        JLabel desc = new JLabel("<html><p style='width:480px; color:#908a83'>" + p.description + "</p></html>");
        desc.setFont(FONT_BODY);
        desc.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Details grid
        JPanel details = new JPanel(new GridLayout(1, 3, 16, 0));
        details.setOpaque(false);
        details.setBorder(new EmptyBorder(20, 0, 20, 0));
        details.add(buildDetailChip("Product ID", "#" + p.id));
        details.add(buildDetailChip("In Stock", p.stock + " units"));
        details.add(buildDetailChip("Price", formatPrice(p.price)));

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);

        JButton addCartBtn = accentButton("🛒  Add to Cart");
        addCartBtn.addActionListener(e -> {
            cart.add(p);
            updateCartBadge();
            showToast(panel, p.name + " added to cart!");
        });

        JButton buyNowBtn = new JButton("⚡  Buy Now");
        buyNowBtn.setFont(FONT_BOLD);
        buyNowBtn.setForeground(TEXT_PRI);
        buyNowBtn.setBackground(SURFACE2);
        buyNowBtn.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(10, 20, 10, 20)));
        buyNowBtn.setFocusPainted(false);
        buyNowBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buyNowBtn.addActionListener(e -> {
            cart.add(p);
            updateCartBadge();
            showCheckout();
        });

        actions.add(addCartBtn);
        actions.add(buyNowBtn);

        card.add(header, BorderLayout.NORTH);
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setOpaque(false);
        bodyPanel.add(desc);
        bodyPanel.add(details);
        bodyPanel.add(actions);
        card.add(bodyPanel, BorderLayout.CENTER);

        gbc.gridy = 0;
        content.add(card, gbc);

        panel.add(content, BorderLayout.CENTER);
        String key = "DETAIL_" + p.id + "_" + sub.name;
        mainPanel.add(panel, key);
        cardLayout.show(mainPanel, key);
    }

    private JPanel buildDetailChip(String label, String value) {
        JPanel chip = new JPanel(new BorderLayout(0, 4));
        chip.setBackground(SURFACE2);
        chip.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(12, 16, 12, 16)));
        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lbl.setForeground(TEXT_MUT);
        JLabel val = new JLabel(value);
        val.setFont(FONT_BOLD);
        val.setForeground(TEXT_PRI);
        chip.add(lbl, BorderLayout.NORTH);
        chip.add(val, BorderLayout.CENTER);
        return chip;
    }

    // ─── CART SCREEN ─────────────────────────────────────────────────────────

    private JPanel buildCartScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.add(buildTopNav("Shopping Cart", true, () -> cardLayout.show(mainPanel, "HOME")), BorderLayout.NORTH);
        panel.setName("CART_PANEL");
        return panel;
    }

    private void showCart() {
        // Rebuild cart content dynamically
        JPanel existing = null;
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof JPanel && "CART_PANEL".equals(((JPanel)c).getName())) {
                existing = (JPanel) c;
                break;
            }
        }
        if (existing != null) mainPanel.remove(existing);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setName("CART_PANEL");
        panel.add(buildTopNav("Shopping Cart", true, () -> cardLayout.show(mainPanel, "HOME")), BorderLayout.NORTH);

        if (cart.count() == 0) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setBackground(BG);
            JLabel msg = new JLabel("🛒  Your cart is empty");
            msg.setFont(FONT_H2);
            msg.setForeground(TEXT_SEC);
            empty.add(msg);
            panel.add(empty, BorderLayout.CENTER);
        } else {
            JPanel content = new JPanel(new BorderLayout(24, 0));
            content.setBackground(BG);
            content.setBorder(new EmptyBorder(24, 32, 24, 32));

            // Cart items list
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setOpaque(false);

            for (int i = 0; i < cart.items.size(); i++) {
                final int idx = i;
                Product p = cart.items.get(i);
                JPanel row = new JPanel(new BorderLayout(12, 0));
                row.setBackground(SURFACE);
                row.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(14, 18, 14, 18)));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel name = new JLabel(p.name);
                name.setFont(FONT_BOLD);
                name.setForeground(TEXT_PRI);
                JLabel brand = new JLabel(p.brand);
                brand.setFont(FONT_SMALL);
                brand.setForeground(TEXT_SEC);
                JPanel namePanel = new JPanel(new BorderLayout(0, 3));
                namePanel.setOpaque(false);
                namePanel.add(name, BorderLayout.NORTH);
                namePanel.add(brand, BorderLayout.SOUTH);

                JLabel price = new JLabel(formatPrice(p.price));
                price.setFont(FONT_PRICE);
                price.setForeground(ACCENT_LIGHT);

                JButton remove = flatButton("✕");
                remove.setFont(new Font("SansSerif", Font.BOLD, 14));
                remove.setForeground(new Color(200, 80, 80));
                remove.addActionListener(e -> {
                    cart.remove(idx);
                    updateCartBadge();
                    showCart();
                });

                JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
                rightPanel.setOpaque(false);
                rightPanel.add(price);
                rightPanel.add(remove);

                row.add(namePanel, BorderLayout.CENTER);
                row.add(rightPanel, BorderLayout.EAST);

                itemsPanel.add(row);
                itemsPanel.add(Box.createVerticalStrut(8));
            }

            JScrollPane scroll = new JScrollPane(itemsPanel);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(14);

            // Summary panel
            JPanel summary = new JPanel(new BorderLayout(0, 16));
            summary.setBackground(SURFACE);
            summary.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(24, 24, 24, 24)));
            summary.setPreferredSize(new Dimension(260, 0));

            JLabel summTitle = new JLabel("Order Summary");
            summTitle.setFont(FONT_H2);
            summTitle.setForeground(TEXT_PRI);

            JPanel lines = new JPanel(new GridLayout(0, 2, 8, 10));
            lines.setOpaque(false);
            lines.setBorder(new EmptyBorder(12, 0, 12, 0));
            lines.add(makeLabel("Items (" + cart.count() + ")", TEXT_SEC));
            lines.add(makeLabel(formatPrice(cart.totalAmount()), TEXT_PRI, SwingConstants.RIGHT));
            lines.add(makeLabel("Delivery", TEXT_SEC));
            lines.add(makeLabel("FREE", SUCCESS, SwingConstants.RIGHT));
            lines.add(new JSeparator());
            lines.add(new JSeparator());
            lines.add(makeLabel("Total", TEXT_PRI));
            lines.add(makeLabel(formatPrice(cart.totalAmount()), ACCENT_LIGHT, SwingConstants.RIGHT));

            JButton checkoutBtn = accentButton("Proceed to Checkout →");
            checkoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            checkoutBtn.addActionListener(e -> showCheckout());

            summary.add(summTitle, BorderLayout.NORTH);
            summary.add(lines, BorderLayout.CENTER);
            summary.add(checkoutBtn, BorderLayout.SOUTH);

            content.add(scroll, BorderLayout.CENTER);
            content.add(summary, BorderLayout.EAST);
            panel.add(content, BorderLayout.CENTER);
        }

        mainPanel.add(panel, "CART_PANEL");
        cardLayout.show(mainPanel, "CART_PANEL");
    }

    // ─── CHECKOUT SCREEN ─────────────────────────────────────────────────────

    private JPanel buildCheckoutScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setName("CHECKOUT");
        return panel;
    }

    private void showCheckout() {
        // Remove existing checkout panel
        for (int i = 0; i < mainPanel.getComponentCount(); i++) {
            Component c = mainPanel.getComponent(i);
            if (c instanceof JPanel && "CHECKOUT".equals(((JPanel)c).getName())) {
                mainPanel.remove(c);
                break;
            }
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setName("CHECKOUT");
        panel.add(buildTopNav("Checkout", true, this::showCart), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;

        JLabel heading = new JLabel("Payment Details");
        heading.setFont(FONT_TITLE);
        heading.setForeground(TEXT_PRI);
        heading.setBorder(new EmptyBorder(0, 0, 24, 0));
        gbc.gridy = 0;
        content.add(heading, gbc);

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 14));
        form.setBackground(SURFACE);
        form.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(28, 32, 28, 32)));

        JTextField cardField = buildFormField(form, "💳  Card Number", "XXXX XXXX XXXX XXXX");
        JTextField cvvField  = buildFormField(form, "🔒  CVV", "•••");
        JTextField otpField  = buildFormField(form, "📱  OTP", "Enter OTP sent to mobile");

        JLabel totalLine = new JLabel("Total Payable: " + formatPrice(cart.totalAmount()));
        totalLine.setFont(FONT_H2);
        totalLine.setForeground(ACCENT_LIGHT);
        totalLine.setBorder(new EmptyBorder(12, 0, 0, 0));
        form.add(totalLine);

        JButton payBtn = accentButton("💳  Pay " + formatPrice(cart.totalAmount()));
        payBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 46));
        payBtn.addActionListener(e -> {
            if (cardField.getText().trim().isEmpty() || cvvField.getText().trim().isEmpty() || otpField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all payment fields.", "Incomplete Details", JOptionPane.WARNING_MESSAGE);
                return;
            }
            cart.clear();
            updateCartBadge();
            cardLayout.show(mainPanel, "SUCCESS");
        });
        form.add(payBtn);

        gbc.gridy = 1;
        content.add(form, gbc);
        panel.add(content, BorderLayout.CENTER);

        mainPanel.add(panel, "CHECKOUT");
        cardLayout.show(mainPanel, "CHECKOUT");
    }

    private JTextField buildFormField(JPanel form, String label, String placeholder) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_SEC);
        form.add(lbl);

        JTextField field = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().equals(placeholder) || getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_MUT);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, getInsets().left + 2, getHeight()/2 + fm.getAscent()/2 - 1);
                    g2.dispose();
                }
            }
        };
        field.setFont(FONT_BODY);
        field.setForeground(TEXT_PRI);
        field.setBackground(SURFACE2);
        field.setCaretColor(ACCENT_LIGHT);
        field.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(10, 14, 10, 14)));
        field.setText("");
        field.setColumns(30);

        // Clear placeholder on focus
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { field.setForeground(TEXT_PRI); }
        });

        form.add(field);
        return field;
    }

    // ─── SUCCESS SCREEN ──────────────────────────────────────────────────────

    private JPanel buildSuccessScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        panel.setName("SUCCESS");

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(SURFACE);
        box.setBorder(new CompoundBorder(new LineBorder(new Color(50, 200, 100, 80), 1, true), new EmptyBorder(50, 60, 50, 60)));

        JLabel tick = new JLabel("✅", SwingConstants.CENTER);
        tick.setFont(new Font("SansSerif", Font.PLAIN, 56));
        tick.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heading = new JLabel("Order Placed Successfully!", SwingConstants.CENTER);
        heading.setFont(FONT_TITLE);
        heading.setForeground(SUCCESS);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
        heading.setBorder(new EmptyBorder(16, 0, 8, 0));

        JLabel sub = new JLabel("Your items will be delivered in 3-5 business days.", SwingConstants.CENTER);
        sub.setFont(FONT_BODY);
        sub.setForeground(TEXT_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton homeBtn = accentButton("Continue Shopping");
        homeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeBtn.setMaximumSize(new Dimension(220, 46));
        homeBtn.setBorder(new EmptyBorder(12, 28, 12, 28));
        homeBtn.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        box.add(tick);
        box.add(heading);
        box.add(sub);
        box.add(Box.createVerticalStrut(28));
        box.add(homeBtn);

        panel.add(box);
        return panel;
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    private void updateCartBadge() {
        if (cartBadge != null) cartBadge.setText(String.valueOf(cart.count()));
    }

    private Category findParent(SubCategory sub) {
        for (Category c : categories) {
            for (SubCategory s : c.subCategories) {
                if (s == sub) return c;
            }
        }
        return categories[0];
    }

    private String formatPrice(double price) {
        return "₹" + NumberFormat.getNumberInstance(new Locale("en", "IN")).format((long) price);
    }

    private void showToast(JPanel parent, String message) {
        JWindow toast = new JWindow(this);
        JLabel msg = new JLabel("  ✓  " + message + "  ");
        msg.setFont(FONT_BOLD);
        msg.setForeground(Color.WHITE);
        msg.setBackground(new Color(30, 160, 80));
        msg.setOpaque(true);
        msg.setBorder(new EmptyBorder(10, 16, 10, 16));
        toast.add(msg);
        toast.pack();

        Point loc = getLocationOnScreen();
        toast.setLocation(loc.x + getWidth()/2 - toast.getWidth()/2, loc.y + getHeight() - 80);
        toast.setVisible(true);
        new Timer(2000, e -> toast.dispose()) {{ setRepeats(false); start(); }};
    }

    private JButton accentButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, ACCENT, getWidth(), 0, new Color(220, 60, 10));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        return btn;
    }

    private JButton flatButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY);
        btn.setForeground(TEXT_SEC);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel makeLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(color);
        return l;
    }

    private JLabel makeLabel(String text, Color color, int align) {
        JLabel l = new JLabel(text, align);
        l.setFont(FONT_BOLD);
        l.setForeground(color);
        return l;
    }

    // ─── INNER CLASSES ───────────────────────────────────────────────────────

    static class HoverPanel extends JPanel {
        private Color normal, hover;
        private boolean hovered = false;

        HoverPanel(Color normal, Color hover) {
            this.normal = normal;
            this.hover = hover;
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            });
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? hover : normal);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ─── MAIN ────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(ErenShoppee::new);
    }
}
