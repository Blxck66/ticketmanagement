package gui.panel;

import data.model.Customer;
import data.model.Employee;
import gui.window.Mainframe;
import logic.RemoteConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class LoginPanel extends JPanel {

    private static final String TITLE = "Einloggen";
    private static final Dimension MINIMUM_SIZE = new Dimension(300, 210);

    private final Mainframe mainframe;

    private JTextField nameField;
    private JPasswordField passwordField;
    private JRadioButton customerRadioButton;
    private JRadioButton employeeRadioButton;


    public LoginPanel(Mainframe mainframe) {
        this.mainframe = mainframe;
        this.mainframe.setMinimumSize(MINIMUM_SIZE);
        this.mainframe.setTitle(TITLE);
        this.initPanel();
    }

    private void initPanel() {
        // basic panel config__________________________________________________
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());

        // name________________________________________________________________
        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(nameLabel, gbc);

        this.nameField = new JTextField();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        this.add(nameField, gbc);


        // password____________________________________________________________
        JLabel passwordLabel = new JLabel("Passwort:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(passwordLabel, gbc);

        this.passwordField = new JPasswordField();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        this.add(passwordField, gbc);


        // Radiobuttons employee and customer__________________________________
        this.employeeRadioButton = new JRadioButton("Mitarbeiter");
        this.customerRadioButton = new JRadioButton("Kunde");
        this.customerRadioButton.setSelected(true);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(customerRadioButton);
        radioButtonGroup.add(employeeRadioButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(customerRadioButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(employeeRadioButton, gbc);


        //login button_________________________________________________________
        JButton loginButton = new JButton("Login");
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        //tells the button to call the loginButtonClicked function when clicked.
        loginButton.addActionListener(this::loginButtonClicked);
        this.add(loginButton, gbc);

    }

    // button actions__________________________________________________________

    private void loginButtonClicked(ActionEvent e) {
        try {
            if (this.customerRadioButton.isSelected()) {
                Customer authenticateCustomer = RemoteConnection.getInstance().getAuthenticationLogic()
                        .authenticateCustomer(nameField.getText(), String.valueOf(passwordField.getPassword()));
                if (authenticateCustomer != null) {
                    /*
                     * Switching the panel of the window to OverviewPanel of customer
                     * because authentication was successful.
                     */
                    this.mainframe.switchToOverviewPanel(authenticateCustomer);

                }
            } else if (this.employeeRadioButton.isSelected()) {
                Employee authenticatedEmployeeId = RemoteConnection.getInstance().getAuthenticationLogic()
                        .authenticateEmployee(nameField.getText(), String.valueOf(passwordField.getPassword()));
                if (authenticatedEmployeeId != null) {
                    this.mainframe.switchToEmployeePanel(authenticatedEmployeeId);
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

}