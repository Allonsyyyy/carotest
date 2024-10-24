package com.mycompany.carogame.admin;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMatchController {

    @FXML
    private TextField matchIdField;

    @FXML
    private TextField player1IdField;

    @FXML
    private TextField player2IdField;

    @FXML
    private TextField startTimeField;

    @FXML
    private TextField endTimeField;

    @FXML
    private TextField winnerIdField;

    private Match match;

    public void initialize() {
        // Initialization logic if needed
    }

    public void setMatch(Match match) {
        this.match = match;
        matchIdField.setText(match.getMatchId());
        player1IdField.setText(match.getPlayer1Id());
        player2IdField.setText(match.getPlayer2Id());
        startTimeField.setText(match.getStartTime());
        endTimeField.setText(match.getEndTime());
        winnerIdField.setText(match.getWinnerId());
    }

    @FXML
    public void handleSave() {
        if (match == null) {
            return; // No match to update
        }

        // Get updated values from text fields
        String matchId = matchIdField.getText().trim();
        String player1Id = player1IdField.getText().trim();
        String player2Id = player2IdField.getText().trim();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();
        String winnerId = winnerIdField.getText().trim();

        // Perform input validation (e.g., check if fields are not empty)
        if (matchId.isEmpty() || player1Id.isEmpty() || player2Id.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || winnerId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out.");
            alert.showAndWait();
            return;
        }

        // Update the match in the database
        try {
            DataFetcher dataFetcher = new DataFetcher(); // Ensure this class is accessible and properly initialized
            dataFetcher.updateMatch(matchId, player1Id, player2Id, startTime, endTime, winnerId);

            // Notify the user of the successful update
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("The match details have been updated successfully.");
            alert.showAndWait();

            // Close the dialog if matchIdField is not null and has a scene
            if (matchIdField != null && matchIdField.getScene() != null) {
                ((Stage) matchIdField.getScene().getWindow()).close();
            } else {
                System.out.println("TextField hoặc Scene là null");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Notify the user of the error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the match. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog if matchIdField is not null and has a scene
        if (matchIdField != null && matchIdField.getScene() != null) {
            ((Stage) matchIdField.getScene().getWindow()).close();
        } else {
            System.out.println("TextField hoặc Scene là null");
        }
    }

}
