package gui.panel.customer;


import data.model.Customer;
import gui.window.Mainframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OverviewPanel extends JPanel {
    private static final String TITLE = "Überblick";
    private static final Dimension MINIMUM_SIZE = new Dimension(300, 150);

    private final Mainframe mainframe;
    private final Customer customer;

    public OverviewPanel(Mainframe mainframe, Customer customer) {
        this.customer = customer;
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


        // button to show current tickets______________________________________
        JButton showTicketsButton = new JButton("Meine Tickets");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        showTicketsButton.addActionListener(this::showTicketsButtonClicked);
        this.add(showTicketsButton, gbc);

        // button to report a problem__________________________________________
        JButton reportProblemButton = new JButton("Problem melden");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        reportProblemButton.addActionListener(this::reportProblemButton);
        this.add(reportProblemButton, gbc);

        // button to log out___________________________________________________
        JButton logOutButton = new JButton("Ausloggen");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        logOutButton.addActionListener(this::logOutButtonClicked);
        this.add(logOutButton, gbc);
    }


    // button actions__________________________________________________________

    private void showTicketsButtonClicked(ActionEvent e) {
        mainframe.switchToShowTicketsPanel(customer);
    }

    private void reportProblemButton(ActionEvent e) {
        this.mainframe.switchToReportProblemPanel(customer);
    }

    private void logOutButtonClicked(ActionEvent e) {
        this.mainframe.switchToLoginPanel();
    }

}