package logic.impl;

import data.DTOs;
import data.model.*;
import logic.ViewAccessLogic;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ViewAccessLogic_Impl extends UnicastRemoteObject implements ViewAccessLogic {

    // UnicastRemoteObject specific stuff______________________________________
    public ViewAccessLogic_Impl() throws RemoteException {
    }

    protected ViewAccessLogic_Impl(int port) throws RemoteException {
        super(port);
    }

    protected ViewAccessLogic_Impl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    // Class specific stuff____________________________________________________


    @Override
    public List<Keyword> getKeywords() {
        return DTOs.getInstance().getKeywordDTO().getAll();
    }

    @Override
    public List<Ticket> getAnsweredTicketsOfCustomer(Customer customer) {
        return DTOs.getInstance().getTicketDTO().getAnsweredTicketsByCustomer(customer.getCustomerId());
    }

    @Override
    public List<Ticket> getPendingTicketsOfCustomer(Customer customer) {
        return DTOs.getInstance().getTicketDTO().getPendingTicketsByCustomer(customer.getCustomerId());
    }

    @Override
    public List<Ticket> getExampleAnsweredTicketsForKeywords(List<Keyword> keywords) {
        return DTOs.getInstance().getTicketDTO().getTicketsByKeywords(keywords);
    }

    @Override
    public List<Answer> getAnswersOfTicket(Ticket ticket, boolean finalAnswer) {
        return DTOs.getInstance().getAnswerDTO().getAnswerByTicketId(ticket.getTicketID(), finalAnswer);
    }

    @Override
    public List<Ticket> getPendingTicketsOfEmployee(Employee employee) throws RemoteException {
        return DTOs.getInstance().getTicketDTO().getPendingTicketsByEmployee(employee.getEmployeeId());
    }
}
