package com.mycompany.carogame.admin;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;

public class UserController {

    @FXML
    private Hyperlink dashboardLink;

    @FXML
    private Hyperlink userLink;

    @FXML
    private Hyperlink backLink;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> userIdColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> createdAtColumn;

    @FXML
    private TableColumn<User, Void> editColumn;

    @FXML
    private TableColumn<User, Void> deleteColumn;

    private DataFetcher dataFetcher = new DataFetcher();

    @FXML
public void initialize() {
    // Initialize columns
     userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));


    // Set up Edit column
    editColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
        @Override
        public TableCell<User, Void> call(TableColumn<User, Void> param) {
            return new TableCell<User, Void>() {
                private final Button editButton = new Button("Edit");

                {
                    editButton.setOnAction(e -> {
                        User user = getTableView().getItems().get(getIndex());
                        handleEdit(user);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
        }
    });

    // Set up Delete column
    deleteColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
        @Override
        public TableCell<User, Void> call(TableColumn<User, Void> param) {
            return new TableCell<User, Void>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(e -> {
                        User user = getTableView().getItems().get(getIndex());
                        handleDelete(user);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        }
    });

    // Load data
    loadData();
}


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backLink.getScene().getWindow();
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
            Stage stage = (Stage) dashboardLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void handleDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/admin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) dashboardLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void loadData() {
        try {
            ObservableList<User> users = dataFetcher.getUserData();
            tableView.setItems(users);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void handleEdit(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/edituser.fxml"));
            DialogPane dialogPane = loader.load();
            EditUserController editController = loader.getController();
            editController.setUser(user);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit User");

            ButtonType saveButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

            dialog.showAndWait().ifPresent(response -> {
                if (response == saveButtonType) {
                    // Handle save logic here
                    editController.handleSave();
                    loadData(); // Refresh the data
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void handleDelete(User user) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this user?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete user from database
                try {
                    dataFetcher.deleteUser(user.getUserId());
                    loadData();  // Refresh the data
                } catch (SQLException e) {
                    showError("Error deleting user", "An error occurred while deleting the user. Please try again.");
                }
            }
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
