/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

/**
 *
 * @author anhphuc
 */
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author anhphuc
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String hash_password;
    private String encryptedPassword;

    private static final String ALGORITHM = "AES";
    private static final byte[] secretKey = "1202033123456789".getBytes(); 

    

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(User itemUser) {
        User u = new User();
        this.id = itemUser.getId();
        this.username = itemUser.getUsername();
        this.password = itemUser.getPassword();
        this.email = itemUser.getEmail();
        this.hash_password = itemUser.encryptPassword(itemUser.getPassword());
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.hash_password = this.encryptPassword(password);
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.hash_password = this.encryptPassword(password);
    }

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hash_password = this.encryptPassword(password);
    }
    public User(int id, String username, String password, String email , String hash_password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.hash_password = hash_password;
    }
    public User(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt("user_id");
            this.username = resultSet.getString("username");
            this.password = resultSet.getString("password");
            this.email = resultSet.getString("email");
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHash_password() {
        return hash_password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "user_id =" + id + ", username=" + username + ", password=" + password + ", email=" + email;
    }

    public void display() {
        System.out.println(this);
    }

    public void input() {
        Scanner scan = new Scanner(System.in);

        System.out.println("Nhap ten dang nhap: ");
        username = scan.nextLine();
        System.out.println("Nhap mat khau: ");
        password = scan.nextLine();
        System.out.println("Nhap email: ");
        email = scan.nextLine();
    }
    public String encryptPassword(String plainPassword) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptPassword(String encryptedPassword) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
