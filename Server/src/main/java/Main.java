import data.DAO.EmployeeDAO;
import data.DAO.TicketDAO;
import data.DAOs;
import data.model.Employee;
import data.model.Ticket;
import logic.AuthenticationLogic;
import logic.TicketAssignmentLogic;
import logic.ViewAccessLogic;
import logic.impl.AuthenticationLogic_Impl;
import logic.impl.TicketAssignmentLogic_Impl;
import logic.impl.ViewAccessLogic_Impl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/ticketmanagement";

    // The driver is included into the project thorough maven.
    private static final String DB_DRIVER = "org.mariadb.jdbc.Driver";

    //This user was created beforehand (by default) when the database was created.
    private static final String DB_USER = "root";

    //If the port is changed here in server, it needs also be changed in client.
    private static final int REGISTRY_PORT = 1099;

    private static TicketAssignmentLogic_Impl ticketAssignmentLogic;

    public static void main(String[] args) throws ClassNotFoundException {

        /*
         * Loads the jdbc driver on runtime and registers it for the DriverManager.
         * The jdbc driver must be registered in the driverManager to build a
         * connection to the database through it.
         *
         */
        Class.forName(DB_DRIVER);

        /*
         * Try with resources because Connection extends AutoClosable.
         *
         * YouTube link about AutoClosable:
         * https://www.youtube.com/watch?v=MtOvbhIMHJQ
         *
         * Baeldung link about AutoClosable:
         * https://www.baeldung.com/java-try-with-resources
         */
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, "")) {
            System.out.println("Database connection build");


            Registry reg = LocateRegistry.createRegistry(REGISTRY_PORT);
            System.out.println("Registry build");
            initRegistry(reg);
            System.out.println("Registry initialized");

            DAOs.getInstance().init(con);
            System.out.println("DAOs initialized");

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> dealStrikes(), 0, 15, TimeUnit.MINUTES);
            System.out.println("Scheduler for dealing strikes configured.");

            Thread.currentThread().join();

        } catch (SQLException e) {
            System.out.println("Failed to build the connection to database!");
            throw new RuntimeException(e);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to build the registry!");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("Unexpected thread error!");
            throw new RuntimeException(e);
        }
    }

    private static void initRegistry(Registry reg) throws AlreadyBoundException, RemoteException {
        reg.bind(Arrays.asList(AuthenticationLogic.class.getName().split("\\.")).getLast(),
                new AuthenticationLogic_Impl());

        reg.bind(Arrays.asList(ViewAccessLogic.class.getName().split("\\.")).getLast(),
                new ViewAccessLogic_Impl());

        ticketAssignmentLogic = new TicketAssignmentLogic_Impl();
        reg.bind(Arrays.asList(TicketAssignmentLogic.class.getName().split("\\.")).getLast(),
                ticketAssignmentLogic);
    }

    private static void dealStrikes() {

        TicketDAO ticketDAO = DAOs.getInstance().getTicketDAO();
        List<Ticket> allPendingTickets = ticketDAO.getAllPendingTickets();
        List<Ticket> expiredTickets = allPendingTickets.stream().filter(ticket -> ticket.getAssignmentDate().isBefore(
                LocalDateTime.now().minusDays(3))).toList();

        EmployeeDAO employeeDAO = DAOs.getInstance().getEmployeeDAO();


        for (Ticket expiredTicket : expiredTickets) {
            try {
                ticketAssignmentLogic.reAssignTicketToEmployee(expiredTicket);
                Employee updatedEmployee = employeeDAO.getEmployeeForEmployeeId(expiredTicket.getEmployeeId()).get();
                updatedEmployee.setStrikeCount(updatedEmployee.getStrikeCount() + 1);
                employeeDAO.updateEmployee(updatedEmployee);
                ticketDAO.updateTicket(expiredTicket);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    }


}