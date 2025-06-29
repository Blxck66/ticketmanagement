package logic;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

/*
 * This is a Singleton class. If you don't know what
 * a singleton class is, look it up:
 *
 * YouTube:
 * https://www.youtube.com/watch?v=3cJbDs-zzpw
 * Baeldung:
 * https://www.baeldung.com/java-singleton
 *
 * This Class is a client specific class and has nothing to do with the server directly.
 * However, this Class initializes all the server calls and makes them globally accessible.
 * The reason we do that is, because we don't want to call the server from the client gui directly because
 * that would lead to confusing server calls from everywhere.
 * Instead, the GUI shall make all its calls to the server through this central point.
 *
 * For example if the prof asks you "Kollega wie authentifizierst du den Login?"
 * you can simply answer:
 * "Das Loginpanel (hier das Blatt rausholen mit dem LoginPanel) ruft die AuthenticationLogic von der globalen
 * Instanz dieser Klasse (hier das Blatt mit RemoteConnection rausholen) auf, wo eine Verbindung zwischen
 * dem Interface AuthenticationLogic im Client und die Implementierung des interfaces im Server (Hier das
 * Blatt mit AuthenticationLogic_Impl rausholen) bereits bei Start des Clients initialisiert wird."
 *
 * The Prof could maybe ask you something like "Was Meinen Sie mit globale Instanz und wie schaffen Sie denn
 * die Verbindung zwischen einem Interface und eine Implementation des Interfaces im Server?"
 *
 * You can then answer something like:
 * "Das Interface AuthenticationLogic gibt es doppelt. Einmal im Client und einmal exakt gleich im Server. Der Server
 * jedoch hat alleine die Implementierung des Interfaces. Die Implementierung wird, wie wir es in den Vorlesungen
 * entsprechend des Skriptes gemacht hatten via java.rmi, also java remote method invocation aufgerufen, wie man hier
 * sehen kann (hier auf dem Blatt RemoteConnection auf die init() Funktion zeigen). Dies ist möglich dadurch, dass
 * beide Interfaces Java.Remote erweitern und die Implementierung des Interfaces
 * (Hier blatt mit AuthenticationLogic_Impl rausholen) die Klasse UnicastRemoteObject erweitert und in einer Registry,
 * welches vom Server als auch vom Client verwendet wird vom Server registriert worden ist (Hier Blatt mit Server.Main
 * rausholen)."
 *
 * -> The text above maybe will sound complex at the beginning, but its actually really simple once you've
 * understood how the concept of java.rmi works.
 *
 */
public class RemoteConnection {

    // Singleton specific stuff________________________________________________
    private static RemoteConnection instance;

    public static RemoteConnection getInstance() throws MalformedURLException, NotBoundException, RemoteException {
        if (RemoteConnection.instance == null) {
            RemoteConnection.instance = new RemoteConnection();
        }
        return RemoteConnection.instance;
    }

    private RemoteConnection() throws MalformedURLException, NotBoundException, RemoteException {
        init();
    }

    // Class specific stuff____________________________________________________

    private static final int REGISTRY_PORT = 1099;
    private static final String REGISTRY_HOST = "localhost";

    private AuthenticationLogic authLogic;
    private ViewAccessLogic viewAccessLogic;
    private TicketAssignmentLogic ticketAssignmentLogic;

    private void init() throws MalformedURLException, NotBoundException, RemoteException {
        String REGISTRY_BASE_URL = String.format("rmi://%s:%d/", REGISTRY_HOST, REGISTRY_PORT);

        this.authLogic = (AuthenticationLogic) Naming.lookup(REGISTRY_BASE_URL
                + Arrays.asList(AuthenticationLogic.class.getName().split("\\.")).getLast());

        this.viewAccessLogic = (ViewAccessLogic) Naming.lookup(REGISTRY_BASE_URL
                + Arrays.asList(ViewAccessLogic.class.getName().split("\\.")).getLast());

        this.ticketAssignmentLogic = (TicketAssignmentLogic) Naming.lookup(REGISTRY_BASE_URL
                + Arrays.asList(TicketAssignmentLogic.class.getName().split("\\.")).getLast());
    }

    public AuthenticationLogic getAuthenticationLogic() {
        return this.authLogic;
    }

    public ViewAccessLogic getViewAccessLogic() {
        return this.viewAccessLogic;
    }

    public TicketAssignmentLogic getTicketAssignmentLogic() {
        return this.ticketAssignmentLogic;
    }

}
