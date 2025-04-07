import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PassesManidsmintSistim extends JFrame {
    // Constants for UI styling - Earth tone palette
    private static final Color BACKGROUND_COLOR = new Color(245, 235, 220); // warm beige
    private static final Color SECONDARY_COLOR = new Color(226, 215, 195); // light taupe
    private static final Color PRIMARY_COLOR = new Color(140, 100, 75);    // medium brown
    private static final Color ACCENT_COLOR = new Color(90, 60, 40);       // dark brown
    private static final Color TEXT_COLOR = new Color(50, 40, 30);         // deep brown for text
    private static final Color HIGHLIGHT_COLOR = new Color(180, 140, 100); // lighter brown for highlights
    
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font MONOSPACE_FONT = new Font("Consolas", Font.PLAIN, 14);
    
    // Input validation pattern - only letters and spaces allowed
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    
    // Core components
    private final PassesManagerBackend system;
    private JTextField customerNameField;
    private JTextArea reportArea;
    private JPanel vipListPanel;
    private JPanel generalListPanel;
    private JLabel statusLabel;

    public PassesManidsmintSistim() {
        system = new PassesManagerBackend();
        initializeUI();
    }

    private void initializeUI() {
        configureMainWindow();
        setupMainContent();
        applySystemLookAndFeel();
    }

    private void configureMainWindow() {
        setTitle("Premium Passes Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void setupMainContent() {
        JPanel mainPanel = createMainPanel();
        
        // Create top banner
        JPanel topBanner = createTopBanner();
        mainPanel.add(topBanner, BorderLayout.NORTH);
        
        // Create card-like content panel
        JPanel contentCard = createContentCard();
        mainPanel.add(contentCard, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        add(mainPanel);
        updateCustomerLists();
    }

    private JPanel createTopBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(ACCENT_COLOR);
        banner.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel titleLabel = new JLabel("Premium Passes Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(SECONDARY_COLOR);
        
        banner.add(titleLabel, BorderLayout.WEST);
        
        return banner;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(ACCENT_COLOR);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        statusLabel = new JLabel("System Ready");
        statusLabel.setFont(TEXT_FONT);
        statusLabel.setForeground(SECONDARY_COLOR);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        return statusBar;
    }
    
    private JPanel createContentCard() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBorder(new CompoundBorder(
            new EmptyBorder(15, 15, 15, 15),
            new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        card.setBackground(BACKGROUND_COLOR);
        
        card.add(createInputPanel(), BorderLayout.NORTH);
        card.add(createMainContentPanel(), BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        
        panel.add(createCustomerSection(), BorderLayout.CENTER);
        panel.add(createReportSection(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCustomerSection() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(BACKGROUND_COLOR);
        
        panel.add(createListsPanel(), BorderLayout.CENTER);
        panel.add(createActionPanel(), BorderLayout.EAST);
        
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BACKGROUND_COLOR);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(createPanelBorder("Customer Registration"));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inputRow.setBackground(BACKGROUND_COLOR);

        JLabel nameLabel = createLabel("Customer Name:");
        customerNameField = createTextField(20);
        
        // Add tooltip to indicate valid input
        customerNameField.setToolTipText("Enter letters only (no numbers or symbols)");
        
        inputRow.add(nameLabel);
        inputRow.add(customerNameField);
        
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttonRow.setBackground(BACKGROUND_COLOR);
        
        buttonRow.add(createButton("Add General Customer", e -> addGeneralCustomer()));
        buttonRow.add(createButton("Add VIP Customer", e -> addVipCustomer()));
        
        panel.add(inputRow);
        panel.add(buttonRow);

        return panel;
    }

    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBackground(BACKGROUND_COLOR);

        vipListPanel = createCustomerListPanel();
        generalListPanel = createCustomerListPanel();

        panel.add(createListContainer("VIP Customers", vipListPanel));
        panel.add(createListContainer("General Customers", generalListPanel));

        return panel;
    }

    private JPanel createListContainer(String title, JPanel listPanel) {
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(createPanelBorder(title));
        container.setBackground(BACKGROUND_COLOR);
        container.add(scrollPane, BorderLayout.CENTER);
        
        container.setPreferredSize(new Dimension(0, 250));
        
        return container;
    }

    private JPanel createCustomerListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(createPanelBorder("Actions"));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(170, 0));

        panel.add(Box.createVerticalGlue());
        panel.add(createActionButton("Serve VIP Customer", e -> serveVipCustomer()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createActionButton("Serve General Customer", e -> serveGeneralCustomer()));
        panel.add(Box.createVerticalStrut(15));
        panel.add(createActionButton("Display Report", e -> displayReport()));
        panel.add(Box.createVerticalGlue());

        return panel;
    }
    
    private JPanel createReportSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(createPanelBorder("Service Report"));
        
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(MONOSPACE_FONT);
        reportArea.setBackground(SECONDARY_COLOR);
        reportArea.setForeground(TEXT_COLOR);
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JButton createActionButton(String text, ActionListener listener) {
        JButton button = createButton(text, listener);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 35));
        return button;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(TEXT_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(TEXT_FONT);
        return label;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(SECONDARY_COLOR);
        field.setForeground(TEXT_COLOR);
        field.setFont(TEXT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private Border createPanelBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1), title,
                TitledBorder.LEFT, TitledBorder.TOP,
                HEADER_FONT, ACCENT_COLOR);
    }

    private boolean isValidName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        
        // Check that name contains only letters and spaces
        return NAME_PATTERN.matcher(name).matches();
    }

    private void addGeneralCustomer() {
        String name = customerNameField.getText().trim();
        try {
            validateName(name);
            system.addGeneralCustomer(name);
            customerNameField.setText("");
            updateCustomerLists();
            appendToReport("Added general customer: " + name);
            updateStatus("Added general customer: " + name);
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void addVipCustomer() {
        String name = customerNameField.getText().trim();
        try {
            validateName(name);
            system.addVipCustomer(name);
            customerNameField.setText("");
            updateCustomerLists();
            appendToReport("Added VIP customer: " + name);
            updateStatus("Added VIP customer: " + name);
        } catch (IllegalArgumentException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void validateName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Please enter a customer name");
        }
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Customer name must contain only letters (no numbers or symbols)");
        }
    }

    private void serveVipCustomer() {
        if (!system.hasVipCustomers()) {
            appendToReport("No VIP customers to serve.");
            updateStatus("No VIP customers to serve");
            return;
        }
        
        String customer = system.serveNextVipCustomer();
        updateCustomerLists();
        appendToReport("Serving VIP customer: " + customer);
        updateStatus("Serving VIP customer: " + customer);
    }

    private void serveGeneralCustomer() {
        if (!system.hasGeneralCustomers()) {
            appendToReport("No general customers to serve.");
            updateStatus("No general customers to serve");
            return;
        }
        
        String customer = system.serveNextGeneralCustomer();
        updateCustomerLists();
        appendToReport("Serving general customer: " + customer);
        updateStatus("Serving general customer: " + customer);
    }

    private void displayReport() {
        reportArea.setText(system.generateReport());
        updateStatus("Generated service report");
    }

    private void updateCustomerLists() {
        updateVipList();
        updateGeneralList();
        repaintLists();
    }

    private void updateVipList() {
        vipListPanel.removeAll();
        system.getVipCustomers().forEach(customer -> 
            vipListPanel.add(createCustomerCard(customer, true)));
    }

    private void updateGeneralList() {
        generalListPanel.removeAll();
        system.getGeneralCustomers().forEach(customer -> 
            generalListPanel.add(createCustomerCard(customer, false)));
    }

    private JPanel createCustomerCard(String name, boolean isVip) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(isVip ? new Color(226, 217, 188) : SECONDARY_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel nameLabel = new JLabel(name);
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(TEXT_FONT);
        
        JLabel typeLabel = new JLabel(isVip ? "VIP" : "");
        typeLabel.setForeground(PRIMARY_COLOR);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        card.add(nameLabel, BorderLayout.WEST);
        card.add(typeLabel, BorderLayout.EAST);
        
        return card;
    }

    private void repaintLists() {
        vipListPanel.revalidate();
        vipListPanel.repaint();
        generalListPanel.revalidate();
        generalListPanel.repaint();
    }

    private void appendToReport(String text) {
        reportArea.append(text + "\n");
        reportArea.setCaretPosition(reportArea.getDocument().getLength());
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    private void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", BACKGROUND_COLOR);
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PassesManidsmintSistim().setVisible(true));
    }

    // Improved data management class with better encapsulation
    private static class PassesManagerBackend {
        private final Queue<String> generalCustomers = new LinkedList<>();
        private final Stack<String> vipCustomers = new Stack<>();
        private int generalCustomersServed = 0;
        private int vipCustomersServed = 0;

        public void addGeneralCustomer(String name) {
            generalCustomers.add(name);
        }

        public void addVipCustomer(String name) {
            vipCustomers.push(name);
        }

        public String serveNextGeneralCustomer() {
            generalCustomersServed++;
            return generalCustomers.poll();
        }

        public String serveNextVipCustomer() {
            vipCustomersServed++;
            return vipCustomers.pop();
        }

        public boolean hasGeneralCustomers() {
            return !generalCustomers.isEmpty();
        }

        public boolean hasVipCustomers() {
            return !vipCustomers.isEmpty();
        }

        public Iterable<String> getGeneralCustomers() {
            return generalCustomers;
        }

        public Iterable<String> getVipCustomers() {
            return new ArrayList<>(vipCustomers);
        }

        public String generateReport() {
            StringBuilder report = new StringBuilder();
            report.append("=== Customer Service Report ===\n\n");
            report.append(String.format("Total VIP customers served: %d\n", vipCustomersServed));
            report.append(String.format("Total general customers served: %d\n", generalCustomersServed));
            report.append(String.format("Remaining VIP customers: %d\n", vipCustomers.size()));
            report.append(String.format("Remaining general customers: %d\n\n", generalCustomers.size()));

            report.append("Remaining VIP customers:\n");
            if (vipCustomers.isEmpty()) {
                report.append("None\n");
            } else {
                new ArrayList<>(vipCustomers).forEach(c -> report.append("- ").append(c).append("\n"));
            }

            report.append("\nRemaining general customers:\n");
            if (generalCustomers.isEmpty()) {
                report.append("None\n");
            } else {
                generalCustomers.forEach(c -> report.append("- ").append(c).append("\n"));
            }

            return report.toString();
        }
    }
}