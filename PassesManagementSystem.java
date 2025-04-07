import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;

public class PassesManagementSystem extends JFrame {
    // Colors and fonts
    private static final Color BG = new Color(245, 235, 220), CARD = new Color(226, 215, 195),
        PRIMARY = new Color(140, 100, 75), ACCENT = new Color(90, 60, 40), TEXT = new Color(50, 40, 30);
    private static final Font HEADER = new Font("Segoe UI", Font.BOLD, 16), 
        MAIN = new Font("Segoe UI", Font.PLAIN, 13), MONO = new Font("Consolas", Font.PLAIN, 14);
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
    
    private final Queue<String> general = new LinkedList<>();
    private final Stack<String> vip = new Stack<>();
    private int servedGeneral = 0, servedVip = 0;
    private JTextField nameField = createTextField();
    private JTextArea report = new JTextArea(), vipArea = new JTextArea(), generalArea = new JTextArea();
    private JLabel status = new JLabel("System Ready");

    public PassesManagementSystemr() {
        setTitle("Passes Management System");
        setSize(900, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setupUI();
    }

    private void setupUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.add(createBanner(), BorderLayout.NORTH);
        main.add(createContent(), BorderLayout.CENTER);
        main.add(createStatusBar(), BorderLayout.SOUTH);
        add(main);
    }

    private JPanel createBanner() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ACCENT);
        p.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel l = new JLabel("Passes Management System");
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        l.setForeground(CARD);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    private JPanel createStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ACCENT);
        p.setBorder(new EmptyBorder(5, 15, 5, 15));
        status.setFont(MAIN);
        status.setForeground(CARD);
        p.add(status, BorderLayout.WEST);
        return p;
    }

    private JPanel createContent() {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBorder(new CompoundBorder(new LineBorder(ACCENT, 1), new EmptyBorder(15, 15, 15, 15)));
        card.setBackground(BG);
        
        card.add(createInputPanel(), BorderLayout.NORTH);
        card.add(createMainPanel(), BorderLayout.CENTER);
        return card;
    }

    private JPanel createInputPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(createBorder("Customer Registration"));
        p.setBackground(BG);

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row1.add(new JLabel("Customer Name:"));
        nameField.setToolTipText("Enter letters only (no numbers or symbols)");
        row1.add(nameField);
        
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row2.add(createBtn("Add General", e -> addCustomer(false)));
        row2.add(createBtn("Add VIP", e -> addCustomer(true)));
        
        p.add(row1);
        p.add(row2);
        return p;
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(new BorderLayout(15, 15));
        p.add(createLists(), BorderLayout.CENTER);
        p.add(createActions(), BorderLayout.EAST);
        p.add(createReport(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createLists() {
        JPanel p = new JPanel(new GridLayout(1, 2, 15, 0));
        p.add(createListPanel("VIP Customers", vipArea));
        p.add(createListPanel("General Customers", generalArea));
        return p;
    }

    private JPanel createListPanel(String title, JTextArea area) {
        JPanel p = new JPanel(new BorderLayout());
        area.setEditable(false);
        area.setFont(MAIN);
        area.setBackground(CARD);
        p.setBorder(createBorder(title));
        p.add(new JScrollPane(area));
        return p;
    }

    private JPanel createActions() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(createBorder("Actions"));
        p.setPreferredSize(new Dimension(170, 0));
        
        p.add(Box.createVerticalGlue());
        p.add(createActionBtn("Serve VIP", e -> serve(true)));
        p.add(Box.createVerticalStrut(15));
        p.add(createActionBtn("Serve General", e -> serve(false)));
        p.add(Box.createVerticalStrut(15));
        p.add(createActionBtn("Display Report", e -> showReport()));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel createReport() {
        JPanel p = new JPanel(new BorderLayout());
        report.setEditable(false);
        report.setFont(MONO);
        report.setBackground(CARD);
        report.setForeground(TEXT);
        p.setBorder(createBorder("Service Report"));
        p.add(new JScrollPane(report));
        return p;
    }

    private Border createBorder(String title) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY, 1), title,
            TitledBorder.LEFT, TitledBorder.TOP, HEADER, ACCENT);
    }

    private JButton createBtn(String text, ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(PRIMARY);
        b.setForeground(CARD);
        b.setFont(MAIN);
        b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        b.addActionListener(a);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(ACCENT); }
            public void mouseExited(MouseEvent e) { b.setBackground(PRIMARY); }
        });
        return b;
    }

    private JButton createActionBtn(String text, ActionListener a) {
        JButton b = createBtn(text, a);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(160, 35));
        return b;
    }

    private JTextField createTextField() {
        JTextField f = new JTextField(20);
        f.setFont(MAIN);
        f.setBackground(CARD);
        f.setForeground(TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return f;
    }

    private void addCustomer(boolean isVip) {
        String name = nameField.getText().trim();
        try {
            if (!NAME_PATTERN.matcher(name).matches()) 
                throw new IllegalArgumentException(name.isEmpty() ? 
                    "Please enter a name" : "Name must contain only letters");
            
            if (isVip) vip.push(name); else general.add(name);
            nameField.setText("");
            updateLists();
            appendReport("Added " + (isVip ? "VIP" : "General") + ": " + name);
            status.setText("Added " + (isVip ? "VIP" : "General") + " customer");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void serve(boolean isVip) {
        String customer = isVip ? (vip.empty() ? null : vip.pop()) : general.poll();
        if (customer == null) {
            appendReport("No " + (isVip ? "VIP" : "General") + " customers");
            status.setText("No customers to serve");
            return;
        }
        if (isVip) servedVip++; else servedGeneral++;
        updateLists();
        appendReport("Served " + (isVip ? "VIP" : "General") + ": " + customer);
        status.setText("Served customer");
    }

    private void showReport() {
        report.setText(String.format(
            "=== Customer Service Report ===\n\n" +
            "VIP served: %d\nGeneral served: %d\n\n" +
            "VIP remaining: %d\nGeneral remaining: %d\n\n" +
            "Remaining VIP:\n%s\nRemaining General:\n%s",
            servedVip, servedGeneral, vip.size(), general.size(),
            vip.isEmpty() ? "None" : String.join("\n", vip),
            general.isEmpty() ? "None" : String.join("\n", general)));
        status.setText("Report generated");
    }

    private void updateLists() {
        vipArea.setText(String.join("\n", vip));
        generalArea.setText(String.join("\n", general));
    }

    private void appendReport(String text) {
        report.append(text + "\n");
        report.setCaretPosition(report.getDocument().getLength());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", BG);
            UIManager.put("Panel.background", BG);
            UIManager.put("OptionPane.messageForeground", TEXT);
        } catch (Exception e) { e.printStackTrace(); }
        
        SwingUtilities.invokeLater(() -> new PassManager().setVisible(true));
    }
}