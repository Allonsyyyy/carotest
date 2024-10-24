package com.mycompany.carogame.admin;

import com.mycompany.carogame.Login.SessionData;
import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminController {
    
    
    @FXML
    private Label newUsersLabel;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label totalMatchesLabel;

    @FXML
    private Hyperlink usersLink;

    @FXML
    private Hyperlink matchesLink;

    @FXML
    private Hyperlink matchHistoryLink;

    @FXML
    private Hyperlink logoutLink;

    private DataFetcher dataFetcher;

    @FXML
    private void initialize() {
        dataFetcher = new DataFetcher();

            updateLabels();
        
    }

    @FXML
    private void handleMatch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/match.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) matchesLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    @FXML
    private void handleUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/user.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usersLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    @FXML
    private void handleMatchHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/matchhistory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) matchHistoryLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void updateLabels() {
        try {
            int newUsers = dataFetcher.getNewUsers();
            int totalUsers = dataFetcher.getTotalUsers();
            int totalMatches = dataFetcher.getTotalMatches();

            // Ensure labels are updated on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                newUsersLabel.setText(String.valueOf(newUsers) + " Users");
                totalUsersLabel.setText(String.valueOf(totalUsers) + " Users");
                totalMatchesLabel.setText(String.valueOf(totalMatches) + " Matches");
            });
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., show error message to user)
        }
    }

    @FXML
    private void handleLogout() {
        SessionData.Logout();
        System.out.println(SessionData.getCurrentUser());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) matchHistoryLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
}
