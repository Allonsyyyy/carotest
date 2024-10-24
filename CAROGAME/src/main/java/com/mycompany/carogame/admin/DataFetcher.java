package com.mycompany.carogame.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataFetcher {

    
    public int getNewUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE created_at > NOW() - INTERVAL 1 MONTH";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTotalUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM Users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTotalMatches() throws SQLException {
        String query = "SELECT COUNT(*) FROM Matches";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public ObservableList<Match> getMatchData() throws SQLException {
        String query = "SELECT match_id, player1_id, player2_id, start_time, end_time, winner_id FROM Matches";
        ObservableList<Match> matches = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String matchId = rs.getString("match_id");
                String player1Id = rs.getString("player1_id");
                String player2Id = rs.getString("player2_id");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String winnerId = rs.getString("winner_id");
                matches.add(new Match(matchId, player1Id, player2Id, startTime, endTime, winnerId));
            }
        }
        return matches;
    }
    
    public ObservableList<User> getUserData() throws SQLException {
        String query = "SELECT user_id, username, password, email, created_at FROM Users";
        ObservableList<User> users = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String userId = rs.getString("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String createdAt = rs.getString("created_at");
                users.add(new User(userId, username, password, email, createdAt));
            }
        }
        return users;
    }
    public ObservableList<MatchHistory> getMatchHistoryData() throws SQLException {
    String query = "SELECT history_id, match_id, player_id, move_number, move_time FROM MatchHistory";
    ObservableList<MatchHistory> matchHistories = FXCollections.observableArrayList();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String historyId = rs.getString("history_id");
            String matchId = rs.getString("match_id");
            String playerId = rs.getString("player_id");
            String moveNumber = rs.getString("move_number");
            String moveTime = rs.getString("move_time");

            matchHistories.add(new MatchHistory(historyId, matchId, playerId, moveNumber, moveTime));
        }
    } catch (SQLException e) {
        e.printStackTrace();  // Handle exception
        throw e;  // Rethrow exception to be handled by the caller
    }

    return matchHistories;
}
    
    
    public void deleteMatch(String matchId) throws SQLException {
        String deleteMatchHistoryQuery = "DELETE FROM MatchHistory WHERE match_id = ?";
        String deleteMatchQuery = "DELETE FROM Matches WHERE match_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);  // Begin transaction

            try (PreparedStatement stmt = conn.prepareStatement(deleteMatchHistoryQuery)) {
                stmt.setString(1, matchId);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteMatchQuery)) {
                stmt.setString(1, matchId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No match found with ID: " + matchId);
                }
            }

            conn.commit();  // Commit transaction
        } catch (SQLException e) {
            // Rollback in case of an error
            e.printStackTrace();
            throw e;
        }
    }

    public void updateMatch(String matchId, String player1Id, String player2Id, String startTime, String endTime, String winnerId) throws SQLException {
        String query = "UPDATE Matches SET player1_id = ?, player2_id = ?, start_time = ?, end_time = ?, winner_id = ? WHERE match_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, player1Id);
            stmt.setString(2, player2Id);
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            stmt.setString(5, winnerId);
            stmt.setString(6, matchId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No match found with ID: " + matchId);
            }
        }
    }

    public void deleteUser(String userId) throws SQLException {
    String deleteMatchHistoryQuery = "DELETE FROM MatchHistory WHERE player_id = ?";
    String deleteMatchesAsPlayer1Query = "DELETE FROM Matches WHERE player1_id = ?";
    String deleteMatchHistoryPlayer2Query = "DELETE FROM MatchHistory WHERE match_id IN (SELECT match_id FROM Matches WHERE player2_id = ?)";
    String deleteMatchesAsPlayer2Query = "DELETE FROM Matches WHERE player2_id = ?";
    String deleteMatchesAsWinnerQuery = "DELETE FROM Matches WHERE winner_id = ?";
    String deleteUserQuery = "DELETE FROM Users WHERE user_id = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);  // Begin transaction

        // Delete MatchHistory records where player_id is the user
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchHistoryQuery)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }

        // Delete Matches records where player1_id is the user
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchesAsPlayer1Query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }

        // Delete MatchHistory records where the user is player2 in matches
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchHistoryPlayer2Query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }

        // Delete Matches records where player2_id is the user
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchesAsPlayer2Query)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }

        // Delete Matches records where winner_id is the user
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchesAsWinnerQuery)) {
            stmt.setString(1, userId);
            stmt.executeUpdate();
        }

        // Delete the user record
        try (PreparedStatement stmt = conn.prepareStatement(deleteUserQuery)) {
            stmt.setString(1, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No user found with ID: " + userId);
            }
        }

        conn.commit();  // Commit transaction
    } catch (SQLException e) {
        // Rollback in case of an error
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.rollback();  // Rollback transaction if any part fails
        } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();  // Handle rollback exception
        }
        e.printStackTrace();  // Log the original exception
        throw e;  // Rethrow the original exception
    }
}





    public void updateUser(String userId, String username, String password, String email) throws SQLException {
    String query = "UPDATE Users SET username = ?, password = ?, email = ?, created_at = current_timestamp() WHERE user_id = ?";
    User u = new User(userId, username, password, email, userId);
    String hp = u.encryptPassword(password);
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, hp);
        stmt.setString(3, email);
        stmt.setString(4, userId);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No user found with ID: " + userId);
        }
    }
}
    public void deleteMatchHistory(String historyId) throws SQLException {
    String deleteMatchHistoryQuery = "DELETE FROM MatchHistory WHERE history_id = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);  // Begin transaction

        // Delete the match history record
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatchHistoryQuery)) {
            stmt.setString(1, historyId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No match history found with ID: " + historyId);
            }
        }

        conn.commit();  // Commit transaction
    } catch (SQLException e) {
        // Rollback in case of an error
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.rollback();  // Rollback transaction if any part fails
        } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();  // Handle rollback exception
        }
        e.printStackTrace();  // Log the original exception
        throw e;  // Rethrow the original exception
    }
}
  public void updateMatchHistory(String historyId, String matchId, String playerId, String moveNumber, String moveTime) throws SQLException {
    String query = "UPDATE MatchHistory SET match_id = ?, player_id = ?, move_number = ?, move_time = ? WHERE history_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, matchId);
        stmt.setString(2, playerId);
        stmt.setString(3, moveNumber);
        stmt.setString(4, moveTime);  
        stmt.setString(5, historyId);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No match history found with ID: " + historyId);
        }
    }
}

}

