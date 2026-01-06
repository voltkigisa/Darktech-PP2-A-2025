package com.library.pos.views.manager.fines;

import com.library.pos.controllers.FineController;
import com.library.pos.models.Fine;
import com.library.pos.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * PayFineDialog - Dialog for paying a fine with Cash or Digital method
 */
public class PayFineDialog extends JDialog {
    private FineController controller;
    private Fine fine;
    private User currentUser;
    private IndexFineView parentView;

    private JButton btnCash, btnDigital;
    private String selectedMethod = null;
    private JButton btnPay, btnCancel;

    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color SECONDARY_COLOR = new Color(149, 165, 166);
    private final Color DARK_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color SELECTED_COLOR = new Color(41, 128, 185);
    private final Color UNSELECTED_COLOR = new Color(189, 195, 199);

    public PayFineDialog(Window parent, int fineId, User currentUser, IndexFineView parentView) {
        super(parent, "Pembayaran Denda", ModalityType.APPLICATION_MODAL);
        this.controller = new FineController();
        this.fine = controller.getFineById(fineId);
        this.currentUser = currentUser;
        this.parentView = parentView;

        if (fine == null) {
            JOptionPane.showMessageDialog(parent, "Denda tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        if (fine.isPaid()) {
            JOptionPane.showMessageDialog(parent, "Denda sudah dibayar!", "Info", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(450, 420);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(LIGHT_BG);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SUCCESS_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel("PEMBAYARAN DENDA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel contentContainer = new JPanel();
        contentContainer.setLayout(new BoxLayout(contentContainer, BoxLayout.Y_AXIS));
        contentContainer.setBackground(Color.WHITE);
        contentContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Member info
        JPanel memberPanel = createInfoRow("Anggota:", fine.getMemberName());
        contentContainer.add(memberPanel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 12)));

        // Days overdue info
        JPanel daysPanel = createInfoRow("Keterlambatan:",
                fine.getDaysOverdue() + " hari × " + fine.getTotalBooks() + " buku");
        contentContainer.add(daysPanel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Separator
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentContainer.add(separator1);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Payment method section - ABOVE total
        JLabel lblMethod = new JLabel("Pilih Metode Pembayaran:");
        lblMethod.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMethod.setForeground(DARK_COLOR);
        lblMethod.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentContainer.add(lblMethod);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Toggle buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        btnCash = createMethodButton("TUNAI (Cash)", false);
        btnDigital = createMethodButton("DIGITAL", false);

        btnCash.addActionListener(e -> selectMethod("CASH"));
        btnDigital.addActionListener(e -> selectMethod("DIGITAL"));

        buttonPanel.add(btnCash);
        buttonPanel.add(btnDigital);

        contentContainer.add(buttonPanel);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Separator
        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        contentContainer.add(separator2);
        contentContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Total amount - NOW BELOW payment method
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(231, 76, 60, 20));
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(231, 76, 60), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblTotalLabel = new JLabel("Total yang harus dibayar:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTotalLabel.setForeground(DARK_COLOR);

        JLabel lblTotalAmount = new JLabel(fine.getFormattedAmount());
        lblTotalAmount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotalAmount.setForeground(new Color(231, 76, 60));
        lblTotalAmount.setHorizontalAlignment(SwingConstants.RIGHT);

        totalPanel.add(lblTotalLabel, BorderLayout.WEST);
        totalPanel.add(lblTotalAmount, BorderLayout.EAST);

        contentContainer.add(totalPanel);

        mainPanel.add(contentContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLabel.setForeground(new Color(127, 140, 141));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblValue.setForeground(DARK_COLOR);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(lblLabel, BorderLayout.WEST);
        panel.add(lblValue, BorderLayout.EAST);

        return panel;
    }

    private JButton createMethodButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 45));

        if (selected) {
            button.setBackground(SELECTED_COLOR);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 2));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(DARK_COLOR);
            button.setBorder(BorderFactory.createLineBorder(UNSELECTED_COLOR, 2));
        }

        return button;
    }

    private void selectMethod(String method) {
        selectedMethod = method;

        // Update button styles
        if (method.equals("CASH")) {
            btnCash.setBackground(SELECTED_COLOR);
            btnCash.setForeground(Color.WHITE);
            btnCash.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 2));

            btnDigital.setBackground(Color.WHITE);
            btnDigital.setForeground(DARK_COLOR);
            btnDigital.setBorder(BorderFactory.createLineBorder(UNSELECTED_COLOR, 2));
        } else {
            btnDigital.setBackground(SELECTED_COLOR);
            btnDigital.setForeground(Color.WHITE);
            btnDigital.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 2));

            btnCash.setBackground(Color.WHITE);
            btnCash.setForeground(DARK_COLOR);
            btnCash.setBorder(BorderFactory.createLineBorder(UNSELECTED_COLOR, 2));
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panel.setBackground(LIGHT_BG);

        btnPay = createActionButton("BAYAR SEKARANG", SUCCESS_COLOR);
        btnPay.addActionListener(e -> processPayment());

        btnCancel = createActionButton("BATAL", SECONDARY_COLOR);
        btnCancel.addActionListener(e -> dispose());

        panel.add(btnPay);
        panel.add(btnCancel);

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 42));

        button.addMouseListener(new MouseAdapter() {
            Color original = bgColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(original);
            }
        });

        return button;
    }

    private void processPayment() {
        // Validate payment method selection
        if (selectedMethod == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih metode pembayaran!",
                    "Validasi Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (controller.payFine(fine.getId(), selectedMethod, currentUser.getId())) {
            if (parentView != null) {
                parentView.loadData();
            }
            dispose();
        }
    }
}
