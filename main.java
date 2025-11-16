package com.mycompany.finalproject2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// USER CLASS
class User {
    String email, password, role;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

// PRODUCT CLASS
class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Object[] toRow() {
        return new Object[]{id, name, price, quantity};
    }
}

// CART ITEM CLASS
class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }
}

public class Finalproject2 extends JFrame {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<CartItem> cart = new ArrayList<>();
    private int nextId = 1;
    private User currentUser = null;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName, txtPrice, txtQuantity;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JLabel lblUserStatus;

    public Finalproject2() {
        setTitle("Sari-Sari Store - E-Commerce System");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createDashboardPanel(), "Dashboard");

        add(mainPanel);

        // Default accounts
        users.add(new User("admin@store.com", "admin123", "admin")); 
        users.add(new User("user@gmail.com", "user123", "user"));

        loadSampleProducts();
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 60, 120));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("SARI-SARI STORE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        panel.add(lblEmail, gbc);

        JTextField txtEmail = new JTextField(25);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridy++;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        panel.add(lblPass, gbc);

        JPasswordField txtPass = new JPasswordField(25);
        txtPass.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(txtPass, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setBackground(new Color(30, 60, 120));

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnRegister = new JButton("CREATE ACCOUNT");
        btnRegister.setBackground(new Color(0, 102, 204));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 18));

        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        panel.add(btnPanel, gbc);

        gbc.gridy++;
        JLabel hint = new JLabel("<html><center><small style='color:yellow'>Admin: admin@store.com / admin123<br>Or create your own account below!</small></center></html>", SwingConstants.CENTER);
        panel.add(hint, gbc);

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword());

            for (User u : users) {
                if (u.email.equalsIgnoreCase(email) && u.password.equals(pass)) {
                    currentUser = u;
                    lblUserStatus.setText("Logged in as: " + u.role.toUpperCase());
                    updateControlsForRole();
                    refreshTable();
                    cardLayout.show(mainPanel, "Dashboard");
                    JOptionPane.showMessageDialog(this, "Welcome back, " + email + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
        });

        btnRegister.addActionListener(e -> {
            txtEmail.setText("");
            txtPass.setText("");
            cardLayout.show(mainPanel, "Register");
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 80, 140));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CREATE USER ACCOUNT", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        panel.add(new JLabel("<html><h3 style='color:white'>Email:</h3></html>"), gbc);
        JTextField txtRegEmail = new JTextField(25);
        txtRegEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(txtRegEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("<html><h3 style='color:white'>Password:</h3></html>"), gbc);
        JPasswordField txtRegPass = new JPasswordField(25);
        txtRegPass.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(txtRegPass, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("<html><h3 style='color:white'>Confirm Password:</h3></html>"), gbc);
        JPasswordField txtConfirm = new JPasswordField(25);
        txtConfirm.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        panel.add(txtConfirm, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setBackground(new Color(50, 80, 140));

        JButton btnCreate = new JButton("CREATE ACCOUNT");
        btnCreate.setBackground(new Color(0, 153, 76));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnBack = new JButton("BACK TO LOGIN");
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));

        btnPanel.add(btnCreate);
        btnPanel.add(btnBack);
        panel.add(btnPanel, gbc);

        btnCreate.addActionListener(e -> {
            String email = txtRegEmail.getText().trim();
            String pass1 = new String(txtRegPass.getPassword());
            String pass2 = new String(txtConfirm.getPassword());

            if (email.isEmpty() || pass1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!pass1.equals(pass2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (User u : users) {
                if (u.email.equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            users.add(new User(email, pass1, "user"));
            JOptionPane.showMessageDialog(this, "Account created successfully!\nYou can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Login");
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 204));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("PRODUCT MANAGEMENT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        lblUserStatus = new JLabel("Logged in as: GUEST");
        lblUserStatus.setFont(new Font("Arial", Font.BOLD, 18));
        lblUserStatus.setForeground(Color.YELLOW);
        header.add(lblUserStatus, BorderLayout.EAST);

        JButton btnLogout = new JButton("LOGOUT");
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 16));
        header.add(btnLogout, BorderLayout.WEST);

        panel.add(header, BorderLayout.NORTH);

        // TABLE
        String[] cols = {"ID", "Product Name", "Price (₱)", "Stock"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // ONLY ADMIN can edit cells
                return currentUser != null && "admin".equals(currentUser.role);
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // FORM
        JPanel form = new JPanel(new GridLayout(4, 2, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(25, 60, 25, 60));
        form.setBackground(new Color(245, 250, 255));

        form.add(createLabel("Product Name:"));
        txtName = new JTextField();
        form.add(txtName);

        form.add(createLabel("Price (₱):"));
        txtPrice = new JTextField();
        form.add(txtPrice);

        form.add(createLabel("Stock Quantity:"));
        txtQuantity = new JTextField();
        form.add(txtQuantity);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        buttonPanel.setBackground(new Color(245, 250, 255));

        btnAdd = createStyledButton("ADD PRODUCT", new Color(34, 139, 34));
        btnUpdate = createStyledButton("UPDATE", new Color(255, 140, 0));
        btnDelete = createStyledButton("DELETE", new Color(178, 34, 34));
        JButton btnClear = createStyledButton("CLEAR", new Color(70, 130, 180));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        form.add(new JLabel(""));
        form.add(buttonPanel);

        panel.add(form, BorderLayout.SOUTH);

        // ACTIONS
        btnAdd.addActionListener(e -> addProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedToForm();
            }
        });

        btnLogout.addActionListener(e -> {
            currentUser = null;
            cart.clear();
            lblUserStatus.setText("Logged in as: GUEST");
            updateControlsForRole();
            cardLayout.show(mainPanel, "Login");
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
        });

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        return lbl;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        return btn;
    }

    private void updateControlsForRole() {
        boolean isAdmin = currentUser != null && "admin".equals(currentUser.role);

        // Instead of setEnabled, use setEditable and setBackground
        txtName.setEditable(isAdmin);
        txtPrice.setEditable(isAdmin);
        txtQuantity.setEditable(isAdmin);

        txtName.setBackground(Color.WHITE);
        txtPrice.setBackground(Color.WHITE);
        txtQuantity.setBackground(Color.WHITE);

        txtName.setForeground(Color.BLACK);
        txtPrice.setForeground(Color.BLACK);
        txtQuantity.setForeground(Color.BLACK);

        btnAdd.setVisible(isAdmin);
        btnUpdate.setVisible(isAdmin);
        btnDelete.setVisible(isAdmin);

        JPanel parent = (JPanel) txtName.getParent();
        // Remove any existing user buttons
        ArrayList<Component> toRemove = new ArrayList<>();
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton) {
                String t = ((JButton) c).getText();
                if (t.equals("ADD TO CART") || t.equals("VIEW CART")) toRemove.add(c);
            }
        }
        for (Component c : toRemove) parent.remove(c);

        if (!isAdmin && currentUser != null) {
            JButton btnAddToCart = createStyledButton("ADD TO CART", new Color(34, 139, 34));
            btnAddToCart.addActionListener(e -> addToCart());
            parent.add(btnAddToCart);

            JButton btnViewCart = createStyledButton("VIEW CART", new Color(0, 102, 204));
            btnViewCart.addActionListener(e -> viewCart());
            parent.add(btnViewCart);
        }

        parent.revalidate();
        parent.repaint();
    }

    private void loadSampleProducts() {
        Product[] items = {
            new Product(nextId++, "Gaming Mouse", 1299.00, 40),
            new Product(nextId++, "RGB Keyboard", 3499.00, 18),
            new Product(nextId++, "27\" Monitor", 14999.00, 10),
            new Product(nextId++, "USB Hub", 899.00, 75)
        };
        for (Product p : items) products.add(p);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : products) tableModel.addRow(p.toRow());
    }

    private void addProduct() {
        if (!"admin".equals(currentUser.role)) return;
        try {
            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQuantity.getText());
            if (name.isEmpty() || price <= 0 || qty < 0) throw new Exception();
            products.add(new Product(nextId++, name, price, qty));
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Product added successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        if (!"admin".equals(currentUser.role)) return;
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product first!"); return; }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQuantity.getText());
            if (name.isEmpty() || price <= 0 || qty < 0) throw new Exception();

            for (Product p : products) {
                if (p.getId() == id) {
                    p.setName(name); p.setPrice(price); p.setQuantity(qty);
                    break;
                }
            }
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Product updated!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid data!");
        }
    }

    private void deleteProduct() {
        if (!"admin".equals(currentUser.role)) return;
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product to delete!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this product permanently?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            products.removeIf(p -> p.getId() == id);
            refreshTable();
            clearForm();
        }
    }

    private void loadSelectedToForm() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtName.setText(tableModel.getValueAt(row, 1).toString());
            txtPrice.setText(tableModel.getValueAt(row, 2).toString());
            txtQuantity.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        table.clearSelection();
    }

    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first!");
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            Product selected = null;
            for (Product p : products) {
                if (p.getId() == id) { selected = p; break; }
            }
            if (selected == null) return;

            String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to add:");
            if (qtyStr == null) return;
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0 || qty > selected.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!");
                return;
            }

            selected.setQuantity(selected.getQuantity() - qty);

            boolean found = false;
            for (CartItem c : cart) {
                if (c.product.getId() == selected.getId()) {
                    c.quantity += qty;
                    found = true;
                    break;
                }
            }
            if (!found) cart.add(new CartItem(selected, qty));

            refreshTable();
            JOptionPane.showMessageDialog(this, qty + " x " + selected.getName() + " added to cart!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (CartItem c : cart) {
            sb.append(c.product.getName())
              .append(" x ").append(c.quantity)
              .append(" = ₱").append(String.format("%.2f", c.getTotal()))
              .append("\n");
            total += c.getTotal();
        }
        sb.append("\nTotal: ₱").append(String.format("%.2f", total));

        // Custom option: Checkout button
        int option = JOptionPane.showOptionDialog(this, sb.toString(), "Your Cart",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"CHECKOUT"}, "CHECKOUT");

        if (option == 0) { // User clicked CHECKOUT
            checkout();
        }
    }

    private void checkout() {
        if (cart.isEmpty()) return;
        cart.clear();
        JOptionPane.showMessageDialog(this, "Thank you for your purchase!\nYour cart has been cleared.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Finalproject2().setVisible(true));
    }
}
