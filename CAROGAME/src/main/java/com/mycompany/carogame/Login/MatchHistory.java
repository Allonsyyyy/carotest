/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author anhphuc
 */

public class MatchHistory {
    private int historyId;
    private int matchId;
    private int playerId;
    private int moveNumber;
    private double moveTime;

    public MatchHistory(int historyId, int matchId, int playerId, int moveNumber, double moveTime) {
        this.historyId = historyId;
        this.matchId = matchId;
        this.playerId = playerId;
        this.moveNumber = moveNumber;
        this.moveTime = moveTime;
    }
     public MatchHistory(int matchId, int playerId, int moveNumber, double moveTime) {
        this.matchId = matchId;
        this.playerId = playerId;
        this.moveNumber = moveNumber;
        this.moveTime = moveTime;
    }
    public MatchHistory(ResultSet resultSet) throws SQLException {
        this.historyId = resultSet.getInt("history_id");
        this.matchId = resultSet.getInt("match_id");
        this.playerId = resultSet.getInt("player_id");
        this.moveNumber = resultSet.getInt("move_number");
        this.moveTime = resultSet.getDouble("move_time");
    }
    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public double getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(double moveTime) {
        this.moveTime = moveTime;
    }
}
