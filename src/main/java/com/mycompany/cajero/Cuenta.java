/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajero;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author JoustinBergerMonge
 */
public class Cuenta {

    private long accountNumber;
    private BigDecimal balance;

    public Cuenta(long accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public static String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }

    public String createUniqueAccountNumber() {
        String numeroCuenta = generateAccountNumber();

        Conexion conexion = new Conexion();

        try (Connection conn = conexion.establishConnection()) {
            String sql = "SELECT COUNT(*) FROM cuenta WHERE numero_cuenta = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, numeroCuenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int cuentaExistente = rs.getInt(1);
                if (cuentaExistente > 0) {

                    numeroCuenta = createUniqueAccountNumber();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se creo la cuenta correctamente. Error: " + e.toString());

        }

        return numeroCuenta;

    }

    public void createAccount(long accountNumber, BigDecimal balance, String username) {
        setAccountNumber(accountNumber);
        setBalance(balance);

        Conexion connectionObject = new Conexion();

        String sql = "INSERT INTO Cuenta (numero_cuenta, username, saldo) VALUES (?, ?, ?);";

        try {
            CallableStatement cs = connectionObject.establishConnection().prepareCall(sql);

            cs.setLong(1, getAccountNumber());
            cs.setString(2, username);
            cs.setBigDecimal(3, getBalance());

            cs.execute();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se creo la cuenta correctamente. Error: " + e.toString());
        }

    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad a retirar debe ser mayor que cero.");
            return;
        }

        if (amount.compareTo(balance) > 0) {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente para retirar esa cantidad.");
            return;
        }

        setBalance(balance.subtract(amount));

        LocalDate date = LocalDate.now();

        LocalTime currentTime = LocalTime.now();

        Transaccion transaction = new Transaccion(date, currentTime, amount, TipoTransaccion.RETIRO);
        Conexion connectionObject = new Conexion();
        String updateSql = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";

        try (Connection conn = connectionObject.establishConnection(); PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setBigDecimal(1, balance);
            stmt.setLong(2, accountNumber);
            stmt.executeUpdate();

            transaction.createTransaction(accountNumber, date, currentTime, amount, TipoTransaccion.RETIRO, 0);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            int option = JOptionPane.showConfirmDialog(null, "¿Quieres imprimir un recibo?", "Recibo", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {

                JOptionPane.showMessageDialog(null, "Número de cuenta: " + String.valueOf(accountNumber) + "\nFecha: " + date.toString() + "\nHora: " + formattedTime + "\nMonto: $" + amount.toString(), "Recibo", JOptionPane.INFORMATION_MESSAGE);

            }

            JOptionPane.showMessageDialog(null, "Retiro exitoso. Nuevo saldo: $" + balance, "Retiro Exitoso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo completar el retiro. Error: " + e.toString());
        }

    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(null, "La cantidad a depositar debe ser mayor que cero.");
            return;
        }

        setBalance(balance.add(amount));

        LocalDate date = LocalDate.now();

        LocalTime currentTime = LocalTime.now();

        Transaccion transaction = new Transaccion(date, currentTime, amount, TipoTransaccion.DEPOSITO);
        Conexion connectionObject = new Conexion();
        String updateSql = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";

        try (Connection conn = connectionObject.establishConnection(); PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setBigDecimal(1, balance);
            stmt.setLong(2, accountNumber);
            stmt.executeUpdate();

            transaction.createTransaction(accountNumber, date, currentTime, amount, TipoTransaccion.DEPOSITO, 0);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            int option = JOptionPane.showConfirmDialog(null, "¿Quieres imprimir un recibo?", "Recibo", JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {

                JOptionPane.showMessageDialog(null, "Número de cuenta: " + String.valueOf(accountNumber) + "\nFecha: " + date.toString() + "\nHora: " + formattedTime + "\nMonto: $" + amount.toString(), "Recibo", JOptionPane.INFORMATION_MESSAGE);

            }

            JOptionPane.showMessageDialog(null, "Depósito exitoso. Nuevo saldo: $" + balance, "Depósito Exitoso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo completar el depósito. Error: " + e.toString());
        }
    }

}
