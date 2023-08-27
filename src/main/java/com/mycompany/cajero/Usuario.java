/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajero;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author JoustinBergerMonge
 */
public class Usuario {

    private String name;
    private String lastname;
    private String username;
    private int pin;
    private int accountAmount;
    private Cuenta cuenta;

    public Usuario(String name, String lastname, String username, int pin, int accountAmount) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.pin = pin;
        this.accountAmount = accountAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getaccountAmount() {
        return accountAmount;
    }

    public void setaccountAmount(int accountAmount) {
        this.accountAmount = accountAmount;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public void createUser(JTextField name, JTextField lastname, JTextField username, JPasswordField pin, JTextField accountAmount) {
        setName(name.getText());
        setLastname(lastname.getText());
        setUsername(username.getText().toLowerCase());
        setPin(Integer.parseInt(String.valueOf(pin.getPassword())));
        setaccountAmount(Integer.parseInt(accountAmount.getText()));

        Conexion connectionObject = new Conexion();

        String sql = "INSERT INTO Usuario (nombre, apellido, PIN, username) VALUES (?, ?, ?, ?)";

        try {
            CallableStatement cs = connectionObject.establishConnection().prepareCall(sql);

            cs.setString(1, getName());
            cs.setString(2, getLastname());
            cs.setInt(3, getPin());
            cs.setString(4, getUsername());

            cs.execute();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se insertó correctamente el usuario. Error: " + e.toString());
        }

    }

    public void changeUsernamePin(String username, int pin) {
        setPin(pin);

        Conexion connectionObject = new Conexion();

        String sql = "UPDATE usuario SET PIN = ? WHERE username = ?";

        try {
            CallableStatement cs = connectionObject.establishConnection().prepareCall(sql);
            cs.setInt(1, pin);
            cs.setString(2, username);
            cs.execute();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se hizo el cambio de PIN correctamente. Error: " + e.toString());
        }

    }
}
