package data.DTO;

import data.model.Answer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDTO {

    private final Connection connection;

    public AnswerDTO(Connection connection) {
        this.connection = connection;
    }


    public void createAnswer(Answer answer) {
        String updateFinalAnswerSQL = """
                    UPDATE Answer
                    SET IsFinal = FALSE
                    WHERE TicketID = ? AND IsFinal = TRUE
                """;

        String insertAnswerSQL = """
                    INSERT INTO Answer (AnswerString, IsFinal, TicketID)
                    VALUES (?, ?, ?)
                """;

        try (PreparedStatement updateStmt = connection.prepareStatement(updateFinalAnswerSQL);
             PreparedStatement insertStmt = connection.prepareStatement(insertAnswerSQL)) {

            connection.setAutoCommit(false);

            // 1. Update the existing answer to set IsFinal to FALSE
            updateStmt.setLong(1, answer.getTicketId());
            updateStmt.executeUpdate();

            // 2. Insert the new answer as a final answer
            insertStmt.setString(1, answer.getAnswerString());
            insertStmt.setBoolean(2, true);  // New answer is final
            insertStmt.setLong(3, answer.getTicketId());

            insertStmt.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List<Answer> getAnswerByTicketId(long ticketId, boolean finalAnswer) {
        String selectAnswerSQL = """
                    SELECT AnswerID, AnswerString, IsFinal, TicketID
                    FROM Answer
                    WHERE TicketID = ? AND IsFinal = 
                """;
        selectAnswerSQL += finalAnswer ? "TRUE" : "FALSE";

        List<Answer> answers = new ArrayList<>();

        try (PreparedStatement selectStmt = connection.prepareStatement(selectAnswerSQL)) {
            selectStmt.setLong(1, ticketId);  // Setzen der TicketID für das gewünschte Ticket

            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    answers.add(new Answer(
                            rs.getLong("AnswerID"),
                            rs.getString("AnswerString"),
                            rs.getBoolean("isFinal"),
                            rs.getLong("TicketID")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

}
