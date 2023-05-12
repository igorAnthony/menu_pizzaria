/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;

/**
 *
 * @author hawks
 */
public class NotaFiscal {
    private int id;
    private int id_usuario;
    private int id_endereco;
    private BigDecimal total;

    public NotaFiscal(int id_usuario, int id_endereco, BigDecimal total) {
        this.id_usuario = id_usuario;
        this.id_endereco = id_endereco;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public int getIdEndereco() {
        return id_endereco;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
