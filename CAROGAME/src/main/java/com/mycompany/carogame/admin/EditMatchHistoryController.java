package com.mycompany.carogame.admin;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMatchHistoryController {

    @FXML
    private TextField historyIdField;

    @FXML
    private TextField matchIdField;

    @FXML
    private TextField playerIdField;

    @FXML
    private TextField moveNumberField;

    @FXML
    private TextField moveTimeField;
    
    @FXML
    private MatchHistory matchHistory;

    public void initialize() {
        // Initialization logic if needed
    }

    public void setMatchHistory(MatchHistory matchHistory) {
        this.matchHistory = matchHistory;
        historyIdField.setText(matchHistory.getHistoryId());
        matchIdField.setText(matchHistory.getMatchId());
        playerIdField.setText(matchHistory.getPlayerId());
        moveNumberField.setText(matchHistory.getMoveNumber());
        moveTimeField.setText(matchHistory.getMoveTime());
    }

    @FXML
    public void handleSave() {
        if (matchHistory == null) {
            return; // No match history to update
        }

        // Get updated values from text fields
        String historyId = historyIdField.getText().trim();
        String matchId = matchIdField.getText().trim();
        String playerId = playerIdField.getText().trim();
        String moveNumber = moveNumberField.getText().trim();
        String moveTime = moveTimeField.getText().trim();

        // Perform input validation (e.g., check if fields are not empty)
        if (matchId.isEmpty() || playerId.isEmpty() || moveNumber.isEmpty() ||
            moveTime.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled out.");
            alert.showAndWait();
            return;
        }

        // Update the match history in the database
        try {
            DataFetcher dataFetcher = new DataFetcher(); // Ensure this class is accessible and properly initialized
            dataFetcher.updateMatchHistory(historyId, matchId, playerId, moveNumber,  moveTime);
            
            // Notify the user of the successful update
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("The match history details have been updated successfully.");
            alert.showAndWait();
            
            // Close the dialog
            ((Stage) historyIdField.getScene().getWindow()).close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Notify the user of the error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the match history. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the dialog without saving
        ((Stage) historyIdField.getScene().getWindow()).close();
    }
}
