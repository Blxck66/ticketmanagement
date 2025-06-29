package data.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ticket implements Serializable {

    private Long ticketID;
    private String description;
    private String state;
    private Long CustomerId;
    private Long EmployeeId;
    private LocalDateTime IssueDate;
    private LocalDateTime AssignmentDate;


    public Ticket(String description, Long customerId) {
        this.description = description;
        CustomerId = customerId;
    }

    public Ticket(Long ticketID, String description, String state, Long customerId, Long employeeId, LocalDateTime issueDate, LocalDateTime assignmentDate) {
        this.ticketID = ticketID;
        this.description = description;
        this.state = state;
        CustomerId = customerId;
        EmployeeId = employeeId;
        IssueDate = issueDate;
        AssignmentDate = assignmentDate;
    }

    public Ticket() {

    }

    public Long getTicketID() {
        return ticketID;
    }

    public void setTicketID(Long ticketID) {
        this.ticketID = ticketID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Long customerId) {
        CustomerId = customerId;
    }

    public Long getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(Long employeeId) {
        EmployeeId = employeeId;
    }

    public LocalDateTime getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        IssueDate = issueDate;
    }

    public LocalDateTime getAssignmentDate() {
        return AssignmentDate;
    }

    public void setAssignmentDate(LocalDateTime assignmentDate) {
        AssignmentDate = assignmentDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(this.ticketID)
                .append("] - ");
        if (this.description != null) {
            sb.append(description, 0, Math.min(description.length(), 30));
        }
        return sb.toString();
    }
}