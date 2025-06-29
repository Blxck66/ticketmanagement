package data.DTO;


import data.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDTO {

    private final Connection connection;

    public EmployeeDTO(Connection connection) {
        this.connection = connection;
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

}