/*
package gui.panel.employee;

import data.model.Employee;
import data.model.Ticket;
import gui.window.Mainframe;
import logic.RemoteConnection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class EmployeePanel extends JPanel {

    private static final String TITLE = "Probleme behandeln";
    private static final Dimension MINIMUM_SIZE = new Dimension(750, 350);
    private final Mainframe mainframe;
    private final Employee employee;

    private JButton sendAnswerButton;


    public EmployeePanel(Mainframe mainframe, Employee employee) {
        this.mainframe = mainframe;
        this.employee = employee;
        this.mainframe.setMinimumSize(MINIMUM_SIZE);
        this.mainframe.setTitle(TITLE);
        try {
            this.initPanel();
        } catch (MalformedURLException | NotBoundException | RemoteException ignored) {
            ignored.printStackTrace();
        }
    }


    private void initPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = GridBagConstraints.BOTH;
        gbc.weightx = GridBagConstraints.BOTH;
        this.setLayout(new GridBagLayout());

        JLabel ticketsLabel = new JLabel("Tickets:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(ticketsLabel, gbc);


        DefaultListModel<Ticket> ticketListModel = new DefaultListModel<>();
        JList<Ticket> ticketList = new JList<>(ticketListModel);
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane ticketScrollPane = new JScrollPane(ticketList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;

        RemoteConnection

//        List<Ticket> pendingTicketsOfEmployee = TicketViewLogic.getPendingTicketsOfEmployee(this.employee);
        pendingTicketsOfEmployee.forEach(ticketListModel::addElement);

        RemoteConnection.getInstance().getViewAccessLogic().getPendingTicketsOfEmpl

        this.add(ticketScrollPane, gbc);


        JLabel customerTextLabel = new JLabel("Kundentext:");
        customerTextLabel.setMinimumSize(new Dimension(300, 50));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        this.add(customerTextLabel, gbc);

        JTextArea customerTextArea = new JTextArea();
        customerTextArea.setEditable(false);
        customerTextArea.setLineWrap(true);
        customerTextArea.setWrapStyleWord(true);
        JScrollPane customerTextAreaScrollPane = new JScrollPane(customerTextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        this.add(customerTextAreaScrollPane, gbc);

        JLabel answerLabel = new JLabel("Antwort:");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 0.0;
        this.add(answerLabel, gbc);

        JTextArea answerTextArea = new JTextArea();
        answerTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void onChange() {
                sendAnswerButton.setEnabled(!answerTextArea.getText().isBlank());
            }
        });
        answerTextArea.setLineWrap(true);
        answerTextArea.setWrapStyleWord(true);
        answerTextArea.setEditable(false);
        JScrollPane answerScrollPane = new JScrollPane(answerTextArea);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weighty = GridBagConstraints.BOTH;
        this.add(answerScrollPane, gbc);


        JLabel previousAnswersLabel = new JLabel("Vorherige Antworten:");
        gbc.gridx = 3;
        gbc.gridy = 0;
        this.add(previousAnswersLabel, gbc);

        JTextArea previousAnswersTextArea = new JTextArea(15, 25);
        previousAnswersTextArea.setLineWrap(true);
        previousAnswersTextArea.setWrapStyleWord(true);
        previousAnswersTextArea.setEditable(false);

        JScrollPane previousAnswersTextAreaScrollPane = new JScrollPane(previousAnswersTextArea);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(previousAnswersTextAreaScrollPane, gbc);

        JLabel loggedAsLabel = new JLabel("Eingeloggt als: " + this.employee.getLoginName() + " Strikes: " + this.employee.getStrikeCount());
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        this.add(loggedAsLabel, gbc);

        JButton logoutButton = new JButton("Ausloggen");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        this.add(logoutButton, gbc);

        sendAnswerButton = new JButton("Antwort abschicken");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        this.add(sendAnswerButton, gbc);
        sendAnswerButton.setEnabled(false);
    }
}
*/
