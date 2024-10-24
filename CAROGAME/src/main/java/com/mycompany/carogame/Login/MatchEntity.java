/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.carogame.Login.Match;
import com.mycompany.carogame.Login.MatchEntity;

/**
 *
 * @author anhphuc
 */
public class MatchEntity extends BaseEntity<Match> {

    private static MatchEntity instance = null;

    public MatchEntity() {
    }

    public synchronized static MatchEntity getInstance() {
        if (instance == null) {
            instance = new MatchEntity();
        }

        return instance;
    }

    @Override
    public List<Match> findAll() {
        List<Match> dataList = new ArrayList<>();

        openConnection();

        String sql = "SELECT * FROM Matches";
        try {
            statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Match match = new Match(resultSet);
                dataList.add(match);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MatchEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeConnection();

        return dataList;
    }

    @Override
public void insert(Match item) {
    openConnection();
    
    String sql = "INSERT INTO Matches (player1_id, player2_id, start_time, end_time, winner_id) VALUES (?, ?, ?, ?, ?)";
    try {
        statement = con.prepareStatement(sql);
        statement.setInt(1, item.getPlayer1Id());
        statement.setString(2, item.getPlayer2Id());
        
        // Chuyển đổi LocalDateTime thành Timestamp
        statement.setTimestamp(3, java.sql.Timestamp.valueOf(item.getStartTime()));
        statement.setTimestamp(4, item.getEndTime() != null ? java.sql.Timestamp.valueOf(item.getEndTime()) : null);
        statement.setString(5, item.getWinnerId());
        
        statement.execute();
    } catch (SQLException ex) {
        ex.printStackTrace(); // In ra chi tiết lỗi
        Logger.getLogger(MatchEntity.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        closeConnection();
    }
}

    @Override
    public void update(Match item) {
        openConnection();

        String sql = "UPDATE Matches SET player1_id=?, player2_id=?, start_time=?, end_time=?, winner_id=? WHERE match_id=?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getPlayer1Id());
            statement.setString(2, item.getPlayer2Id());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(item.getStartTime()));
            statement.setTimestamp(4, item.getEndTime() != null ? java.sql.Timestamp.valueOf(item.getEndTime()) : null);
            statement.setString(5, item.getWinnerId());
            statement.setInt(6, item.getMatchId());

            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MatchEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeConnection();
    }

    @Override
    public void delete(Match item) {
        openConnection();

        String sql = "DELETE FROM Matches WHERE match_id = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getMatchId());

            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MatchEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeConnection();
    }

    @Override
    public Match findById(Match item) {
        Match itemFind = null;

        openConnection();

        String sql = "SELECT * FROM Matches WHERE player1_id=? AND player2_id=? AND start_time=? AND end_time=?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getPlayer1Id());
            statement.setString(2, item.getPlayer2Id());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(item.getStartTime()));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(item.getEndTime()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                itemFind = new Match(resultSet);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MatchEntity.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeConnection();

        return itemFind;
    }
}
