import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ATMSimpleSwing {
    public static void main(String[] args) {
        // Set a modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
        new LoginScreen();
    }
}

class LoginScreen extends JFrame implements ActionListener {
    JTextField userField;
    JPasswordField pinField;
    JButton loginButton;
    JLabel titleLabel;

    LoginScreen() {
        setTitle("ATM Login");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 62, 80));

        titleLabel = new JLabel("Welcome to Modern ATM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(236, 240, 241));
        titleLabel.setBounds(40, 10, 270, 30);
        add(titleLabel);

        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(40, 60, 80, 25);
        userLabel.setForeground(new Color(236, 240, 241));
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(130, 60, 160, 25);
        userField.setBackground(new Color(189, 195, 199));
        add(userField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(40, 100, 80, 25);
        pinLabel.setForeground(new Color(236, 240, 241));
        add(pinLabel);

        pinField = new JPasswordField();
        pinField.setBounds(130, 100, 160, 25);
        pinField.setBackground(new Color(189, 195, 199));
        add(pinField);

        loginButton = new JButton("Login");
        loginButton.setBounds(110, 150, 120, 35);
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(this);
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String user = userField.getText();
        String pin = new String(pinField.getPassword());

        if (user.equals("Chandan") && pin.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new ATMPanel(user);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid ID or PIN", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class ATMPanel extends JFrame implements ActionListener {
    String userId;
    double balance = 5000.0;

    JLabel balanceLabel, messageLabel, titleLabel;
    JButton depositBtn, withdrawBtn, logoutBtn;

    ATMPanel(String userId) {
        this.userId = userId;
        this.balance = readBalanceFromFile();

        setTitle("ATM Interface - " + userId);
        setSize(400, 320);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(52, 73, 94));

        titleLabel = new JLabel("ATM Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(241, 196, 15));
        titleLabel.setBounds(110, 10, 200, 30);
        add(titleLabel);

        balanceLabel = new JLabel("Balance: ₹" + balance);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(39, 174, 96));
        balanceLabel.setBounds(40, 60, 300, 25);
        add(balanceLabel);

        messageLabel = new JLabel("Welcome, " + userId);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(236, 240, 241));
        messageLabel.setBounds(40, 90, 300, 25);
        add(messageLabel);

        depositBtn = new JButton("Deposit");
        depositBtn.setBounds(40, 140, 120, 40);
        depositBtn.setBackground(new Color(41, 128, 185));
        depositBtn.setForeground(Color.WHITE);
        depositBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        depositBtn.setFocusPainted(false);
        depositBtn.addActionListener(this);
        add(depositBtn);

        withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(220, 140, 120, 40);
        withdrawBtn.setBackground(new Color(192, 57, 43));
        withdrawBtn.setForeground(Color.WHITE);
        withdrawBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        withdrawBtn.setFocusPainted(false);
        withdrawBtn.addActionListener(this);
        add(withdrawBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(130, 210, 120, 35);
        logoutBtn.setBackground(new Color(127, 140, 141));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(this);
        add(logoutBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositBtn) {
            String input = JOptionPane.showInputDialog(this, "Enter deposit amount:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) {
                    balance += amount;
                    writeBalanceToFile(balance);
                    messageLabel.setText("Deposited ₹" + amount);
                } else {
                    messageLabel.setText("Enter a valid amount.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == withdrawBtn) {
            String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount:");
            try {
                double amount = Double.parseDouble(input);
                if (amount > balance) {
                    messageLabel.setText("Insufficient funds.");
                } else if (amount <= 0) {
                    messageLabel.setText("Enter a valid amount.");
                } else {
                    balance -= amount;
                    writeBalanceToFile(balance);
                    messageLabel.setText("Withdrew ₹" + amount);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == logoutBtn) {
            writeBalanceToFile(balance);
            dispose();
            new LoginScreen();
        }

        balanceLabel.setText("Balance: ₹" + balance);
    }

    double readBalanceFromFile() {
        try {
            java.io.File file = new java.io.File("balance.txt");
            if (!file.exists()) {
                writeBalanceToFile(balance);
            }
            java.util.Scanner sc = new java.util.Scanner(file);
            if (sc.hasNextDouble()) {
                return sc.nextDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 5000.0;
    }

    void writeBalanceToFile(double bal) {
        try {
            java.io.PrintWriter pw = new java.io.PrintWriter("balance.txt");
            pw.println(bal);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
