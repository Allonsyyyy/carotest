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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchHistoryEntity extends BaseEntity<MatchHistory> {
    private static MatchHistoryEntity instance = null;
    
    private MatchHistoryEntity() {
    }
    
    public synchronized static MatchHistoryEntity getInstance() {
        if(instance == null) {
            instance = new MatchHistoryEntity();
        }
        
        return instance;
    }

    @Override
    public List<MatchHistory> findAll() {
        List<MatchHistory> dataList = new ArrayList<>();
        
        openConnection();
        
        String sql = "SELECT * FROM MatchHistory";
        try {
            statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                MatchHistory matchHistory = new MatchHistory(resultSet);
                dataList.add(matchHistory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
        
        return dataList;
    }

    @Override
    public void insert(MatchHistory item) {
        openConnection();
        
        String sql = "INSERT INTO MatchHistory (match_id, player_id, move_number, move_time) VALUES (?, ?, ?, ?)";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getMatchId());
            statement.setInt(2, item.getPlayerId());
            statement.setInt(3, item.getMoveNumber());
            statement.setDouble(4, item.getMoveTime()); // move_time lưu dưới dạng số giờ
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public void update(MatchHistory item) {
        openConnection();
        
        String sql = "UPDATE MatchHistory SET match_id=?, player_id=?, move_number=?, move_time=? WHERE history_id=?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getMatchId());
            statement.setInt(2, item.getPlayerId());
            statement.setInt(3, item.getMoveNumber());
            statement.setDouble(4, item.getMoveTime()); // move_time lưu dưới dạng số giờ
            statement.setInt(5, item.getHistoryId());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public void delete(MatchHistory item) {
        openConnection();
        
        String sql = "DELETE FROM MatchHistory WHERE history_id = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getHistoryId());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public MatchHistory findById(MatchHistory item) {
        MatchHistory itemFind = null;
        
        openConnection();
        
        String sql = "SELECT * FROM MatchHistory WHERE history_id = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getHistoryId());
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                itemFind = new MatchHistory(resultSet);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MatchHistoryEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
        
        return itemFind;
    }
}