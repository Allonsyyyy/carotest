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
public class UserEntity extends BaseEntity<User> {
    private static UserEntity instance = null;
    
    private UserEntity() {
    }
    
    public synchronized static UserEntity getInstance() {
        if(instance == null) {
            instance = new UserEntity();
        }
        
        return instance;
    }

    @Override
    public List<User> findAll() {
        List<User> dataList = new ArrayList<>();
        
        openConnection();
        
        String sql = "select * from users";
        try {
            statement = con.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                User user = new User(resultSet);
                dataList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
        
        return dataList;
    }

    @Override
    public void insert(User item) {
        openConnection();
        
        String sql = "insert into users(username, password, email ) values (?, ?, ?)";
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, item.getUsername());
            statement.setString(2, item.getHash_password());
            statement.setString(3, item.getEmail());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public void update(User item) {
        openConnection();
        
        String sql = "update users set username=?, password=?, email=? where user_id=?";
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, item.getUsername());
            statement.setString(2, item.getPassword());
            statement.setString(3, item.getEmail());
            statement.setInt(4, item.getId());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public void delete(User item) {
        openConnection();
        
        String sql = "delete from users where user_id =?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getId());
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
    }

    @Override
    public User findById(User item) {
        User itemFind = null;
        
        openConnection();
        
        String sql = "select * from users where user_id = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, item.getId());
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                itemFind = new User(resultSet);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
        
        return itemFind;
    }
    public User findById2(int id) {
        User itemFind = null;
        
        openConnection();
        
        String sql = "select * from users where user_id = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                itemFind = new User(resultSet);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        closeConnection();
        
        return itemFind;
    }
    public int findAcount(String username, String password) {
    // Kiểm tra điều kiện đặc biệt trước khi mở kết nối cơ sở dữ liệu
    if (username.equals("Admin") && password.equals("123")) {
        return 0;
    }
    User u = new User();
    String hp = u.encryptPassword(password);
    
    User acountUser = null;
    openConnection(); // Chỉ mở kết nối nếu không phải trường hợp đặc biệt
    
    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
    try {
        statement = con.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, hp);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            acountUser = new User(resultSet);
            break;
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        closeConnection();
    }
    return acountUser == null ? -1 : acountUser.getId();
}

     public int findAcountRegiser(String username ) {
       
        User acountUser = null;
        openConnection();
        
        String sql = "select * from users where username = ?";
        try {
            statement = con.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                acountUser = new User(resultSet);
                break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            closeConnection();
        }
        return acountUser == null ? -1 : 1 ;
    }
}

