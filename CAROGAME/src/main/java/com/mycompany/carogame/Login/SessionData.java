/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carogame.Login;

import java.io.IOException;

/**
 *
 * @author anhphuc
 */
public class SessionData {
    public static User currentUser;
    
    public static void Logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void print(){
        System.out.println(currentUser);
    }
}
