package logic.impl;

import data.DAOs;
import data.model.Customer;
import data.model.Employee;
import logic.AuthenticationLogic;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

public class AuthenticationLogic_Impl extends UnicastRemoteObject implements AuthenticationLogic {

    // UnicastRemoteObject specific stuff______________________________________
    public AuthenticationLogic_Impl() throws RemoteException {
    }

    protected AuthenticationLogic_Impl(int port) throws RemoteException {
        super(port);
    }

    protected AuthenticationLogic_Impl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    // Class specific stuff____________________________________________________

    @Override
    public Employee authenticateEmployee(String LoginName, String password) throws RemoteException {
        Optional<Employee> oEmployee = DAOs.getInstance().getEmployeeDAO().getEmployeeForLoginName(LoginName);
        if (oEmployee.isPresent() && oEmployee.get().getPassword().equals(password)) {
            return oEmployee.get();
        }
        return null;
    }

    @Override
    public Customer authenticateCustomer(String loginName, String password) throws RemoteException {
        Optional<Customer> oCustomer = DAOs.getInstance().getCustomerDAO().getCustomerForLoginName(loginName);
        if (oCustomer.isPresent() && oCustomer.get().getPassword().equals(password)) {
            return oCustomer.get();
        }
        return null;
    }
}
