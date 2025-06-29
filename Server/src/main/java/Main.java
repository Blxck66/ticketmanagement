import data.DTOs;
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
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/ticketmanagement";

    // The driver is included into the project thorough maven.
    private static final String DB_DRIVER = "org.mariadb.jdbc.Driver";

    //This user was created beforehand (by default) when the database was created.
    private static final String DB_USER = "root";

    //If the port is changed here in server, it needs also be changed in client.
    private static final int REGISTRY_PORT = 1099;

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

            DTOs.getInstance().init(con);
            System.out.println("DTOs initialized");

            System.out.println("Enter \"exit\" to stop the server.");
            Scanner scanner = new Scanner(System.in);

            while (!scanner.nextLine().equals("exit")) {
            }

        } catch (SQLException e) {
            System.out.println("Failed to build the connection to database!");
            throw new RuntimeException(e);
        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Failed to build the registry!");
            throw new RuntimeException(e);
        }
    }

    private static void initRegistry(Registry reg) throws AlreadyBoundException, RemoteException {
        reg.bind(Arrays.asList(AuthenticationLogic.class.getName().split("\\.")).getLast(),
                new AuthenticationLogic_Impl());

        reg.bind(Arrays.asList(ViewAccessLogic.class.getName().split("\\.")).getLast(),
                new ViewAccessLogic_Impl());

        reg.bind(Arrays.asList(TicketAssignmentLogic.class.getName().split("\\.")).getLast(),
                new TicketAssignmentLogic_Impl());
    }


}