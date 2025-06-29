package logic;

import data.model.Customer;
import data.model.Employee;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationLogic extends Remote {

    public abstract Employee authenticateEmployee(String LoginName, String password) throws RemoteException;

    public abstract Customer authenticateCustomer(String LoginName, String password) throws RemoteException;

}
