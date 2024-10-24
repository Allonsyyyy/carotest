/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

/**
 *
 * @author anhphuc
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Match {
    private int matchId;
    private int player1Id;
    private String player2Id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String winnerId;

    public Match(int player1Id, String player2Id, LocalDateTime startTime , LocalDateTime endTime , String winnerId) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.winnerId = winnerId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }
    public Match(ResultSet resultSet) throws SQLException {
        this.matchId = resultSet.getInt("match_id");
        this.player1Id = resultSet.getInt("player1_id");
        this.player2Id = resultSet.getString("player2_id");
        this.startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
        this.endTime = resultSet.getTimestamp("end_time") != null ? resultSet.getTimestamp("end_time").toLocalDateTime() : null;
        this.winnerId = resultSet.getString("winner_id");
    }


}

