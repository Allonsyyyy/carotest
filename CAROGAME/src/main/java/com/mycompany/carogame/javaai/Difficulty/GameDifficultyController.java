package com.mycompany.carogame.javaai.Difficulty;
import com.mycompany.carogame.Login.App;
import com.mycompany.carogame.Login.SessionData;
import com.mycompany.carogame.javaai.AIPlayer;
import com.mycompany.carogame.javaai.BoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;

public class GameDifficultyController {
    
    @FXML
    private Button easyButton;
    @FXML
    private Button btnOut;
    @FXML
    private void handleEasyMode(ActionEvent event) {
        loadGameBoard("easy");
    }

    @FXML
    private void handleMediumMode(ActionEvent event) {
        loadGameBoard("medium");
    }

    @FXML
    private void handleHardMode(ActionEvent event) {
        loadGameBoard("hard");
    }
    @FXML
    private void switchToLogin() throws IOException {
        SessionData.Logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnOut.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private void loadGameBoard(String difficulty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/Board.fxml"));
            Parent root = loader.load();

            BoardController boardController = loader.getController();

            // Initialize AIPlayer and pass it to BoardController
            AIPlayer aiPlayer = new AIPlayer("X", "O", boardController.getBoard()); // Adjust markers as needed
            boardController.setAIPlayer(aiPlayer);

            boardController.initGame(difficulty); // Initialize game with selected difficulty

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Game Board");
            stage.show();

            // Close current window if needed
            Stage currentStage = (Stage) easyButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle load error
        }
    }
}