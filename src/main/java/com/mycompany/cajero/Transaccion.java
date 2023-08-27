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
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;

/**
 *
 * @author JoustinBergerMonge
 */
public class Transaccion {

    private LocalDate date;
    private LocalTime time;
    private BigDecimal ammount;
    private TipoTransaccion tipo;

    public Transaccion(LocalDate date, LocalTime time, BigDecimal ammount, TipoTransaccion tipo) {
        this.date = date;
        this.time = time;
        this.ammount = ammount;
        this.tipo = tipo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public void createTransaction(long accountNumber, LocalDate date, LocalTime currentTime, BigDecimal amount, TipoTransaccion tipoTransaccion, long destinataryAccountNumber) {

        Conexion connectionObject = new Conexion();

        String insertTransaccionSql = "INSERT INTO transacciones (numero_cuenta, fecha, hora, tipo, monto, numero_cuenta_destino) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            CallableStatement insertStmt = connectionObject.establishConnection().prepareCall(insertTransaccionSql);

            if (destinataryAccountNumber == 0) {
                insertStmt.setLong(1, accountNumber);
                insertStmt.setDate(2, Date.valueOf(date));
                insertStmt.setTime(3, Time.valueOf(currentTime));
                insertStmt.setString(4, tipoTransaccion.name());
                insertStmt.setBigDecimal(5, amount);
                insertStmt.setNull(6, java.sql.Types.BIGINT);
                insertStmt.executeUpdate();
            } else {
                insertStmt.setLong(1, accountNumber);
                insertStmt.setDate(2, Date.valueOf(date));
                insertStmt.setTime(3, Time.valueOf(currentTime));
                insertStmt.setString(4, tipoTransaccion.name());
                insertStmt.setBigDecimal(5, amount);
                insertStmt.setLong(6, destinataryAccountNumber);
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "No se creo la transacción correctamente. Error: " + e.toString());
        }

    }

}
