package gui.panel.customer;


import data.model.Answer;
import data.model.Customer;
import data.model.Ticket;
import gui.window.Mainframe;
import logic.RemoteConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ShowTicketsPanel extends JPanel {
    private static final String TITLE = "Potentielle Lösungen";
    private static final Dimension MINIMUM_SIZE = new Dimension(600, 300);

    private final Mainframe mainframe;
    private final Customer customer;

    JList<Ticket> answeredTicketsList;

    JTextArea ticketDescriptionTextArea;
    JTextArea answerTextArea;

    JButton notSolvedButton;
    JButton solvedButton;

    DefaultListModel<Ticket> answeredTicketsListModel;
    DefaultListModel<Ticket> pendingTicketsListModel;

    public ShowTicketsPanel(Mainframe mainframe, Customer customer) {
        this.mainframe = mainframe;
        this.customer = customer;
        this.mainframe.setMinimumSize(MINIMUM_SIZE);
        this.mainframe.setTitle(TITLE);
        try {
            initPanel();
        } catch (MalformedURLException | NotBoundException | RemoteException ignored) {
            ignored.printStackTrace();
        }
    }


    private void initPanel() throws MalformedURLException, NotBoundException, RemoteException {
        // basic panel config__________________________________________________
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());


        // answered tickets label______________________________________________
        JLabel answeredTicketsLabel = new JLabel("Beantwortete Tickets:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(answeredTicketsLabel, gbc);


        // answered tickets list_______________________________________________
        this.answeredTicketsListModel = new DefaultListModel<>();
        this.answeredTicketsList = new JList<>(this.answeredTicketsListModel);
        this.answeredTicketsList.addListSelectionListener(this::answeredTicketSelected);
        this.answeredTicketsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane answeredKeywordsScroll = new JScrollPane(this.answeredTicketsList);
        List<Ticket> answeredTicketsOfCustomer = RemoteConnection.getInstance()
                .getViewAccessLogic()
                .getAnsweredTicketsOfCustomer(this.customer);
        answeredTicketsOfCustomer.forEach(this.answeredTicketsListModel::addElement);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(answeredKeywordsScroll, gbc);


        // pending tickets label_______________________________________________
        JLabel pendingTicketsLabel = new JLabel("ausstehende Tickets:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(pendingTicketsLabel, gbc);


        // pending tickets list________________________________________________
        this.pendingTicketsListModel = new DefaultListModel<>();
        JList<Ticket> pendingTicketsList = new JList<>(this.pendingTicketsListModel);
        pendingTicketsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane pendingTicketsScroll = new JScrollPane(pendingTicketsList);
        List<Ticket> pendingTicketsOfCustomer = RemoteConnection.getInstance().getViewAccessLogic().getPendingTicketsOfCustomer(this.customer);
        pendingTicketsOfCustomer.forEach(this.pendingTicketsListModel::addElement);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(pendingTicketsScroll, gbc);


        // customers text label________________________________________________
        JLabel myTextLabel = new JLabel("Mein Text:");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        this.add(myTextLabel, gbc);


        // ticket description__________________________________________________
        this.ticketDescriptionTextArea = new JTextArea();
        this.ticketDescriptionTextArea.setLineWrap(true);
        this.ticketDescriptionTextArea.setWrapStyleWord(true);
        this.ticketDescriptionTextArea.setEditable(false);
        JScrollPane ticketDescriptionTextAreaScroll = new JScrollPane(this.ticketDescriptionTextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(ticketDescriptionTextAreaScroll, gbc);


        // answer of employee label____________________________________________
        JLabel answerLabel = new JLabel("Antwort:");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(answerLabel, gbc);


        // answer of employee text area________________________________________
        this.answerTextArea = new JTextArea();
        this.answerTextArea.setLineWrap(true);
        this.answerTextArea.setWrapStyleWord(true);
        this.answerTextArea.setEditable(false);
        JScrollPane answerTextAreaScroll = new JScrollPane(this.answerTextArea);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        this.add(answerTextAreaScroll, gbc);


        // not solved button___________________________________________________
        this.notSolvedButton = new JButton("Problem nicht gelöst");
        this.notSolvedButton.setEnabled(false);
        this.notSolvedButton.addActionListener(this::notSolvedButtonClicked);
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        this.add(this.notSolvedButton, gbc);


        // solved button_______________________________________________________
        this.solvedButton = new JButton("Problem gelöst");
        this.solvedButton.setEnabled(false);
        this.solvedButton.addActionListener(this::SolvedButtonClicked);
        gbc.gridx = 0;
        gbc.gridy = 8;
        this.add(this.solvedButton, gbc);


        // back button_________________________________________________________
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(this::backButtonClicked);
        gbc.gridx = 0;
        gbc.gridy = 9;
        this.add(backButton, gbc);

    }


    // button actions__________________________________________________________
    private void notSolvedButtonClicked(ActionEvent e) {
        Ticket selectedTicket = this.answeredTicketsList.getSelectedValue();
        if (selectedTicket != null) {
            try {
                RemoteConnection.getInstance().getTicketAssignmentLogic().reAssignTicketToEmployee(selectedTicket);
                this.answeredTicketsListModel.removeElement(selectedTicket);
                this.answerTextArea.setText("");
                this.ticketDescriptionTextArea.setText("");
                this.solvedButton.setEnabled(false);
                this.notSolvedButton.setEnabled(false);
                this.pendingTicketsListModel.addElement(selectedTicket);
            } catch (RemoteException | MalformedURLException | NotBoundException ignored) {
                ignored.printStackTrace();
            }
        }

    }

    private void SolvedButtonClicked(ActionEvent e) {

        Ticket selectedTicket = this.answeredTicketsList.getSelectedValue();

        this.answeredTicketsListModel.removeElement(selectedTicket);
        this.ticketDescriptionTextArea.setText("");
        this.answerTextArea.setText("");
        this.notSolvedButton.setEnabled(false);
        this.solvedButton.setEnabled(false);
        try {
            RemoteConnection.getInstance().getTicketAssignmentLogic().unassignTicked(selectedTicket);
        } catch (RemoteException | MalformedURLException | NotBoundException ignored) {
            ignored.printStackTrace();
        }
    }

    private void backButtonClicked(ActionEvent e) {
        this.mainframe.switchToOverviewPanel(customer);
    }


    // selection changed actions_______________________________________________

    private void answeredTicketSelected(ListSelectionEvent e) {
        Ticket selectedTicket = this.answeredTicketsList.getSelectedValue();
        if (selectedTicket != null) {
            this.ticketDescriptionTextArea.setText(selectedTicket.getDescription());
            try {
                Answer answer = RemoteConnection.getInstance().getViewAccessLogic().getAnswersOfTicket(selectedTicket, true).getFirst();
                this.answerTextArea.setText(answer.getAnswerString());
                this.solvedButton.setEnabled(true);
                this.notSolvedButton.setEnabled(true);
            } catch (MalformedURLException | NotBoundException | RemoteException ignored) {
                ignored.printStackTrace();
            }

        }
    }
}