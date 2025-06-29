package data;

import data.DAO.*;

import java.sql.Connection;

/**
 * This is also a Singleton class just like the RemoteConnection class of Client
 * to centralize the access to the database through the DAOs.
 */
public class DAOs {

    // Singleton specific stuff________________________________________________
    private static DAOs instance;

    public static DAOs getInstance() {
        if (DAOs.instance == null) {
            DAOs.instance = new DAOs();
        }
        return DAOs.instance;
    }

    private DAOs() {
    }

    // Class specific stuff____________________________________________________

    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;
    private KeywordDAO keywordDAO;
    private TicketDAO ticketDAO;
    private AnswerDAO answerDAO;

    public void init(Connection connection) {
        this.employeeDAO = new EmployeeDAO(connection);
        this.customerDAO = new CustomerDAO(connection);
        this.keywordDAO = new KeywordDAO(connection);
        this.ticketDAO = new TicketDAO(connection);
        this.answerDAO = new AnswerDAO(connection);
    }

    public EmployeeDAO getEmployeeDAO() {
        return this.employeeDAO;
    }

    public CustomerDAO getCustomerDAO() {
        return this.customerDAO;
    }

    public KeywordDAO getKeywordDAO() {
        return this.keywordDAO;
    }

    public TicketDAO getTicketDAO() {
        return this.ticketDAO;
    }

    public AnswerDAO getAnswerDAO() {
        return answerDAO;
    }
}
