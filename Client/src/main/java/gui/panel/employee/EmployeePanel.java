
package gui.panel.employee;

import data.model.Answer;
import data.model.Employee;
import data.model.Ticket;
import gui.window.Mainframe;
import logic.RemoteConnection;
import logic.ViewAccessLogic;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private JButton logoutButton;

    private JList<Ticket> ticketList;

    private JTextArea customerTextArea;
    private JTextArea answerTextArea;
    private JTextArea previousAnswersTextArea;

    private DefaultListModel<Ticket> ticketListModel;

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


    private void initPanel() throws MalformedURLException, NotBoundException, RemoteException {
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


        ticketListModel = new DefaultListModel<>();
        this.ticketList = new JList<>(ticketListModel);
        this.ticketList.addListSelectionListener(this::ticketSelected);
        List<Ticket> pendingTicketsOfEmployee = RemoteConnection.getInstance().getViewAccessLogic().getPendingTicketsOfEmployee(this.employee);
        pendingTicketsOfEmployee.forEach(ticketListModel::addElement);
        this.ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane ticketScrollPane = new JScrollPane(this.ticketList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(ticketScrollPane, gbc);


        JLabel customerTextLabel = new JLabel("Kundentext:");
        customerTextLabel.setMinimumSize(new Dimension(300, 50));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        this.add(customerTextLabel, gbc);

        this.customerTextArea = new JTextArea();
        this.customerTextArea.setEditable(false);
        this.customerTextArea.setLineWrap(true);
        this.customerTextArea.setWrapStyleWord(true);
        JScrollPane customerTextAreaScrollPane = new JScrollPane(this.customerTextArea);
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

        this.answerTextArea = new JTextArea();
        this.answerTextArea.getDocument().addDocumentListener(new DocumentListener() {
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
        });
        this.answerTextArea.setLineWrap(true);
        this.answerTextArea.setWrapStyleWord(true);
        this.answerTextArea.setEditable(false);
        JScrollPane answerScrollPane = new JScrollPane(this.answerTextArea);
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

        this.previousAnswersTextArea = new JTextArea(15, 25);
        this.previousAnswersTextArea.setLineWrap(true);
        this.previousAnswersTextArea.setWrapStyleWord(true);
        this.previousAnswersTextArea.setEditable(false);

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

        this.logoutButton = new JButton("Ausloggen");
        logoutButton.addActionListener(this::logoutClicked);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        this.add(logoutButton, gbc);

        this.sendAnswerButton = new JButton("Antwort abschicken");
        this.sendAnswerButton.addActionListener(this::sendAnswerClicked);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        this.add(this.sendAnswerButton, gbc);
        this.sendAnswerButton.setEnabled(false);
    }

    private void logoutClicked(ActionEvent event) {
        this.mainframe.switchToLoginPanel();
    }

    private void ticketSelected(ListSelectionEvent e) {
        Ticket selectedTicket = ticketList.getSelectedValue();
        if (selectedTicket != null) {
            customerTextArea.setText(selectedTicket.getDescription());

            try {
                ViewAccessLogic viewAccessLogic = RemoteConnection.getInstance().getViewAccessLogic();
                List<Answer> lastAnswerOfTicket = viewAccessLogic.getAnswersOfTicket(selectedTicket, true);
                List<Answer> otherAnswersOfTicket = viewAccessLogic.getAnswersOfTicket(selectedTicket, false);

                if (!lastAnswerOfTicket.isEmpty()) {
                    previousAnswersTextArea.setText(lastAnswerOfTicket.getFirst().getAnswerString());
                }


                for (Answer previousAnswer : otherAnswersOfTicket) {
                    previousAnswersTextArea.setText(previousAnswersTextArea.getText()
                            + "\n\n"
                            + previousAnswer.getAnswerString());
                }
                answerTextArea.setEditable(true);
                if (!answerTextArea.getText().isBlank()) {
                    sendAnswerButton.setEnabled(true);
                }

            } catch (RemoteException | MalformedURLException | NotBoundException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    private void sendAnswerClicked(ActionEvent event) {

        Ticket selectedTicket = ticketList.getSelectedValue();
        if (selectedTicket != null) {
            try {
                RemoteConnection.getInstance().getTicketAssignmentLogic().assignTicketToCustomer(selectedTicket,
                        new Answer(answerTextArea.getText(), true, selectedTicket.getTicketID()));

                this.ticketListModel.removeElement(selectedTicket);
                this.customerTextArea.setText("");
                this.answerTextArea.setText("");
                this.previousAnswersTextArea.setText("");
                this.sendAnswerButton.setEnabled(false);
                this.answerTextArea.setEnabled(false);

            } catch (RemoteException | MalformedURLException | NotBoundException ignored) {
                ignored.printStackTrace();
            }

        }
    }

    private void onChange() {
        this.sendAnswerButton.setEnabled(!this.answerTextArea.getText().isBlank());
    }
}