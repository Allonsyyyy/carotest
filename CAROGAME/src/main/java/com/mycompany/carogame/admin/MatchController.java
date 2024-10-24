package com.mycompany.carogame.admin;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

public class MatchController {

    @FXML
    private Hyperlink dashboardLink;

    @FXML
    private Hyperlink matchLink;

    @FXML
    private Hyperlink backLink;

    @FXML
    private TableView<Match> tableView;

    @FXML
    private TableColumn<Match, String> matchIdColumn;

    @FXML
    private TableColumn<Match, String> player1IdColumn;

    @FXML
    private TableColumn<Match, String> player2IdColumn;

    @FXML
    private TableColumn<Match, String> startTimeColumn;

    @FXML
    private TableColumn<Match, String> endTimeColumn;

    @FXML
    private TableColumn<Match, String> winnerIdColumn;

    @FXML
    private TableColumn<Match, Void> editColumn;

    @FXML
    private TableColumn<Match, Void> deleteColumn;

    private DataFetcher dataFetcher = new DataFetcher();

    @FXML
    public void initialize() {
        // Initialize columns
        matchIdColumn.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        player1IdColumn.setCellValueFactory(new PropertyValueFactory<>("player1Id"));
        player2IdColumn.setCellValueFactory(new PropertyValueFactory<>("player2Id"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        winnerIdColumn.setCellValueFactory(new PropertyValueFactory<>("winnerId"));

        // Set up Edit column
        editColumn.setCellFactory(new Callback<TableColumn<Match, Void>, TableCell<Match, Void>>() {
            @Override
            public TableCell<Match, Void> call(TableColumn<Match, Void> param) {
                return new TableCell<Match, Void>() {
                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction(e -> {
                            Match match = getTableView().getItems().get(getIndex());
                            handleEdit(match);
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
        deleteColumn.setCellFactory(new Callback<TableColumn<Match, Void>, TableCell<Match, Void>>() {
            @Override
            public TableCell<Match, Void> call(TableColumn<Match, Void> param) {
                return new TableCell<Match, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(e -> {
                            Match match = getTableView().getItems().get(getIndex());
                            handleDelete(match);
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
    private void handleMatch() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/match.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) matchLink.getScene().getWindow();
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
            ObservableList<Match> matches = dataFetcher.getMatchData();
            tableView.setItems(matches);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

     private void handleEdit(Match match) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/admin/editmatch.fxml"));
            DialogPane dialogPane = loader.load();
            EditMatchController editController = loader.getController();
            editController.setMatch(match);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Match");

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
    private void handleDelete(Match match) {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Delete Match");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete this match?");
    alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // Delete match from database
            try {
                dataFetcher.deleteMatch(match.getMatchId());
                loadData();  // Refresh the data
            } catch (SQLException e) {
                showError("Error deleting match", "An error occurred while deleting the match. Please try again.");
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
