package com.mycompany.carogame.admin;

import java.sql.SQLException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {

    @FXML
    private TextField userIdField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField createdAtField;

    private User user;

    public void initialize() {
        // Initialization logic if needed
    }

    public void setUser(User user) {
        this.user = user;
        userIdField.setText(user.getUserId());
        usernameField.setText(user.getUsername());
        passwordField.setText(user.decryptPassword(user.getPassword()));
        emailField.setText(user.getEmail());
        createdAtField.setText(user.getCreatedAt());
    }

    @FXML
public void handleSave() {
    if (user == null) {
        return; // Không có người dùng nào để cập nhật
    }

    // Lấy các giá trị cập nhật từ các text field
    String userId = userIdField.getText().trim();
    String username = usernameField.getText().trim();
    String password = passwordField.getText().trim();
    String email = emailField.getText().trim();

    // Thực hiện kiểm tra đầu vào (ví dụ: kiểm tra nếu các trường không rỗng)
    if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("All fields must be filled out.");
        alert.showAndWait();
        return;
    }

    // Cập nhật người dùng trong cơ sở dữ liệu
    try {
        DataFetcher dataFetcher = new DataFetcher(); // Đảm bảo rằng lớp này có thể truy cập và được khởi tạo đúng cách
        dataFetcher.updateUser(userId, username, password, email);

        // Thông báo cho người dùng về việc cập nhật thành công
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Successful");
        alert.setHeaderText(null);
        alert.setContentText("The user details have been updated successfully.");
        alert.showAndWait();

        // Đóng hộp thoại
        Platform.runLater(() -> {
            if (userIdField.getScene() != null) {
                Stage stage = (Stage) userIdField.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            } else {
                System.out.println("Save");
            }
        });
    } catch (SQLException e) {
        e.printStackTrace();
        // Thông báo cho người dùng về lỗi
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update Failed");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while updating the user. Please try again.");
        alert.showAndWait();
    }
}


    @FXML
    private void handleCancel() {
        // Close the dialog without saving
        Stage stage = (Stage) userIdField.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
