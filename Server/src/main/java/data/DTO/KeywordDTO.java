package data.DTO;

import data.model.Keyword;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KeywordDTO {

    private final Connection connection;

    public KeywordDTO(Connection connection) {
        this.connection = connection;
    }

    public List<Keyword> getAll() {
        List<Keyword> keywords = new ArrayList<Keyword>();

        try (Statement statement = connection.createStatement()) {
            statement.execute("SELECT * FROM Keyword");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                keywords.add(new Keyword(
                        resultSet.getLong("KeywordId"),
                        resultSet.getString("Keyword")));
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }

        return keywords;
    }
}
