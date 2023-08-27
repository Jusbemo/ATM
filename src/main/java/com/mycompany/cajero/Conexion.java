/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajero;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author JoustinBergerMonge
 */
public class Conexion {
    
    Connection connection = null;
    String url = "jdbc:mysql://localhost:3306/cajero";
    String username = "root";
    String password = "admin123";

    public Connection establishConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to database. Error: " + e.toString());
        }
        
        return connection;

    }
}
