package gui.panel.customer;

import data.model.Customer;
import data.model.Keyword;
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

public class ExampleAnswersPanel extends JPanel {

    private static final String TITLE = "Beispielantworten";
    private static final Dimension MINIMUM_SIZE = new Dimension(300, 150);

    private final Mainframe mainframe;
    private final Ticket ticket;
    private final Customer customer;
    private final List<Keyword> keywords;

    private JList<Ticket> ticketJList;
    private JTextArea ticketDescriptionTextArea;

    public ExampleAnswersPanel(Mainframe mainframe, Ticket ticket, Customer customer, List<Keyword> keywords) {
        this.mainframe = mainframe;
        this.ticket = ticket;
        this.customer = customer;
        this.keywords = keywords;
        mainframe.setMinimumSize(MINIMUM_SIZE);
        mainframe.setTitle(TITLE);
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
        gbc.weightx = GridBagConstraints.BOTH;
        this.setLayout(new GridBagLayout());

        JLabel questionLabel = new JLabel("""
                <html>Löst eines dieser Antworten Ihr Problem?</html>
                """);
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(questionLabel, gbc);


        DefaultListModel<Ticket> ticketListModel = new DefaultListModel<>();
        List<Ticket> exampleAnsweredTicketsForKeywords = RemoteConnection.getInstance().getViewAccessLogic().getExampleAnsweredTicketsForKeywords(this.keywords);
        exampleAnsweredTicketsForKeywords.forEach(ticketListModel::addElement);
        ticketJList = new JList<>(ticketListModel);
        ticketJList.addListSelectionListener(this::ticketSelected);
        ticketJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane ticketsScroll = new JScrollPane(ticketJList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(ticketsScroll, gbc);


        ticketDescriptionTextArea = new JTextArea();
        ticketDescriptionTextArea.setLineWrap(true);
        ticketDescriptionTextArea.setWrapStyleWord(true);
        ticketDescriptionTextArea.setEditable(false);
        JScrollPane ticketDescriptionScroll = new JScrollPane(ticketDescriptionTextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(ticketDescriptionScroll, gbc);


        JButton backButton = new JButton("Ja (zurück)");
        backButton.addActionListener(this::backButtonClicked);
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(backButton, gbc);

        JButton nextButton = new JButton("Nein (weiter)");
        nextButton.addActionListener(this::nextButtonClicked);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(nextButton, gbc);
    }

    private void backButtonClicked(ActionEvent e) {
        this.mainframe.switchToReportProblemPanel(customer);
    }

    private void ticketSelected(ListSelectionEvent e) {
        Ticket selectedTicket = ticketJList.getSelectedValue();
        if (selectedTicket != null) {
            ticketDescriptionTextArea.setText(selectedTicket.getDescription());
        }
    }

    private void nextButtonClicked(ActionEvent e) {
        try {
            RemoteConnection.getInstance()
                    .getTicketAssignmentLogic()
                    .AssignNewTicketToEmployee(ticket, keywords);

            new JOptionPane("Ein Ticket wurde für sie automatisch erstellt.",
                    JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    new Object[]{new JButton("OK") {{
                        addActionListener(e -> {
                            mainframe.switchToOverviewPanel(customer);
                            SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
                        });
                    }}},
                    null)
                    .createDialog("Hinweis")
                    .setVisible(true);

        } catch (RemoteException | MalformedURLException | NotBoundException ignored) {
            ignored.printStackTrace();
        }

    }
}