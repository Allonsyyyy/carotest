package com.mycompany.carogame.socket12.v2;

import com.mycompany.carogame.Login.App;
import com.mycompany.carogame.Login.Match;
import com.mycompany.carogame.Login.MatchEntity;
import com.mycompany.carogame.Login.MatchHistory;
import com.mycompany.carogame.Login.MatchHistoryEntity;
import com.mycompany.carogame.Login.SessionData;
import com.mycompany.carogame.Login.User;
import com.mycompany.carogame.javaai.AIPlayer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardController {

    private boolean gameEnded = false;
    private boolean xTurn = true; // 'X' starts first
    private String[][] board = new String[12][12];
    private AIPlayer aiPlayer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int moveCount = 0;
    @FXML
    private GridPane gridPane;
    @FXML
    private Text txtUsername;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnPlayAsGuest;

    // Define buttons as @FXML fields
    @FXML
    private Button button00, button01, button02, button03, button04, button05, button06, button07, button08, button09, button010, button011;
    @FXML
    private Button button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button110, button111;
    @FXML
    private Button button20, button21, button22, button23, button24, button25, button26, button27, button28, button29, button210, button211;
    @FXML
    private Button button30, button31, button32, button33, button34, button35, button36, button37, button38, button39, button310, button311;
    @FXML
    private Button button40, button41, button42, button43, button44, button45, button46, button47, button48, button49, button410, button411;
    @FXML
    private Button button50, button51, button52, button53, button54, button55, button56, button57, button58, button59, button510, button511;
    @FXML
    private Button button60, button61, button62, button63, button64, button65, button66, button67, button68, button69, button610, button611;
    @FXML
    private Button button70, button71, button72, button73, button74, button75, button76, button77, button78, button79, button710, button711;
    @FXML
    private Button button80, button81, button82, button83, button84, button85, button86, button87, button88, button89, button810, button811;
    @FXML
    private Button button90, button91, button92, button93, button94, button95, button96, button97, button98, button99, button910, button911;
    @FXML
    private Button button100, button101, button102, button103, button104, button105, button106, button107, button108, button109, button1010, button1011;
    @FXML
    private Button button1100, button1101, button112, button113, button114, button115, button116, button117, button118, button119, button1110, button1111;

    public BoardController() {
        initializeBoard();
        aiPlayer = new AIPlayer("O", "X", board); // AI plays as 'O'
    }

    @FXML
    public void initialize() {
        setUserName();
        initializeBoard();
        setupButtons();
        startTime = LocalDateTime.now();
    }

    private void setUserName() {
        User user = new User(SessionData.getCurrentUser());
        txtUsername.setText("Hello " + user.getUsername());
    }

    @FXML
    public void logOut() throws IOException {
        SessionData.Logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogOut.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToPlayAsGuest() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/GameDifficulty.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPlayAsGuest.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void Back() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/socket12/v2/Board.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPlayAsGuest.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                board[i][j] = "";
            }
        }
    }

    private void setupButtons() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Button button = (Button) gridPane.lookup("#button" + i + j);
                if (button != null) {
                    button.setOnAction(this::handleButtonAction); // Set action handler
                }
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (gameEnded) {
            return; // Exit if the game has ended
        }

        Button button = (Button) event.getSource(); // Get the button that was clicked
        if (!button.getText().isEmpty()) {
            return; // Exit if the button has already been clicked
        }

        // Determine the row and column based on button's position in GridPane
        Integer row = GridPane.getRowIndex(button);
        Integer col = GridPane.getColumnIndex(button);

        // Default to 0 if row or col is null (for buttons not yet added to the GridPane)
        row = (row == null) ? 0 : row;
        col = (col == null) ? 0 : col;

        // Set the button's text based on whose turn it is
        String mark = xTurn ? "X" : "O";
        button.setText(mark);
        board[row][col] = mark;
        xTurn = !xTurn; // Switch turns

        moveCount++;
        printBoard();

        if (checkWinner()) {
            gameEnded = true;
            disableAllButtons();
            showWinner(mark); // Show winner message
            return;
        }

        if (isGameDraw()) {
            gameEnded = true;
            disableAllButtons();
            showDraw(); // Show draw message
        }
    }

    private void printBoard() {
        System.out.println("Current Board:");
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean checkWinner() {
        Button[][] buttons = {
            {button00, button01, button02, button03, button04, button05, button06, button07, button08, button09, button010, button011},
            {button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button110, button111},
            {button20, button21, button22, button23, button24, button25, button26, button27, button28, button29, button210, button211},
            {button30, button31, button32, button33, button34, button35, button36, button37, button38, button39, button310, button311},
            {button40, button41, button42, button43, button44, button45, button46, button47, button48, button49, button410, button411},
            {button50, button51, button52, button53, button54, button55, button56, button57, button58, button59, button510, button511},
            {button60, button61, button62, button63, button64, button65, button66, button67, button68, button69, button610, button611},
            {button70, button71, button72, button73, button74, button75, button76, button77, button78, button79, button710, button711},
            {button80, button81, button82, button83, button84, button85, button86, button87, button88, button89, button810, button811},
            {button90, button91, button92, button93, button94, button95, button96, button97, button98, button99, button910, button911},
            {button100, button101, button102, button103, button104, button105, button106, button107, button108, button109, button1010, button1011},
            {button110, button111, button112, button113, button114, button115, button116, button117, button118, button119, button1110, button1111}
        };

        // Check rows
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                if (!buttons[i][j].getText().isEmpty()
                        && buttons[i][j].getText().equals(buttons[i][j + 1].getText())
                        && buttons[i][j].getText().equals(buttons[i][j + 2].getText())
                        && buttons[i][j].getText().equals(buttons[i][j + 3].getText())
                        && buttons[i][j].getText().equals(buttons[i][j + 4].getText())) {
                    showWinner(buttons[i][j].getText());
                    return true;
                }
            }
        }

        // Check columns
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                if (!buttons[j][i].getText().isEmpty()
                        && buttons[j][i].getText().equals(buttons[j + 1][i].getText())
                        && buttons[j][i].getText().equals(buttons[j + 2][i].getText())
                        && buttons[j][i].getText().equals(buttons[j + 3][i].getText())
                        && buttons[j][i].getText().equals(buttons[j + 4][i].getText())) {
                    showWinner(buttons[j][i].getText());
                    return true;
                }
            }
        }

        // Check diagonals (top-left to bottom-right)
        for (int i = 0; i <= 12 - 5; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                if (!buttons[i][j].getText().isEmpty()
                        && buttons[i][j].getText().equals(buttons[i + 1][j + 1].getText())
                        && buttons[i][j].getText().equals(buttons[i + 2][j + 2].getText())
                        && buttons[i][j].getText().equals(buttons[i + 3][j + 3].getText())
                        && buttons[i][j].getText().equals(buttons[i + 4][j + 4].getText())) {
                    showWinner(buttons[i][j].getText());
                    return true;
                }
            }
        }

        // Check diagonals (top-right to bottom-left)
        for (int i = 0; i <= 12 - 5; i++) {
            for (int j = 4; j < 12; j++) {
                if (!buttons[i][j].getText().isEmpty()
                        && buttons[i][j].getText().equals(buttons[i + 1][j - 1].getText())
                        && buttons[i][j].getText().equals(buttons[i + 2][j - 2].getText())
                        && buttons[i][j].getText().equals(buttons[i + 3][j - 3].getText())
                        && buttons[i][j].getText().equals(buttons[i + 4][j - 4].getText())) {
                    showWinner(buttons[i][j].getText());
                    return true;
                }
            }
        }

        // Check for draw
        if (isGameDraw()) {
            showDraw();
            return true;
        }

        return false;
    }

    private boolean isGameDraw() {
        Button[][] buttons = {
            {button00, button01, button02, button03, button04, button05, button06, button07, button08, button09, button010, button011},
            {button10, button11, button12, button13, button14, button15, button16, button17, button18, button19, button110, button111},
            {button20, button21, button22, button23, button24, button25, button26, button27, button28, button29, button210, button211},
            {button30, button31, button32, button33, button34, button35, button36, button37, button38, button39, button310, button311},
            {button40, button41, button42, button43, button44, button45, button46, button47, button48, button49, button410, button411},
            {button50, button51, button52, button53, button54, button55, button56, button57, button58, button59, button510, button511},
            {button60, button61, button62, button63, button64, button65, button66, button67, button68, button69, button610, button611},
            {button70, button71, button72, button73, button74, button75, button76, button77, button78, button79, button710, button711},
            {button80, button81, button82, button83, button84, button85, button86, button87, button88, button89, button810, button811},
            {button90, button91, button92, button93, button94, button95, button96, button97, button98, button99, button910, button911},
            {button100, button101, button102, button103, button104, button105, button106, button107, button108, button109, button1010, button1011},
            {button110, button111, button112, button113, button114, button115, button116, button117, button118, button119, button1110, button1111}
        };

        // Check if any button is empty
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void saveMatchToDatabase(String winnerId) {
        if (SessionData.getCurrentUser() != null) {
            int player1Id = SessionData.currentUser.getId();
            String player2Id = "Player2";
            endTime = LocalDateTime.now();
            Match match = new Match(player1Id, player2Id, startTime, endTime, winnerId);
            MatchEntity.getInstance().insert(match);
            int id = MatchEntity.getInstance().findById(match).getMatchId();
            // luu matchHistory
            double totalDuration = java.time.Duration.between(startTime, endTime).toSeconds();
            MatchHistory matchHistory = new MatchHistory(id, player1Id, moveCount, totalDuration);
            MatchHistoryEntity.getInstance().insert(matchHistory);
        }

    }

    private void showWinner(String winner) {
        if (!gameEnded) {
            System.out.println("Player " + winner + " wins!");
            saveMatchToDatabase(winner.equals("X") ? String.valueOf(SessionData.getCurrentUser().getId()) : "Player2");
            gameEnded = true;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Player " + winner + " wins!");
            alert.setContentText("Click OK to play again.");

            // Xử lý sự kiện khi nút "OK" được nhấn
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Back();
                    } catch (IOException ex) {
                        Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }

    private void showDraw() {
        if (!gameEnded) {
            System.out.println("It's a draw!");
            saveMatchToDatabase(null);
            gameEnded = true;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("It's a draw!");
            alert.setContentText("Click OK to play again.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        Back();
                    } catch (IOException ex) {
                        Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }


    private void disableAllButtons() {
        Button button110000 = (Button) gridPane.lookup("#button110000");
        Button button111000 = (Button) gridPane.lookup("#button111000");

        if (button110000 != null) {
            button110000.setDisable(true);
        }

        if (button111000 != null) {
            button111000.setDisable(true);
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Button button = (Button) gridPane.getChildren().get(i * 12 + j);
                button.setDisable(true);
            }
        }
    }

}
