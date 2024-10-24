package com.mycompany.carogame.Login;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private Button btnRegister;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnPlayAsGuest;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtVPassword;
    @FXML
    private Button btnBack;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
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
    private void switchToLogin() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
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
    public void register() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();
        String vPassword = txtVPassword.getText();

        if (username == null || username.isEmpty()) {
            showAlert("Register failed", "Please enter a username.");
            return;
        }
        
        if (password == null || password.isEmpty()) {
            showAlert("Register failed", "Please enter a password.");
            return;
        }
        
        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            showAlert("Register failed", "Please enter a valid email.");
            return;
        }

        UserEntity userEntity = UserEntity.getInstance();
        int check = userEntity.findAcountRegiser(username);

        if (check == 1) {
            showAlert("Register failed", "Account already exists.");
            return;
        } else {
            if (password.equals(vPassword)) {
                User user = new User(username, password, email);
                userEntity.insert(user);
                switchToLogin();
            } else {
                showAlert("Register failed", "Passwords do not match.");
            }
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
