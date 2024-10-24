/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.javaai;

/**
 *
 * @author anhphuc
 */
import com.mycompany.carogame.Login.MatchEntity;
import com.mycompany.carogame.Login.SessionData;
import com.mycompany.carogame.Login.Match;
import com.mycompany.carogame.Login.MatchHistory;
import com.mycompany.carogame.Login.MatchHistoryEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BoardController {

    private boolean gameEnded = false;
    private boolean xTurn = true;
    private AIPlayer aiPlayer;
    private String[][] board = new String[12][12];
    private String difficulty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private int moveCount = 0;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button btnBackToLogin;
    @FXML
    private Button btnExit;
    @FXML
    private Text txtUserName;

    @FXML
    public void initialize() {
        setUSetUserName();
    }

    @FXML
    public void setUSetUserName() {
        if (SessionData.currentUser != null) {
            txtUserName.setText("Hello " + SessionData.getCurrentUser().getUsername());
        }
    }

    @FXML
    private void switchToGameDifficulty(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/GameDifficulty.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    @FXML
    private void Back() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/Board.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnExit.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    @FXML
    private void switchToLogin(ActionEvent event) throws IOException {
        SessionData.Logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/carogame/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBackToLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
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

    public BoardController() {
        initializeBoard();
        aiPlayer = new AIPlayer("O", "X", board); // AI plays as 'O'
    }

    private void initializeBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                board[i][j] = "";
            }
        }
    }

    public void initGame(String difficulty) {
        this.difficulty = difficulty; // Store the selected difficulty
        aiPlayer = new AIPlayer("O", "X", board); // AI plays as 'O'
        startTime = LocalDateTime.now();
    }

    public String[][] getBoard() {
        return board;
    }

    public void setAIPlayer(AIPlayer aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    @FXML
    private void handleButtonAction(javafx.event.ActionEvent event) {
        if (gameEnded) {
            return;
        }
        Button button = (Button) event.getSource();
        if (!button.getText().isEmpty()) {
            return;
        }
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
        button.setText(xTurn ? "X" : "O");
        board[row][col] = xTurn ? "X" : "O";
        xTurn = !xTurn;

        moveCount++;

        printBoard();

        if (checkWinner()) {
            gameEnded = true;
            disableAllButtons();
            return;
        }

        if (!xTurn) {
            aiPlayer.makeMove(difficulty); // Change to "medium" or "hard" for other difficulties
            System.out.println("AI move made.");
            printBoard();
            updateBoard();
            xTurn = true;
            if (checkWinner()) {
                gameEnded = true; // Set gameEnded to true
                disableAllButtons(); // Disable all buttons upon game end
            }
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Button button = (Button) gridPane.getChildren().get(i * 12 + j);
                button.setText(board[i][j]);
            }
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
            {button110000, button111000, button112, button113, button114, button115, button116, button117, button118, button119, button1110, button1111}
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
            {button110000, button111000, button112, button113, button114, button115, button116, button117, button118, button119, button1110, button1111}
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
        String player2Id = "AI";
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
        System.out.println("Người chơi " + winner + " thắng!");
        if (SessionData.getCurrentUser() != null){saveMatchToDatabase(winner.equals("X") ? String.valueOf(SessionData.getCurrentUser().getId()) : "AI");}
        try {
            Back();
        } catch (IOException ex) {
            Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showDraw() {
        System.out.println("Hòa!");
        if (SessionData.getCurrentUser() != null){saveMatchToDatabase(null);
            try {
                Back();
            } catch (IOException ex) {
                Logger.getLogger(BoardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button03;
    @FXML
    private Button button04;
    @FXML
    private Button button05;
    @FXML
    private Button button06;
    @FXML
    private Button button07;
    @FXML
    private Button button08;
    @FXML
    private Button button09;
    @FXML
    private Button button010;
    @FXML
    private Button button011;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button13;
    @FXML
    private Button button14;
    @FXML
    private Button button15;
    @FXML
    private Button button16;
    @FXML
    private Button button17;
    @FXML
    private Button button18;
    @FXML
    private Button button19;
    @FXML
    private Button button110;
    @FXML
    private Button button111;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;
    @FXML
    private Button button23;
    @FXML
    private Button button24;
    @FXML
    private Button button25;
    @FXML
    private Button button26;
    @FXML
    private Button button27;
    @FXML
    private Button button28;
    @FXML
    private Button button29;
    @FXML
    private Button button210;
    @FXML
    private Button button211;
    @FXML
    private Button button30;
    @FXML
    private Button button31;
    @FXML
    private Button button32;
    @FXML
    private Button button33;
    @FXML
    private Button button34;
    @FXML
    private Button button35;
    @FXML
    private Button button36;
    @FXML
    private Button button37;
    @FXML
    private Button button38;
    @FXML
    private Button button39;
    @FXML
    private Button button310;
    @FXML
    private Button button311;
    @FXML
    private Button button40;
    @FXML
    private Button button41;
    @FXML
    private Button button42;
    @FXML
    private Button button43;
    @FXML
    private Button button44;
    @FXML
    private Button button45;
    @FXML
    private Button button46;
    @FXML
    private Button button47;
    @FXML
    private Button button48;
    @FXML
    private Button button49;
    @FXML
    private Button button410;
    @FXML
    private Button button411;
    @FXML
    private Button button50;
    @FXML
    private Button button51;
    @FXML
    private Button button52;
    @FXML
    private Button button53;
    @FXML
    private Button button54;
    @FXML
    private Button button55;
    @FXML
    private Button button56;
    @FXML
    private Button button57;
    @FXML
    private Button button58;
    @FXML
    private Button button59;
    @FXML
    private Button button510;
    @FXML
    private Button button511;
    @FXML
    private Button button60;
    @FXML
    private Button button61;
    @FXML
    private Button button62;
    @FXML
    private Button button63;
    @FXML
    private Button button64;
    @FXML
    private Button button65;
    @FXML
    private Button button66;
    @FXML
    private Button button67;
    @FXML
    private Button button68;
    @FXML
    private Button button69;
    @FXML
    private Button button610;
    @FXML
    private Button button611;
    @FXML
    private Button button70;
    @FXML
    private Button button71;
    @FXML
    private Button button72;
    @FXML
    private Button button73;
    @FXML
    private Button button74;
    @FXML
    private Button button75;
    @FXML
    private Button button76;
    @FXML
    private Button button77;
    @FXML
    private Button button78;
    @FXML
    private Button button79;
    @FXML
    private Button button710;
    @FXML
    private Button button711;
    @FXML
    private Button button80;
    @FXML
    private Button button81;
    @FXML
    private Button button82;
    @FXML
    private Button button83;
    @FXML
    private Button button84;
    @FXML
    private Button button85;
    @FXML
    private Button button86;
    @FXML
    private Button button87;
    @FXML
    private Button button88;
    @FXML
    private Button button89;
    @FXML
    private Button button810;
    @FXML
    private Button button811;
    @FXML
    private Button button90;
    @FXML
    private Button button91;
    @FXML
    private Button button92;
    @FXML
    private Button button93;
    @FXML
    private Button button94;
    @FXML
    private Button button95;
    @FXML
    private Button button96;
    @FXML
    private Button button97;
    @FXML
    private Button button98;
    @FXML
    private Button button99;
    @FXML
    private Button button910;
    @FXML
    private Button button911;
    @FXML
    private Button button100;
    @FXML
    private Button button101;
    @FXML
    private Button button102;
    @FXML
    private Button button103;
    @FXML
    private Button button104;
    @FXML
    private Button button105;
    @FXML
    private Button button106;
    @FXML
    private Button button107;
    @FXML
    private Button button108;
    @FXML
    private Button button109;
    @FXML
    private Button button1010;
    @FXML
    private Button button1011;
    @FXML
    private Button button110000;
    @FXML
    private Button button111000;
    @FXML
    private Button button112;
    @FXML
    private Button button113;
    @FXML
    private Button button114;
    @FXML
    private Button button115;
    @FXML
    private Button button116;
    @FXML
    private Button button117;
    @FXML
    private Button button118;
    @FXML
    private Button button119;
    @FXML
    private Button button1110;
    @FXML
    private Button button1111;

}
