package gui.panel.customer;

import data.model.Customer;
import data.model.Keyword;
import data.model.Ticket;
import gui.window.Mainframe;
import logic.RemoteConnection;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ReportProblemPanel extends JPanel {

    private static final String TITLE = "Problem melden";
    private static final Dimension MINIMUM_SIZE = new Dimension(600, 300);

    private final Mainframe mainframe;
    private final Customer customer;

    private JButton selectKeywordButton;
    private JButton unselectKeywordButton;
    private JButton nextButton;

    private DefaultListModel<Keyword> selectableKeywordListModel;
    private DefaultListModel<Keyword> selectedKeywordsListModel;

    private JList<Keyword> selectableKeywordList;
    private JList<Keyword> selectedKeywordsList;

    private JTextArea descriptionTextArea;

    public ReportProblemPanel(Mainframe mainframe, Customer customer) {
        this.mainframe = mainframe;
        this.customer = customer;
        this.mainframe.setMinimumSize(MINIMUM_SIZE);
        this.mainframe.setTitle(TITLE);
        try {
            this.initPanel();
        } catch (MalformedURLException | NotBoundException | RemoteException ignored) {
        }
    }

    private void initPanel() throws MalformedURLException, NotBoundException, RemoteException {
        // basic panel config__________________________________________________
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = GridBagConstraints.BOTH;
        setLayout(new GridBagLayout());


        // instructions________________________________________________________
        JLabel buttonInstructionsLabel = new JLabel("""
                <html>Wählen Sie aus der Liste links 3 Schlüsselwörter zu Ihrem Problem und bewegen Sie diese mithilfe der \"→\" Schaltfläche nach rechts.
                Bei einer falschen Auswahl, können Sie auch mithilfe der \"←\" Schaltfläche ein ausgewähltes Schlüsselwort entfernen.</html>
                """);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        this.add(buttonInstructionsLabel, gbc);


        // List of selectable keywords_________________________________________
        this.selectableKeywordListModel = new DefaultListModel<>();
        List<Keyword> keywords = RemoteConnection.getInstance().getViewAccessLogic().getKeywords();
        keywords.forEach(this.selectableKeywordListModel::addElement);

        this.selectableKeywordList = new JList<>(this.selectableKeywordListModel);
        this.selectableKeywordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane selectableKeywordsScroll = new JScrollPane(this.selectableKeywordList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(selectableKeywordsScroll, gbc);


        // buttons to move keywords____________________________________________
        JPanel moveKeywordButtonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        this.selectKeywordButton = new JButton("→");
        this.unselectKeywordButton = new JButton("←");
        unselectKeywordButton.setEnabled(false);
        selectKeywordButton.addActionListener(this::selectKeywordClicked);
        unselectKeywordButton.addActionListener(this::unselectKeywordClicked);
        moveKeywordButtonsPanel.add(selectKeywordButton);
        moveKeywordButtonsPanel.add(unselectKeywordButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        this.add(moveKeywordButtonsPanel, gbc);


        // list of selected keywords___________________________________________
        this.selectedKeywordsListModel = new DefaultListModel<>();

        this.selectedKeywordsList = new JList<>(this.selectedKeywordsListModel);

        this.selectedKeywordsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane selectedKeywordsScroll = new JScrollPane(this.selectedKeywordsList);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        this.add(selectedKeywordsScroll, gbc);


        // instructions________________________________________________________
        JLabel descriptionInstructionsLabel = new JLabel("""
                <html>Beschreiben Sie kurz Ihr Problem:</html>
                """);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        this.add(descriptionInstructionsLabel, gbc);


        // text area of user description_______________________________________
        this.descriptionTextArea = new JTextArea();
        this.descriptionTextArea.setLineWrap(true);
        this.descriptionTextArea.setWrapStyleWord(true);
        this.descriptionTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                descriptionTextAreaUpdated();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                descriptionTextAreaUpdated();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.gridheight = 2;
        this.add(this.descriptionTextArea, gbc);


        // button to turn back_________________________________________________
        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(this::backClicked);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(backButton, gbc);


        // button to submit ticket_____________________________________________
        this.nextButton = new JButton("Weiter");
        this.nextButton.addActionListener(this::nextClicked);
        this.nextButton.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(this.nextButton, gbc);


    }

    // button actions__________________________________________________________

    private void selectKeywordClicked(ActionEvent e) {
        Keyword selectedKeyword = this.selectableKeywordList.getSelectedValue();
        if (selectedKeyword != null) {
            this.selectedKeywordsListModel.addElement(selectedKeyword);
            this.selectableKeywordListModel.removeElement(selectedKeyword);

            if (this.selectedKeywordsListModel.size() == 3) {
                this.selectKeywordButton.setEnabled(false);
                if (!this.descriptionTextArea.getText().isEmpty()) {
                    this.nextButton.setEnabled(true);
                }
            }
            unselectKeywordButton.setEnabled(true);
        }

    }

    private void unselectKeywordClicked(ActionEvent e) {
        Keyword selectedKeyword = this.selectedKeywordsList.getSelectedValue();
        if (selectedKeyword != null) {
            selectableKeywordListModel.addElement(selectedKeyword);
            selectedKeywordsListModel.removeElement(selectedKeyword);
            if (selectedKeywordsListModel.size() < 3) {
                selectKeywordButton.setEnabled(true);
                nextButton.setEnabled(false);
            }
            if (selectedKeywordsListModel.isEmpty()) {
                unselectKeywordButton.setEnabled(false);
            }
        }
    }

    private void backClicked(ActionEvent e) {
        mainframe.switchToOverviewPanel(customer);
    }

    private void nextClicked(ActionEvent e) {
        List<Keyword> currentSelectedKeywords = new ArrayList();
        selectedKeywordsListModel.elements().asIterator().forEachRemaining(currentSelectedKeywords::add);
        this.mainframe.switchToExampleAnswersPanel(
                new Ticket(this.descriptionTextArea.getText(), this.customer.getCustomerId()),
                this.customer,
                currentSelectedKeywords);
    }


    // Text area has changed actions __________________________________________
    private void descriptionTextAreaUpdated() {
        if (selectedKeywordsListModel.size() == 3 && !descriptionTextArea.getText().isEmpty()) {
            nextButton.setEnabled(true);
        }
        if (descriptionTextArea.getText().isEmpty()) {
            nextButton.setEnabled(false);
        }
    }
}