/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.javaai;

/**
 *
 * @author anhphuc
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {

    private String aiMarker;
    private String playerMarker;
    private String[][] board;

    public AIPlayer(String aiMarker, String playerMarker, String[][] board) {
        this.aiMarker = aiMarker;
        this.playerMarker = playerMarker;
        this.board = board;
    }

    public void makeMove(String difficulty) {
        switch (difficulty) {
            case "easy":
                makeEasyMove();
                break;
            case "medium":
                makeMediumMove();
                break;
            case "hard":
                makeHardMove();
                break;
        }
    }

    private void makeEasyMove() {
        List<int[]> possibleMoves = new ArrayList<>();

        // Loop through the board to find all cells that are not empty
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (!board[i][j].isEmpty()) {
                    // Check surrounding cells (8 directions)
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int newRow = i + di;
                            int newCol = j + dj;
                            if (isInBounds(newRow, newCol) && board[newRow][newCol].isEmpty()) {
                                possibleMoves.add(new int[]{newRow, newCol});
                            }
                        }
                    }
                }
            }
        }

        // Check if we need to block the player's winning move
        int[] blockingMove = findBlockingMove();
        if (blockingMove != null) {
            board[blockingMove[0]][blockingMove[1]] = aiMarker;
        } else if (!possibleMoves.isEmpty()) {
            // Make a random move from possible surrounding cells
            int[] move = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
            board[move[0]][move[1]] = aiMarker;
        } else {
            // Fallback to random move if no surrounding cells are found (very rare case)
            Random random = new Random();
            int row, col;
            do {
                row = random.nextInt(12);
                col = random.nextInt(12);
            } while (!board[row][col].isEmpty());
            board[row][col] = aiMarker;
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 12 && col >= 0 && col < 12;
    }

    private int[] findBlockingMove() {
        // Check rows for player's potential winning move
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyCol = -1;
                boolean blocked = false;

                for (int k = 0; k < 5; k++) {
                    if (board[i][j + k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i][j + k].isEmpty()) {
                        emptyCount++;
                        emptyCol = j + k;
                    } else if (k == 0 || k == 4) {
                        blocked = true;
                    }
                }

                if (playerCount == 4 && emptyCount == 1 && !blocked) {
                    return new int[]{i, emptyCol};
                }
            }
        }

        // Check columns for player's potential winning move
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i <= 12 - 5; i++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1;
                boolean blocked = false;

                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                    } else if (k == 0 || k == 4) {
                        blocked = true;
                    }
                }

                if (playerCount == 4 && emptyCount == 1 && !blocked) {
                    return new int[]{emptyRow, j};
                }
            }
        }

        // Check diagonals (top-left to bottom-right) for player's potential winning move
        for (int i = 0; i <= 12 - 5; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1, emptyCol = -1;
                boolean blocked = false;

                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j + k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j + k].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                        emptyCol = j + k;
                    } else if (k == 0 || k == 4) {
                        blocked = true;
                    }
                }

                if (playerCount == 4 && emptyCount == 1 && !blocked) {
                    return new int[]{emptyRow, emptyCol};
                }
            }
        }

        // Check diagonals (top-right to bottom-left) for player's potential winning move
        for (int i = 0; i <= 12 - 5; i++) {
            for (int j = 4; j < 12; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1, emptyCol = -1;
                boolean blocked = false;

                for (int k = 0; k < 5; k++) {
                    if (board[i + k][j - k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j - k].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                        emptyCol = j - k;
                    } else if (k == 0 || k == 4) {
                        blocked = true;
                    }
                }

                if (playerCount == 4 && emptyCount == 1 && !blocked) {
                    return new int[]{emptyRow, emptyCol};
                }
            }
        }

        // Check rows for player's potential move with 3 in a row and open ends
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j <= 12 - 4; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyCol = -1;

                for (int k = 0; k < 4; k++) {
                    if (board[i][j + k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i][j + k].isEmpty()) {
                        emptyCount++;
                        emptyCol = j + k;
                    }
                }

                if (playerCount == 3 && emptyCount == 1) {
                    return new int[]{i, emptyCol};
                }
            }
        }

        // Check columns for player's potential move with 3 in a row and open ends
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i <= 12 - 4; i++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1;

                for (int k = 0; k < 4; k++) {
                    if (board[i + k][j].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                    }
                }

                if (playerCount == 3 && emptyCount == 1) {
                    return new int[]{emptyRow, j};
                }
            }
        }

        // Check diagonals (top-left to bottom-right) for player's potential move with 3 in a row and open ends
        for (int i = 0; i <= 12 - 4; i++) {
            for (int j = 0; j <= 12 - 4; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1, emptyCol = -1;

                for (int k = 0; k < 4; k++) {
                    if (board[i + k][j + k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j + k].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                        emptyCol = j + k;
                    }
                }

                if (playerCount == 3 && emptyCount == 1) {
                    return new int[]{emptyRow, emptyCol};
                }
            }
        }

        // Check diagonals (top-right to bottom-left) for player's potential move with 3 in a row and open ends
        for (int i = 0; i <= 12 - 4; i++) {
            for (int j = 3; j < 12; j++) {
                int playerCount = 0;
                int emptyCount = 0;
                int emptyRow = -1, emptyCol = -1;

                for (int k = 0; k < 4; k++) {
                    if (board[i + k][j - k].equals(playerMarker)) {
                        playerCount++;
                    } else if (board[i + k][j - k].isEmpty()) {
                        emptyCount++;
                        emptyRow = i + k;
                        emptyCol = j - k;
                    }
                }

                if (playerCount == 3 && emptyCount == 1) {
                    return new int[]{emptyRow, emptyCol};
                }
            }
        }

        return null;
    }

    private void makeMediumMove() {
        int[] move = minimax(MAX_DEPTH - 1, aiMarker);
        int row = move[0];
        int col = move[1];
        board[row][col] = aiMarker;
    }

    private static final int MAX_DEPTH = 2;
    private static final int WIN_SCORE = 100000;
    private static final int FOUR_SCORE = 10000;
    private static final int THREE_SCORE = 1000;
    private static final int TWO_SCORE = 100;
    private static final int ONE_SCORE = 10;

    private void makeHardMove() {
        int[] move = minimax(MAX_DEPTH, aiMarker);
        int row = move[0];
        int col = move[1];
        board[row][col] = aiMarker;
    }

    private int[] minimax(int depth, String player) {
        List<int[]> possibleMoves = generateMoves();

        int bestScore = (player.equals(aiMarker)) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int[] bestMove = new int[]{-1, -1};

        if (possibleMoves.isEmpty() || depth == 0) {
            bestScore = evaluate();
        } else {
            for (int[] move : possibleMoves) {
                int row = move[0];
                int col = move[1];
                board[row][col] = player;

                if (player.equals(aiMarker)) {
                    currentScore = minimax(depth - 1, player.equals(aiMarker) ? playerMarker : aiMarker)[2];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove = move;
                    }
                } else {
                    currentScore = minimax(depth - 1, player.equals(aiMarker) ? playerMarker : aiMarker)[2];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestMove = move;
                    }
                }

                board[row][col] = ""; // Undo move
            }
        }

        return new int[]{bestMove[0], bestMove[1], bestScore};
    }

    private List<int[]> generateMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (board[i][j].isEmpty()) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    private int evaluate() {
        int score = 0;

        // Evaluate rows and columns
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                score += evaluateLine(board[i][j], board[i][j + 1], board[i][j + 2], board[i][j + 3], board[i][j + 4]);
                score += evaluateLine(board[j][i], board[j + 1][i], board[j + 2][i], board[j + 3][i], board[j + 4][i]);
            }
        }

        // Evaluate diagonals (top-left to bottom-right and top-right to bottom-left)
        for (int i = 0; i <= 12 - 5; i++) {
            for (int j = 0; j <= 12 - 5; j++) {
                score += evaluateLine(board[i][j], board[i + 1][j + 1], board[i + 2][j + 2], board[i + 3][j + 3], board[i + 4][j + 4]);
                score += evaluateLine(board[i][j + 4], board[i + 1][j + 3], board[i + 2][j + 2], board[i + 3][j + 1], board[i + 4][j]);
            }
        }

        return score;
    }

    private int evaluateLine(String cell1, String cell2, String cell3, String cell4, String cell5) {
        int score = 0;
        String[] line = {cell1, cell2, cell3, cell4, cell5};
        int aiCount = 0;
        int playerCount = 0;
        int emptyCount = 0;

        for (String cell : line) {
            if (cell.equals(aiMarker)) {
                aiCount++;
            } else if (cell.equals(playerMarker)) {
                playerCount++;
            } else {
                emptyCount++;
            }
        }

        // Evaluate based on the count of AI and player markers
        if (aiCount == 5) { // AI wins
            score += WIN_SCORE;
        } else if (playerCount == 5) { // Player wins
            score -= WIN_SCORE;
        } else if (aiCount == 4 && emptyCount == 1) {
            score += FOUR_SCORE;
        } else if (playerCount == 4 && emptyCount == 1) {
            score -= FOUR_SCORE;
        } else if (aiCount == 3 && emptyCount == 2) {
            score += THREE_SCORE;
        } else if (playerCount == 3 && emptyCount == 2) {
            score -= THREE_SCORE;
        } else if (aiCount == 2 && emptyCount == 3) {
            score += TWO_SCORE;
        } else if (playerCount == 2 && emptyCount == 3) {
            score -= TWO_SCORE;
        } else if (aiCount == 1 && emptyCount == 4) {
            score += ONE_SCORE;
        } else if (playerCount == 1 && emptyCount == 4) {
            score -= ONE_SCORE;
        }

        return score;
    }
}
