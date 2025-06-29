package data.DTO;


import data.model.Customer;
import data.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDTO {

    private final Connection connection;

    public CustomerDTO(Connection connection) {
        this.connection = connection;
    }

    public Optional<Customer> getCustomerForLoginName(String loginName) {
        String sql = "SELECT * FROM Customer WHERE LoginName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, loginName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Customer(
                        resultSet.getLong("CustomerID"),
                        resultSet.getString("LoginName"),
                        resultSet.getString("Password")
                ));
            }

        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
        return Optional.empty();
    }

}
