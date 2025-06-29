package data.DAO;


import data.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAO {

    private final Connection connection;

    public EmployeeDAO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Employee> getEmployeeForEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM Employee WHERE EmployeeId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Employee(
                        resultSet.getLong("EmployeeID"),
                        resultSet.getString("LoginName"),
                        resultSet.getString("Password"),
                        resultSet.getLong("StrikeCount")
                ));
            }
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    public Optional<Employee> getEmployeeForLoginName(String loginName) {
        String sql = "SELECT * FROM Employee WHERE LoginName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Employee(
                        resultSet.getLong("EmployeeID"),
                        resultSet.getString("LoginName"),
                        resultSet.getString("Password"),
                        resultSet.getLong("StrikeCount")
                ));
            }
        } catch (SQLException ignored) {
        }
        return Optional.empty();
    }

    /*
     * A function to get all employees sorted by the amount of tickets they have that are
     * not closed ordered by the amount asc
     */
    public List<Employee> getEmployeesSorted() {

        List<Employee> employees = new ArrayList<>();
        String sql = """
                SELECT
                    e.EmployeeID,
                    e.LoginName,
                    e.Password,
                    e.StrikeCount,
                    COUNT(t.TicketID) AS OpenTicketCount
                FROM
                    Employee e
                LEFT JOIN
                    Ticket t ON e.EmployeeID = t.EmployeeID AND t.State != 'CLOSED'
                GROUP BY
                    e.EmployeeID, e.LoginName, e.Password, e.StrikeCount
                ORDER BY
                    OpenTicketCount DESC;
                """;
        try (Statement statement = connection.createStatement()) {

            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("EmployeeID"),
                        resultSet.getString("LoginName"),
                        resultSet.getString("Password"),
                        resultSet.getLong("StrikeCount")
                ));
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

        return employees;
    }

    public void updateEmployee(Employee employee) {
        String updateTicketSQL = """
                    UPDATE Employee
                    SET LoginName = ?, Password = ?, StrikeCount = ?
                    WHERE EmployeeId = ?
                """;

        try (PreparedStatement updateStmt = connection.prepareStatement(updateTicketSQL)) {
            // 1. Setzen des neuen Status, der EmployeeID und des AssignmentDate

            updateStmt.setString(1, employee.getLoginName());
            updateStmt.setString(2, employee.getPassword());
            updateStmt.setLong(3, employee.getStrikeCount());
            updateStmt.setLong(4, employee.getEmployeeId());


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

}