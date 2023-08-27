/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cajero;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author JoustinBergerMonge
 */
class CuentaPanel extends JPanel {

    private JLabel numeroCuentaLabel;
    private JLabel saldoLabel;

    public CuentaPanel(Cuenta cuenta) {
        setLayout(new GridLayout(2, 1));

        numeroCuentaLabel = new JLabel("Número de cuenta: " + cuenta.getAccountNumber());
        saldoLabel = new JLabel("Saldo: $" + cuenta.getBalance());

        add(numeroCuentaLabel);
        add(saldoLabel);
    }
}
