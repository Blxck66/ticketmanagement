package logic;

import data.model.Answer;
import data.model.Keyword;
import data.model.Ticket;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TicketAssignmentLogic extends Remote {

    public abstract void AssignNewTicketToEmployee(Ticket ticket, List<Keyword> keywords) throws RemoteException;

    public abstract void assignTicketToCustomer(Ticket ticket, Answer answer) throws RemoteException;

    public abstract void reAssignTicketToEmployee(Ticket ticket) throws RemoteException;

    public abstract void unassignTicked(Ticket ticket) throws RemoteException;
}
