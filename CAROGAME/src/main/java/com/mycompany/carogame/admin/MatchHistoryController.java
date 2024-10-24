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

public class MatchHistoryController {

    @FXML
    private Hyperlink dashboardLink;

    @FXML
    private Hyperlink matchHistoryLink;

    @FXML
    private Hyperlink backLink;

    @FXML
    private TableView<MatchHistory> tableView;

    @FXML
    private TableColumn<MatchHistory, String> historyIdColumn;

    @FXML
    private TableColumn<MatchHistory, String> matchIdColumn;

    @FXML
    private TableColumn<MatchHistory, String> playerIdColumn;

    @FXML
    private TableColumn<MatchHistory, Integer> moveNumberColumn;

  

    @FXML
    private TableColumn<MatchHistory, String> moveTimeColumn;

    @FXML
    private TableColumn<MatchHistory, Void> editColumn;

    @FXML
    private TableColumn<MatchHistory, Void> deleteColumn;

    private DataFetcher dataFetcher = new DataFetcher();

    @FXML
    public void initialize() {
        // Initialize columns
        historyIdColumn.setCellValueFactory(new PropertyValueFactory<>("historyId"));
        matchIdColumn.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        moveNumberColumn.setCellValueFactory(new PropertyValueFactory<>("moveNumber"));
        moveTimeColumn.setCellValueFactory(new PropertyValueFactory<>("moveTime"));

        // Set up Edit column
        editColumn.setCellFactory(new Callback<TableColumn<MatchHistory, Void>, TableCell<MatchHistory, Void>>() {
            @Override
            public TableCell<MatchHistory, Void> call(TableColumn<MatchHistory, Void> param) {
                return new TableCell<MatchHistory, Void>() {
                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction(e -> {
                            MatchHistory matchHistory = getTableView().getItems().get(getIndex());
                            handleEdit(matchHistory);
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
        deleteColumn.setCellFactory(new Callback<TableColumn<MatchHistory, Void>, TableCell<MatchHistory, Void>>() {
            @Override
            public TableCell<MatchHistory, Void> call(TableColumn<MatchHistory, Void> param) {
                return new TableCell<MatchHistory, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(e -> {
                            MatchHistory matchHistory = getTableView().getItems().get(getIndex());
                            handleDelete(matchHistory);
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
            ObservableList<MatchHistory> matchHistories = dataFetcher.getMatchHistoryData();
            tableView.setItems(matchHistories);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private void handleEdit(MatchHistory matchHistory) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/editmatchhistory.fxml"));
            DialogPane dialogPane = loader.load();
            EditMatchHistoryController editController = loader.getController();
            editController.setMatchHistory(matchHistory);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Match History");

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

    private void handleDelete(MatchHistory matchHistory) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Match History");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this match history?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete match history from database
                try {
                    dataFetcher.deleteMatchHistory(matchHistory.getHistoryId());
                    loadData();  // Refresh the data
                } catch (SQLException e) {
                    showError("Error deleting match history", "An error occurred while deleting the match history. Please try again.");
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
