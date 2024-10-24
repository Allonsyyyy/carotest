package com.mycompany.carogame.socket12.v2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {

    @FXML
    private Button startButton;

    @FXML
    private void handleStartButton() {
        // Hiển thị thông báo
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Bắt đầu trò chơi!");
        alert.showAndWait();
        
        // Tải trang Board.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/socket12/v2/Board.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu có
        }
    }
}
