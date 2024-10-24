/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

/**
 *
 * @author anhphuc
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnPlayAsGuest;
    @FXML
    private Button btnRegister;
    @FXML
    private Hyperlink termsOfUseLink;
    @FXML
    private Button btnBack;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchFromPolicy() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void switchToRegister() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void playAsGuest() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/GameDifficulty.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPlayAsGuest.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void switchToPolicy() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/Policy.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) termsOfUseLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    public void checkUser() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username == null || username.isEmpty()) {
            showAlert("Login failed", "Please enter a username.");
            return;
        }
        if (password == null || password.isEmpty()) {
            showAlert("Login failed", "Please enter a password.");
            return;
        }

        UserEntity userEntity = UserEntity.getInstance();
        int check = userEntity.findAcount(username, password);

        if (check == 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/admin.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) termsOfUseLink.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }     // Xử lý trường hợp Admin
        } else if (check == -1) {
            showAlert("Login failed", "Incorrect username or password. Please try again.");
        } else {
            User user = userEntity.findById2(check);
            SessionData.currentUser = user;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/socket12/v2/Board.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) termsOfUseLink.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }     // Xử lý trường hợp Admin
        }
    }

    @FXML
    public void checkAdminUser() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        UserEntity userEntity = UserEntity.getInstance();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
