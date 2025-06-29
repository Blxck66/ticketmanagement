package data;

import data.DTO.*;

import java.sql.Connection;

/**
 * This is also a Singleton class just like the RemoteConnection class of Client
 * to centralize the access to the database through the DTOs.
 */
public class DTOs {

    // Singleton specific stuff________________________________________________
    private static DTOs instance;

    public static DTOs getInstance() {
        if (DTOs.instance == null) {
            DTOs.instance = new DTOs();
        }
        return DTOs.instance;
    }

    private DTOs() {
    }

    // Class specific stuff____________________________________________________

    private EmployeeDTO employeeDTO;
    private CustomerDTO customerDTO;
    private KeywordDTO keywordDTO;
    private TicketDTO ticketDTO;
    private AnswerDTO answerDTO;

    public void init(Connection connection) {
        this.employeeDTO = new EmployeeDTO(connection);
        this.customerDTO = new CustomerDTO(connection);
        this.keywordDTO = new KeywordDTO(connection);
        this.ticketDTO = new TicketDTO(connection);
        this.answerDTO = new AnswerDTO(connection);
    }

    public EmployeeDTO getEmployeeDTO() {
        return this.employeeDTO;
    }

    public CustomerDTO getCustomerDTO() {
        return this.customerDTO;
    }

    public KeywordDTO getKeywordDTO() {
        return this.keywordDTO;
    }

    public TicketDTO getTicketDTO() {
        return this.ticketDTO;
    }

    public AnswerDTO getAnswerDTO() {
        return answerDTO;
    }
}
