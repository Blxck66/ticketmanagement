package gui.window;

import data.model.Customer;
import data.model.Keyword;
import data.model.Ticket;
import gui.panel.LoginPanel;
import gui.panel.customer.ExampleAnswersPanel;
import gui.panel.customer.OverviewPanel;
import gui.panel.customer.ReportProblemPanel;
import gui.panel.customer.ShowTicketsPanel;
import gui.panel.employee.EmployeePanel;

import javax.swing.*;
import java.util.List;


public class Mainframe extends JFrame {

    private LoginPanel loginPanel;
    private OverviewPanel overviewPanel;
    private EmployeePanel employeePanel;
    private ShowTicketsPanel showTicketsPanel;
    private ReportProblemPanel reportProblemPanel;
    private ExampleAnswersPanel exampleAnswersPanel;

    public Mainframe() {
        this.startup();
    }

    private void startup() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        switchToLoginPanel();
        this.setVisible(true);
    }

    // panel switches__________________________________________________________
    /*
     * Panel switches are called from panels. The panel switches are called
     * if the current panel (containing content) of the window
     * shall be switched.
     */

    public void switchToLoginPanel() {
        this.overviewPanel = null;

        this.loginPanel = new LoginPanel(this);
        this.setContentPane(loginPanel);
        this.pack();
    }

    public void switchToOverviewPanel(Customer customer) {
        this.loginPanel = null;
        this.reportProblemPanel = null;
        this.showTicketsPanel = null;
        this.exampleAnswersPanel = null;

        this.overviewPanel = new OverviewPanel(this, customer);
        this.setContentPane(overviewPanel);
        this.pack();
    }

    public void switchToReportProblemPanel(Customer customer) {
        this.overviewPanel = null;
        this.exampleAnswersPanel = null;

        this.reportProblemPanel = new ReportProblemPanel(this, customer);
        this.setContentPane(reportProblemPanel);
        this.pack();
    }

    public void switchToShowTicketsPanel(Customer customer) {
        this.overviewPanel = null;

        this.showTicketsPanel = new ShowTicketsPanel(this, customer);
        this.setContentPane(showTicketsPanel);
        this.pack();
    }

    public void switchToExampleAnswersPanel(Ticket ticket, Customer customer, List<Keyword> keywords) {
         this.reportProblemPanel = null;

        this.exampleAnswersPanel = new ExampleAnswersPanel(this, ticket, customer, keywords);
        this.setContentPane(exampleAnswersPanel);
        this.pack();
    }
}