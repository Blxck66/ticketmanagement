package data.DAO;

import data.model.Keyword;
import data.model.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private final Connection connection;

    public TicketDAO(Connection connection) {
        this.connection = connection;
    }

    public void createTicket(Ticket ticket, List<Keyword> keywords) throws SQLException {
        String insertTicketSQL = """
                    INSERT INTO Ticket (Description, IssueDate, State, CustomerID, EmployeeID, AssignmentDate)
                    VALUES (?, ?, ?, ?, ?, ?)
                """;
        String insertKeywordSQL = "INSERT INTO Ticket_Keyword (TicketID, KeywordID) VALUES (?, ?)";

        connection.setAutoCommit(false);

        try (PreparedStatement ticketStmt = connection.prepareStatement(insertTicketSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement keywordStmt = connection.prepareStatement(insertKeywordSQL)) {
            // 1. create the ticked in the database
            ticketStmt.setString(1, ticket.getDescription());
            ticketStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ticketStmt.setString(3, ticket.getState());
            ticketStmt.setLong(4, ticket.getCustomerId());
            ticketStmt.setLong(5, ticket.getEmployeeId());
            ticketStmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            ticketStmt.executeUpdate();

            // 2. get the ticket that was created
            ResultSet rs = ticketStmt.getGeneratedKeys();
            if (rs.next()) {
                Long ticketId = rs.getLong(1);
                ticket.setTicketID(ticketId);
            } else {
                throw new SQLException("Fehler beim Erzeugen der Ticket-ID.");
            }

            // 3. create the keyword mappings.
            for (Keyword keyword : keywords) {
                keywordStmt.setLong(1, ticket.getTicketID());
                keywordStmt.setLong(2, keyword.getKeywordId());
                keywordStmt.addBatch();
            }
            keywordStmt.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Ticket> getAllPendingTickets() {
        String selectTicketsSQL = """
                    SELECT TicketID, Description, IssueDate, State, CustomerID, EmployeeID, AssignmentDate
                    FROM Ticket
                    WHERE State = 'PENDING'
                """;

        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectTicketsSQL)) {

            try (ResultSet rs = selectStmt.executeQuery()) {
                // 1. Durch die ResultSet iterieren und Ticket-Objekte erstellen
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(rs.getLong("TicketID"));
                    ticket.setDescription(rs.getString("Description"));
                    ticket.setIssueDate(rs.getTimestamp("IssueDate").toLocalDateTime());
                    ticket.setState(rs.getString("State"));
                    ticket.setCustomerId(rs.getLong("CustomerID"));
                    ticket.setEmployeeId(rs.getLong("EmployeeID"));
                    ticket.setAssignmentDate(rs.getTimestamp("AssignmentDate").toLocalDateTime());

                    // Ticket zur Liste hinzufügen
                    ticketList.add(ticket);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    public void updateTicket(Ticket ticket) {
        String updateTicketSQL = """
                    UPDATE Ticket
                    SET State = ?, EmployeeID = ?, AssignmentDate = ?
                    WHERE TicketID = ?
                """;

        try (PreparedStatement updateStmt = connection.prepareStatement(updateTicketSQL)) {
            // 1. Setzen des neuen Status, der EmployeeID und des AssignmentDate
            updateStmt.setString(1, ticket.getState());
            updateStmt.setLong(2, ticket.getEmployeeId());
            updateStmt.setTimestamp(3, Timestamp.valueOf(ticket.getAssignmentDate()));

            updateStmt.setLong(4, ticket.getTicketID());  // TicketID, um das richtige Ticket zu finden

            // 2. Update ausführen
            updateStmt.executeUpdate();
            connection.commit();

        } catch (SQLException ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public List<Ticket> getAnsweredTicketsByCustomer(long customerId) {
        String selectTicketsSQL = """
                    SELECT TicketID, Description, IssueDate, State, CustomerID, EmployeeID, AssignmentDate
                    FROM Ticket
                    WHERE CustomerID = ?
                    AND State = 'ANSWERED'
                """;

        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectTicketsSQL)) {
            selectStmt.setLong(1, customerId);  // Setzen der CustomerID, um Tickets des spezifischen Kunden zu bekommen

            try (ResultSet rs = selectStmt.executeQuery()) {
                // 1. Durch die ResultSet iterieren und Ticket-Objekte erstellen
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(rs.getLong("TicketID"));
                    ticket.setDescription(rs.getString("Description"));
                    ticket.setIssueDate(rs.getTimestamp("IssueDate").toLocalDateTime());
                    ticket.setState(rs.getString("State"));
                    ticket.setCustomerId(rs.getLong("CustomerID"));
                    ticket.setEmployeeId(rs.getLong("EmployeeID"));
                    ticket.setAssignmentDate(rs.getTimestamp("AssignmentDate").toLocalDateTime());

                    // Ticket zur Liste hinzufügen
                    ticketList.add(ticket);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    public List<Ticket> getPendingTicketsByCustomer(long customerId) {
        String selectTicketsSQL = """
                    SELECT TicketID, Description, IssueDate, State, CustomerID, EmployeeID, AssignmentDate
                    FROM Ticket
                    WHERE CustomerID = ?
                    AND State = 'PENDING'
                """;

        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectTicketsSQL)) {
            selectStmt.setLong(1, customerId);  // Setzen der CustomerID, um Tickets des spezifischen Kunden zu bekommen

            try (ResultSet rs = selectStmt.executeQuery()) {
                // 1. Durch die ResultSet iterieren und Ticket-Objekte erstellen
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(rs.getLong("TicketID"));
                    ticket.setDescription(rs.getString("Description"));
                    ticket.setIssueDate(rs.getTimestamp("IssueDate").toLocalDateTime());
                    ticket.setState(rs.getString("State"));
                    ticket.setCustomerId(rs.getLong("CustomerID"));
                    ticket.setEmployeeId(rs.getLong("EmployeeID"));
                    ticket.setAssignmentDate(rs.getTimestamp("AssignmentDate").toLocalDateTime());

                    // Ticket zur Liste hinzufügen
                    ticketList.add(ticket);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    public List<Ticket> getPendingTicketsByEmployee(long employeeId) {
        String selectTicketsSQL = """
                    SELECT TicketID, Description, IssueDate, State, CustomerID, EmployeeID, AssignmentDate
                    FROM Ticket
                    WHERE EmployeeID = ?
                    AND State = 'PENDING'
                """;

        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectTicketsSQL)) {
            selectStmt.setLong(1, employeeId);  // Setzen der CustomerID, um Tickets des spezifischen Kunden zu bekommen

            try (ResultSet rs = selectStmt.executeQuery()) {
                // 1. Durch die ResultSet iterieren und Ticket-Objekte erstellen
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(rs.getLong("TicketID"));
                    ticket.setDescription(rs.getString("Description"));
                    ticket.setIssueDate(rs.getTimestamp("IssueDate").toLocalDateTime());
                    ticket.setState(rs.getString("State"));
                    ticket.setCustomerId(rs.getLong("CustomerID"));
                    ticket.setEmployeeId(rs.getLong("EmployeeID"));
                    ticket.setAssignmentDate(rs.getTimestamp("AssignmentDate").toLocalDateTime());

                    // Ticket zur Liste hinzufügen
                    ticketList.add(ticket);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }

    public List<Ticket> getTicketsByKeywords(List<Keyword> keywords) {
        // Wenn keine Keywords übergeben wurden, werfen wir eine Exception oder führen eine leere Abfrage aus
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Die Liste der Keywords darf nicht leer sein.");
        }

        // Erstelle eine Liste der Keyword-IDs
        List<Long> keywordIds = new ArrayList<>();
        for (Keyword keyword : keywords) {
            keywordIds.add(keyword.getKeywordId());
        }

        // Baue die SQL-Abfrage mit einer IN-Klausel
        StringBuilder sqlBuilder = new StringBuilder("""
                SELECT DISTINCT t.TicketID, t.Description, t.IssueDate, t.State, t.CustomerID, t.EmployeeID, t.AssignmentDate
                FROM Ticket t
                JOIN Ticket_Keyword tk ON t.TicketID = tk.TicketID
                WHERE tk.KeywordID IN (""");

        // Füge alle Keyword-IDs in die IN-Klausel ein
        for (int i = 0; i < keywordIds.size(); i++) {
            sqlBuilder.append("?");
            if (i < keywordIds.size() - 1) {
                sqlBuilder.append(", ");
            }
        }
        sqlBuilder.append(")");

        // Vorbereiten der SQL-Abfrage
        String selectTicketsSQL = sqlBuilder.toString();

        List<Ticket> ticketList = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectTicketsSQL)) {
            // Setzen der Keyword-IDs als Parameter
            for (int i = 0; i < keywordIds.size(); i++) {
                selectStmt.setLong(i + 1, keywordIds.get(i));  // Setzen der Keyword-ID in der IN-Klausel
            }

            // Ausführen der Abfrage
            try (ResultSet rs = selectStmt.executeQuery()) {
                // Durch das ResultSet iterieren und Tickets erstellen
                while (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketID(rs.getLong("TicketID"));
                    ticket.setDescription(rs.getString("Description"));
                    ticket.setIssueDate(rs.getTimestamp("IssueDate").toLocalDateTime());
                    ticket.setState(rs.getString("State"));
                    ticket.setCustomerId(rs.getLong("CustomerID"));
                    ticket.setEmployeeId(rs.getLong("EmployeeID"));
                    ticket.setAssignmentDate(rs.getTimestamp("AssignmentDate").toLocalDateTime());

                    // Ticket zur Liste hinzufügen
                    ticketList.add(ticket);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ticketList;
    }


}