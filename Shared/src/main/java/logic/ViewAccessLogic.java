package logic;


import data.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ViewAccessLogic extends Remote {

    public abstract List<Keyword> getKeywords() throws RemoteException;

    public abstract List<Ticket> getAnsweredTicketsOfCustomer(Customer customer) throws RemoteException;

    public abstract List<Ticket> getPendingTicketsOfCustomer(Customer customer) throws RemoteException;

    public abstract List<Ticket> getExampleAnsweredTicketsForKeywords(List<Keyword> keywords) throws RemoteException;

    public abstract List<Answer> getAnswersOfTicket(Ticket ticket, boolean finalAnswer) throws RemoteException;

    public abstract List<Ticket> getPendingTicketsOfEmployee(Employee employee) throws RemoteException;


}