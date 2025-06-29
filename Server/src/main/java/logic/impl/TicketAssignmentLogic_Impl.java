package logic.impl;

import data.DTO.EmployeeDTO;
import data.DTOs;
import data.model.Answer;
import data.model.Employee;
import data.model.Keyword;
import data.model.Ticket;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TicketAssignmentLogic_Impl extends UnicastRemoteObject implements logic.TicketAssignmentLogic {


    // UnicastRemoteObject specific stuff______________________________________

    public TicketAssignmentLogic_Impl() throws RemoteException {
    }

    protected TicketAssignmentLogic_Impl(int port) throws RemoteException {
        super(port);
    }

    protected TicketAssignmentLogic_Impl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    // Class specific stuff____________________________________________________

    @Override
    public void AssignNewTicketToEmployee(Ticket ticket, List<Keyword> keywords) throws RemoteException {
        EmployeeDTO employeeDTO = DTOs.getInstance().getEmployeeDTO();
        List<Employee> employeesSorted = employeeDTO.getEmployeesSorted();

        ticket.setEmployeeId(employeesSorted.getFirst().getEmployeeId());
        ticket.setState("PENDING");
        try {
            DTOs.getInstance().getTicketDTO().createTicket(ticket, keywords);
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

    }

    @Override
    public void assignTicketToCustomer(Ticket ticket, Answer answer) throws RemoteException {
        ticket.setState("ANSWERED");
        DTOs.getInstance().getTicketDTO().updateTicket(ticket);
        DTOs.getInstance().getAnswerDTO().createAnswer(answer);
    }

    @Override
    public void reAssignTicketToEmployee(Ticket ticket) throws RemoteException {

        ticket.setState("PENDING");

        List<Employee> employeesSorted = DTOs.getInstance().getEmployeeDTO().getEmployeesSorted();
        employeesSorted.removeIf(employee -> employee.getEmployeeId().equals(ticket.getEmployeeId()));
        ticket.setEmployeeId(employeesSorted.getFirst().getEmployeeId());
        ticket.setIssueDate(LocalDateTime.now());

        DTOs.getInstance().getTicketDTO().updateTicket(ticket);


    }

    @Override
    public void unassignTicked(Ticket ticket) throws RemoteException {
        ticket.setState("CLOSED");
        ticket.setAssignmentDate(LocalDateTime.now());
        DTOs.getInstance().getTicketDTO().updateTicket(ticket);
    }

    // private helper functions________________________________________________


}
